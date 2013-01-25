/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Used to load Agilent CopyNumber array designs.
 */
class AgilentTcgaAdfCghPlatformLoader extends AbstractPlatformLoader {
    
    private static final Logger LOGGER = Logger.getLogger(AgilentTcgaAdfCghPlatformLoader.class);
    
    // ADF file headers
    private static final String FIRST_FIELD_HEADER = "X_Block";
    private static final String PROBE_SET_ID_HEADER = "Reporter_ID";
    private static final String GENE_SYMBOL_HEADER = "Genomic_Symbol";
    private static final String COMPOSITE_CHR_COORDS_HEADER = "Composite_chr_coords";
    private static final String NO_GENE_SYMBOL = "unmapped";
    private static final String NA_COORDS = "NA-NA";
    private static final String[] REQUIRED_HEADERS = {FIRST_FIELD_HEADER, PROBE_SET_ID_HEADER,
        GENE_SYMBOL_HEADER, COMPOSITE_CHR_COORDS_HEADER};
    
    private static final Map<String, String> GENOMIC_VERSION_MAPPING = new HashMap<String, String>();
    static {
        GENOMIC_VERSION_MAPPING.put("36", "Hg18");
        GENOMIC_VERSION_MAPPING.put("37", "Hg19");
    }

    AgilentTcgaAdfCghPlatformLoader(AgilentCnPlatformSource source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Platform load(CaIntegrator2Dao dao) throws PlatformLoadingException {
        Platform platform = createPlatform(PlatformVendorEnum.AGILENT);
        platform.setName(((AgilentCnPlatformSource) getSource()).getPlatformName());
        loadAnnotationFiles(platform, dao);
        return platform;
    }

    @Override
    public String getPlatformName() throws PlatformLoadingException {
        return getSource().getPlatformName();
    }
    
    void handleAnnotationFile(File annotationFile, Platform platform, CaIntegrator2Dao dao)
    throws PlatformLoadingException {
        try {
            setAnnotationFileReader(new CSVReader(new FileReader(annotationFile), '\t'));
            loadHeaders();
            ReporterList reporterList = 
                platform.addReporterList(platform.getName(), ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            loadAnnotations(reporterList, dao);
            reporterList.sortAndLoadReporterIndexes();
        } catch (IOException e) {
            throw new PlatformLoadingException("Couldn't read annotation file " + getAnnotationFileNames(), e);
        }
    }

    protected void loadAnnotations(ReporterList reporterList, CaIntegrator2Dao dao) 
    throws IOException {
        String[] fields;
        while ((fields = getAnnotationFileReader().readNext()) != null) {
            loadAnnotations(fields, reporterList, dao);
        }
    }

    private void loadHeaders() throws PlatformLoadingException, IOException {
        String[] fields;
        while ((fields = getAnnotationFileReader().readNext()) != null) {
            if (isAnnotationHeadersLine(fields, FIRST_FIELD_HEADER)) {
                loadAnnotationHeaders(fields, REQUIRED_HEADERS);
                return;
            }
        }        
        throw new PlatformLoadingException("Invalid Agilent annotation file; headers not found in file: " 
                + getAnnotationFileNames());
    }

    private void loadAnnotations(String[] fields, ReporterList reporterList, 
            CaIntegrator2Dao dao) {
        String[] symbols = getSymbols(fields);
        String probeSetName = getAnnotationValue(fields, PROBE_SET_ID_HEADER);
        if (probeSetName.startsWith("A_")) {
            Set<Gene> genes = getGenes(symbols, fields, dao);
            handleProbeSet(probeSetName, genes, fields, reporterList);
        }
    }

    private void handleProbeSet(String probeSetName, Set<Gene> genes, String[] fields,
            ReporterList reporterList) {
        String[] chrCoords = getAnnotationValue(fields, COMPOSITE_CHR_COORDS_HEADER).split(":");
        if (chrCoords.length < 3) {
            getLogger().error("Platform Loading: "
                    + "invalid chromosome coords found for this probe: " 
                    + probeSetName);
        } else {
            DnaAnalysisReporter reporter = new DnaAnalysisReporter();
            reporter.setName(probeSetName);
            reporterList.getReporters().add(reporter);
            reporter.setReporterList(reporterList);
            reporter.getGenes().addAll(genes);
            reporterList.setGenomeVersion(mapGenomicVersion(chrCoords[0].substring(1)));
            reporter.setChromosome(chrCoords[1]);
            reporter.setPosition(getIntegerValue(chrCoords[2]));
        }
    }
    
    private String mapGenomicVersion(String version) {
        return GENOMIC_VERSION_MAPPING.get(version.split("\\.")[0]);
    }
    
    private Integer getIntegerValue(String value) {
        if (value.equalsIgnoreCase(NA_COORDS)) {
            return null;
        } else {
            return Integer.parseInt(value.split("-")[0]);
        }
    }

    private Set<Gene> getGenes(String[] symbols, String[] fields, CaIntegrator2Dao dao) {
        Set<Gene> genes = new HashSet<Gene>(symbols.length);
        for (String symbol : symbols) {
            if (!symbol.matches("chr.+:\\d+-\\d+") && !symbol.equalsIgnoreCase(NO_GENE_SYMBOL)) {
                addGene(genes, symbol, fields, dao);
            }
        }
        return genes;
    }
    
    private void addGene(Set<Gene> genes, String symbol, String[] fields, CaIntegrator2Dao dao) {
        Gene gene = getSymbolToGeneMap().get(symbol.toUpperCase(Locale.getDefault()));
        if (gene == null) {
            gene = lookupOrCreateGene(fields, symbol, dao);
        }
        if (gene != null) {
            genes.add(gene);
        }
    }

    @SuppressWarnings("PMD.UseStringBufferForStringAppends")    // Invalid rule violation
    private String[] getSymbols(String[] fields) {
        String[] symbols = getAnnotationValue(fields, GENE_SYMBOL_HEADER).split("///");
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = symbols[i].trim();
        }
        return symbols;
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

}
