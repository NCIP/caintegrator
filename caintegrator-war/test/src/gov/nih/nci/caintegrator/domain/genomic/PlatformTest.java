/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * 
 */
public class PlatformTest {

    @Test
    public void testGetReporterListListing() {
        Platform platform = new Platform();
        platform.addReporterList("Reporter list1", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertEquals("Reporter list1", platform.getReporterListListing());
        platform.addReporterList("Reporter list2", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        assertTrue(platform.getReporterListListing().contains("Reporter list1"));
        assertTrue(platform.getReporterListListing().contains("Reporter list2"));
    }

    @Test
    public void testGetDisplayableArrayNames() {
        Platform platform = new Platform();
        platform.setName("Platform Name");
        platform.setVendor(PlatformVendorEnum.AFFYMETRIX);
        platform.addReporterList("list1", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        platform.addReporterList("list1", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        platform.addReporterList("list2", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        String arrayNames = platform.getDisplayableArrayNames();
        List<String> arrayNamesList = Arrays.asList(arrayNames.split(","));
        arrayNamesList.contains("list1");
        arrayNamesList.contains("list2");
        assertEquals(2, arrayNamesList.size());
    }
}
