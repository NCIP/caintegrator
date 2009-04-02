package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
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

    public static Platform checkLoadAffymetrixArrayDesign(File cdfFile, File annotationFile, ArrayDataService service) 
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
        ReporterList geneReporters = getReporterList(ReporterTypeEnum.GENE_EXPRESSION_GENE, platform);
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
        ReporterList probeSetReporters = getReporterList(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, platform);
        assertEquals(platform, probeSetReporters.getPlatform());
        assertEquals(cdfReader.getCdfData().getHeader().getNumProbeSets(), probeSetReporters.getReporters().size());
        for (AbstractReporter abstractReporter : probeSetReporters.getReporters()) {
            GeneExpressionReporter reporter = (GeneExpressionReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
        }
    }

    private static ReporterList getReporterList(ReporterTypeEnum type, Platform platform) {
        for (ReporterList reporterList : platform.getReporterLists()) {
            if (type.equals(reporterList.getReporterType())) {
                return reporterList;
            }
        }
        return null;
    }

    private static void checkPlatform(Platform platform, AffymetrixCdfReader cdfReader) {
        assertNotNull(platform);
        assertEquals(cdfReader.getCdfData().getChipType(), platform.getName());
        assertEquals(PlatformVendorEnum.AFFYMETRIX, platform.getVendor());
        assertEquals(2, platform.getReporterLists().size());
    }

}
