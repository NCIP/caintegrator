package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ArrayDesignChecker {

    public static Platform checkLoadAffymetrixSnpArrayDesign(File cdfFile, File annotationFile, ArrayDataService service) throws PlatformLoadingException, AffymetrixCdfReadException {
        AffymetrixCdfReader cdfReader = AffymetrixCdfReader.create(cdfFile);
        AffymetrixPlatformSource source = new AffymetrixPlatformSource(annotationFile);
        Platform platform = service.loadArrayDesign(source);
        if (platform.getId() == null) {
            platform.setId(1L);
        }
        assertNotNull(platform);
        assertEquals(cdfReader.getCdfData().getChipType(), platform.getName());
        assertEquals(PlatformVendorEnum.AFFYMETRIX, platform.getVendor());
        checkSnpReporters(platform, cdfReader);
        return platform;
    }

    private static void checkSnpReporters(Platform platform, AffymetrixCdfReader cdfReader) {
        PlatformHelper platformHelper = new PlatformHelper(platform);
        ReporterList reporters = platformHelper.getReporterList(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        for (AbstractReporter abstractReporter : reporters.getReporters()) {
            DnaAnalysisReporter reporter = (DnaAnalysisReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
            assertNotNull(reporter.getAlleleA());
            assertNotNull(reporter.getAlleleB());
        }
    }

    public static Platform checkLoadAffymetrixExpressionArrayDesign(File cdfFile, File annotationFile, ArrayDataService service) 
    throws PlatformLoadingException, AffymetrixCdfReadException {
        AffymetrixCdfReader cdfReader = AffymetrixCdfReader.create(cdfFile);
        AffymetrixPlatformSource source = new AffymetrixPlatformSource(annotationFile);
        Platform platform = service.loadArrayDesign(source);
        if (platform.getId() == null) {
            platform.setId(1L);
        }
        checkPlatform(platform, cdfReader);
        checkProbeSetReporters(platform, cdfReader);
        checkGeneReporters(platform);
        return platform;
    }

    public static Platform checkLoadAgilentArrayDesign(File cdfFile, File annotationFile, ArrayDataService service) 
    throws PlatformLoadingException {
        AgilentPlatformSource source = new AgilentPlatformSource(annotationFile, "Agilent platform");
        Platform platform = service.loadArrayDesign(source);
        if (platform.getId() == null) {
            platform.setId(1L);
        }
        checkGeneReporters(platform);
        return platform;
    }

    private static void checkGeneReporters(Platform platform) {
        PlatformHelper platformHelper = new PlatformHelper(platform);
        ReporterList geneReporters = platformHelper.getReporterList(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        assertEquals(platform, geneReporters.getPlatform());
        Set<String> geneSymbols = new HashSet<String>();
        for (AbstractReporter reporter : geneReporters.getReporters()) {
            assertEquals(1, reporter.getGenes().size());
            Gene gene = reporter.getGenes().iterator().next();
            assertEquals(reporter.getName(), gene.getSymbol());
            geneSymbols.add(gene.getSymbol());
        }
        assertEquals(geneReporters.getReporters().size(), geneSymbols.size());
    }

    private static void checkProbeSetReporters(Platform platform, AffymetrixCdfReader cdfReader) {
        PlatformHelper platformHelper = new PlatformHelper(platform);
        ReporterList probeSetReporters = platformHelper.getReporterList(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        assertEquals(platform, probeSetReporters.getPlatform());
        assertEquals(cdfReader.getCdfData().getHeader().getNumProbeSets(), probeSetReporters.getReporters().size());
        for (AbstractReporter abstractReporter : probeSetReporters.getReporters()) {
            GeneExpressionReporter reporter = (GeneExpressionReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
        }
    }

    private static void checkPlatform(Platform platform, AffymetrixCdfReader cdfReader) {
        assertNotNull(platform);
        assertEquals(cdfReader.getCdfData().getChipType(), platform.getName());
        assertEquals(PlatformVendorEnum.AFFYMETRIX, platform.getVendor());
        assertEquals(2, platform.getReporterLists().size());
    }

}
