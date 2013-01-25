/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Used to load Affymetrix array designs.
 */
class AffymetrixExpressionPlatformLoader extends AbstractExpressionPlatformLoader {
    
    private static final Logger LOGGER = Logger.getLogger(AffymetrixExpressionPlatformLoader.class);
    
    private static final String PROBE_SET_ID_HEADER = "Probe Set ID";
    private static final String GENE_SYMBOL_HEADER = "Gene Symbol";
    private static final String ENTREZ_GENE_HEADER = "Entrez Gene";
    private static final String ENSEMBL_HEADER = "Ensembl";
    private static final String UNIGENE_ID_HEADER = "UniGene ID";
    private static final String CHIP_TYPE_HEADER = "chip_type";
    private static final String VERSION_HEADER = "netaffx-annotation-netaffx-build";
    private static final String GENOME_VERSION_HEADER = "genome-version";
    private static final String NO_VALUE_INDICATOR = "---";
    private static final String[] REQUIRED_HEADERS = {PROBE_SET_ID_HEADER, GENE_SYMBOL_HEADER, ENTREZ_GENE_HEADER,
        ENSEMBL_HEADER, UNIGENE_ID_HEADER};
    
    private Map<String, String> fileHeaders;

    AffymetrixExpressionPlatformLoader(AffymetrixExpressionPlatformSource source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException {
        Platform platform = createPlatform(PlatformVendorEnum.AFFYMETRIX);
        loadAnnotationFiles(platform, dao);
        return platform;
    }

    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return getChipType(getAnnotationFiles().get(0));
    }
    
    private String getChipType(File annotationFile)
    throws PlatformLoadingException {
        try {
            setAnnotationFileReader(new CSVReader(new FileReader(annotationFile)));
            loadHeaders();
            return getHeaderValue(CHIP_TYPE_HEADER);
        } catch (FileNotFoundException e) {
            LOGGER.error("Annotation file not found: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IO Error: " + e.getMessage());
        }
        throw new PlatformLoadingException("Unable extracting Chip Type from annotation file.");
    }

    void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException {
        try {
            String chipType = getChipType(annotationFile);
            platform.setName(chipType);
            platform.setVersion(getHeaderValue(VERSION_HEADER));
            ReporterList geneReporters = platform.addReporterList(chipType, ReporterTypeEnum.GENE_EXPRESSION_GENE);
            ReporterList probeSetReporters = 
                platform.addReporterList(chipType, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            probeSetReporters.setGenomeVersion(getHeaderValue(GENOME_VERSION_HEADER));
            loadAnnotations(geneReporters, probeSetReporters, dao);
            probeSetReporters.sortAndLoadReporterIndexes();
            geneReporters.sortAndLoadReporterIndexes();
        } catch (IOException e) {
            throw new PlatformLoadingException("Couldn't read annotation file " + getAnnotationFileNames(), e);
        }
    }

    private void loadHeaders() throws PlatformLoadingException, IOException {
        AffymetrixAnnotationHeaderReader headerReader = new AffymetrixAnnotationHeaderReader(
                getAnnotationFileReader());
        fileHeaders = headerReader.getFileHeaders();
        loadAnnotationHeaders(headerReader.getDataHeaders(), REQUIRED_HEADERS);
    }
    
    void loadAnnotations(String[] fields, ReporterList geneReporters, ReporterList probeSetReporters, 
            CaIntegrator2Dao dao) {
        String[] symbols = getSymbols(fields);
        String probeSetName = getAnnotationValue(fields, PROBE_SET_ID_HEADER);
        Set<Gene> genes = handleGeneSymbols(symbols, fields, geneReporters, dao);
        handleProbeSet(probeSetName, genes, probeSetReporters);
    }

    @SuppressWarnings("PMD.UseStringBufferForStringAppends")    // Invalid rule violation
    private String[] getSymbols(String[] fields) {
        String[] symbols = getAnnotationValue(fields, GENE_SYMBOL_HEADER).split("///");
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = symbols[i].trim();
        }
        return symbols;
    }

    private String getHeaderValue(String headerName) {
        return fileHeaders.get(headerName);
    }

    private Set<Gene> handleGeneSymbols(String[] symbols, String[] fields, ReporterList geneReporters, 
            CaIntegrator2Dao dao) {
        Set<Gene> genes = new HashSet<Gene>(symbols.length);
        for (String symbol : symbols) {
            Gene gene = getSymbolToGeneMap().get(symbol.toUpperCase(Locale.getDefault()));
            if (gene == null && !symbol.equals(NO_VALUE_INDICATOR)) {
                gene = lookupOrCreateGene(fields, symbol, dao);
                addGeneReporter(geneReporters, gene);
            }
            if (gene != null) {
                genes.add(gene);
            }
        }
        return genes;
    }

    protected Gene createGene(String symbol, String[] fields) {
        Gene gene = new Gene();
        gene.setSymbol(symbol);
        gene.setEntrezgeneID(getAnnotationValue(fields, ENTREZ_GENE_HEADER, NO_VALUE_INDICATOR));
        gene.setEnsemblgeneID(getAnnotationValue(fields, ENSEMBL_HEADER, NO_VALUE_INDICATOR));
        gene.setUnigeneclusterID(getAnnotationValue(fields, UNIGENE_ID_HEADER, NO_VALUE_INDICATOR));
        return gene;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

}
