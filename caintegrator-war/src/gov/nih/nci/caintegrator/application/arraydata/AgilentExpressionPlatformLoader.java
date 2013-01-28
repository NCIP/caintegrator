/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Used to load Agilent array designs.
 */
class AgilentExpressionPlatformLoader extends AbstractExpressionPlatformLoader {
    
    private static final Logger LOGGER = Logger.getLogger(AgilentExpressionPlatformLoader.class);
    
    private static final String PROBE_SET_ID_HEADER = "ProbeID";
    private static final String GENE_SYMBOL_HEADER = "GeneSymbol";
    private static final String GENE_NAME_HEADER = "GeneName";
    private static final String ACCESSIONS_HEADER = "Accessions";
    private static final String NO_GENE_SYMBOL = "";
    private static final String[] REQUIRED_HEADERS = {PROBE_SET_ID_HEADER, GENE_SYMBOL_HEADER, GENE_NAME_HEADER,
        ACCESSIONS_HEADER};

    // ADF file headers
    private static final String ADF_FIRST_FIELD_HEADER = "X_Block";
    private static final String ADF_PROBE_SET_ID_HEADER = "Reporter_ID";
    private static final String ADF_GENE_SYMBOL_HEADER = "Composite";
    private static final String ADF_NO_GENE_SYMBOL = "NOMATCH";
    private static final String[] ADF_REQUIRED_HEADERS = {ADF_FIRST_FIELD_HEADER, ADF_PROBE_SET_ID_HEADER,
        ADF_GENE_SYMBOL_HEADER};

    private String firstFieldHeader;
    private String probeIdHeader;
    private String geneSymbolHeader;
    private String[] requiredHeaders;
    private String noGeneSymbol;
    private final Set<String> probeSetNames;

    AgilentExpressionPlatformLoader(AgilentExpressionPlatformSource source) {
        super(source);
        probeSetNames = new HashSet<String>();
        setFieldHeaders();
    }
    
    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return getSource().getPlatformName();
    }
    
    private void setFieldHeaders() {
        if (((AgilentExpressionPlatformSource) getSource()).getPlatformFileName().endsWith(".adf")) {
            firstFieldHeader = ADF_FIRST_FIELD_HEADER;
            probeIdHeader = ADF_PROBE_SET_ID_HEADER;
            geneSymbolHeader = ADF_GENE_SYMBOL_HEADER;
            noGeneSymbol = ADF_NO_GENE_SYMBOL;
            requiredHeaders = ADF_REQUIRED_HEADERS;
        } else {
            firstFieldHeader = PROBE_SET_ID_HEADER;
            probeIdHeader = PROBE_SET_ID_HEADER;
            geneSymbolHeader = GENE_SYMBOL_HEADER;
            noGeneSymbol = NO_GENE_SYMBOL;
            requiredHeaders = REQUIRED_HEADERS;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException {
        Platform platform = createPlatform(PlatformVendorEnum.AGILENT);
        loadAnnotationFiles(platform, dao);
        return platform;
    }

    void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException {
        try {
            setAnnotationFileReader(new CSVReader(new FileReader(annotationFile), '\t'));
            loadHeaders();
            platform.setName(((AgilentExpressionPlatformSource) getSource()).getPlatformName());
            ReporterList geneReporters = 
                platform.addReporterList(((AgilentExpressionPlatformSource) getSource()).getPlatformName(),
                        ReporterTypeEnum.GENE_EXPRESSION_GENE);
            ReporterList probeSetReporters = 
                platform.addReporterList(((AgilentExpressionPlatformSource) getSource()).getPlatformName(),
                        ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            loadAnnotations(geneReporters, probeSetReporters, dao);
            probeSetReporters.sortAndLoadReporterIndexes();
            geneReporters.sortAndLoadReporterIndexes();
        } catch (IOException e) {
            LOGGER.error("IO Error reading annotation file.");
            throw new PlatformLoadingException("Couldn't read annotation file " + getAnnotationFileNames(), e);
        }
    }

    private void loadHeaders() throws PlatformLoadingException, IOException {
        String[] fields;
        while ((fields = getAnnotationFileReader().readNext()) != null) {
            if (isAnnotationHeadersLine(fields, firstFieldHeader)) {
                loadAnnotationHeaders(fields, requiredHeaders);
                return;
            }
        }        
        throw new PlatformLoadingException("Invalid Agilent annotation file; headers not found in file: " 
                + getAnnotationFileNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void loadAnnotations(String[] fields, ReporterList geneReporters, ReporterList probeSetReporters, 
            CaIntegrator2Dao dao) {
        String probeSetName = getAnnotationValue(fields, probeIdHeader);
        if (probeSetName.startsWith("A_") && !probeSetNames.contains(probeSetName)) {
            probeSetNames.add(probeSetName);
            extractGene(probeSetName, fields, geneReporters, probeSetReporters, dao);
        }
    }
    
    private void extractGene(String probeSetName, String[] fields, ReporterList geneReporters,
            ReporterList probeSetReporters, CaIntegrator2Dao dao) {
        String symbol = getAnnotationValue(fields, geneSymbolHeader);
        Gene gene = getSymbolToGeneMap().get(symbol.toUpperCase(Locale.getDefault()));
        if (gene == null && !symbol.equals(noGeneSymbol) && !symbol.matches("^chr\\d+")) {
            gene = lookupOrCreateGene(fields, symbol, dao);
            addGeneReporter(geneReporters, gene);
        }
        Set<Gene> genes = new HashSet<Gene>();
        if (gene != null) {
            genes.add(gene);
        }
        handleProbeSet(probeSetName, genes, probeSetReporters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Gene createGene(String symbol, String[] fields) {
        Gene gene = new Gene();
        gene.setSymbol(symbol.toUpperCase(Locale.getDefault()));
        gene.setGenbankAccession(getAnnotationValue(fields, ACCESSIONS_HEADER));
        gene.setFullName(getAnnotationValue(fields, GENE_NAME_HEADER));
        return gene;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

}
