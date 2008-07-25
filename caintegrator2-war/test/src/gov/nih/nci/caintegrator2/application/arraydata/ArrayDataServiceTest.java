package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterSet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArrayDataServiceTest {
    
    private CaIntegrator2DaoStub daoStub;
    private ArrayDataService service;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("service-test-config.xml", ArrayDataServiceTest.class); 
        service = (ArrayDataService) context.getBean("arrayDataService"); 
        daoStub = (CaIntegrator2DaoStub) context.getBean("daoStub");
        daoStub.clear();                
    }

    @Test
    public void testLoadArrayDesign() throws PlatformLoadingException, AffymetrixCdfReadException {
        checkLoadArrayDesign(TestArrayDesignFiles.YEAST_2_CDF_FILE, TestArrayDesignFiles.YEAST_2_ANNOTATION_FILE);
    }

    private void checkLoadArrayDesign(File cdfFile, File annotationFile) throws PlatformLoadingException, AffymetrixCdfReadException {
        AffymetrixPlatformSource source = new AffymetrixPlatformSource(cdfFile, annotationFile);
        Platform platform = service.loadArrayDesign(source);
        assertTrue(daoStub.saveCalled);
        AffymetrixCdfReader cdfReader = AffymetrixCdfReader.create(TestArrayDesignFiles.YEAST_2_CDF_FILE);
        checkPlatform(platform, cdfReader);
        checkProbeSetReporters(platform, cdfReader);
        checkGeneReporters(platform);
    }

    private void checkGeneReporters(Platform platform) {
        ReporterSet geneReporters = getReporterSet(ReporterTypeEnum.GENE_EXPRESSION_GENE, platform);
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

    private void checkProbeSetReporters(Platform platform, AffymetrixCdfReader cdfReader) {
        ReporterSet probeSetReporters = getReporterSet(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, platform);
        assertEquals(cdfReader.getCdfData().getHeader().getNumProbeSets(), probeSetReporters.getReporters().size());
        for (AbstractReporter abstractReporter : probeSetReporters.getReporters()) {
            GeneExpressionReporter reporter = (GeneExpressionReporter) abstractReporter;
            assertFalse(StringUtils.isBlank(reporter.getName()));
            assertNotNull(reporter.getGene());
        }
    }

    private ReporterSet getReporterSet(ReporterTypeEnum type, Platform platform) {
        for (ReporterSet reporterSet : platform.getReporterSets()) {
            if (type.getValue().equals(reporterSet.getReporterType())) {
                return reporterSet;
            }
        }
        return null;
    }

    private void checkPlatform(Platform platform, AffymetrixCdfReader cdfReader) {
        assertNotNull(platform);
        assertEquals(cdfReader.getCdfData().getChipType(), platform.getName());
        assertEquals("Affymetrix", platform.getVendor());
        assertEquals(2, platform.getReporterSets().size());
    }

}
