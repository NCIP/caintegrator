package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterSet;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ArrayDesignChecker {

    public static Platform checkLoadArrayDesign(File cdfFile, File annotationFile, ArrayDataService service) 
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

    private static void checkGeneReporters(Platform platform) {
        ReporterSet geneReporters = getReporterSet(ReporterTypeEnum.GENE_EXPRESSION_GENE, platform);
        assertEquals(platform, geneReporters.getPlatform());
        Set<String> geneSymbols = new HashSet<String>();
        for (AbstractReporter abstractReporter : geneReporters.getReporters()) {
            GeneExpressionReporter reporter = (GeneExpressionReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
            assertNotNull(reporter.getGene());
            assertEquals(reporter.getName(), reporter.getGene().getSymbol());
            geneSymbols.add(reporter.getGene().getSymbol());
        }
        assertEquals(geneReporters.getReporters().size(), geneSymbols.size());
    }

    private static void checkProbeSetReporters(Platform platform, AffymetrixCdfReader cdfReader) {
        ReporterSet probeSetReporters = getReporterSet(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, platform);
        assertEquals(platform, probeSetReporters.getPlatform());
        assertEquals(cdfReader.getCdfData().getHeader().getNumProbeSets(), probeSetReporters.getReporters().size());
        for (AbstractReporter abstractReporter : probeSetReporters.getReporters()) {
            GeneExpressionReporter reporter = (GeneExpressionReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
        }
    }

    private static ReporterSet getReporterSet(ReporterTypeEnum type, Platform platform) {
        for (ReporterSet reporterSet : platform.getReporterSets()) {
            if (type.equals(reporterSet.getReporterType())) {
                return reporterSet;
            }
        }
        return null;
    }

    private static void checkPlatform(Platform platform, AffymetrixCdfReader cdfReader) {
        assertNotNull(platform);
        assertEquals(cdfReader.getCdfData().getChipType(), platform.getName());
        assertEquals("Affymetrix", platform.getVendor());
        assertEquals(2, platform.getReporterSets().size());
    }

}
