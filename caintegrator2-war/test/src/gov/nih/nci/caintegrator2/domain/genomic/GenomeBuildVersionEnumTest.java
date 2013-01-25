/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * 
 */
public class GenomeBuildVersionEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomeBuildVersionEnum.HG16, GenomeBuildVersionEnum.getByValue(GenomeBuildVersionEnum.HG16.getValue()));
        assertEquals(GenomeBuildVersionEnum.HG18, GenomeBuildVersionEnum.getByValue(GenomeBuildVersionEnum.HG18.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        GenomeBuildVersionEnum.checkType("not found");
    }
    
    @Test
    public void testMatchGenomeVersion() {
        assertEquals(GenomeBuildVersionEnum.HG16, GenomeBuildVersionEnum.matchGenomVersion("hg16"));
        assertEquals(GenomeBuildVersionEnum.HG16, GenomeBuildVersionEnum.matchGenomVersion("NCBI Build 34"));
        assertEquals(GenomeBuildVersionEnum.HG17, GenomeBuildVersionEnum.matchGenomVersion("hg17"));
        assertEquals(GenomeBuildVersionEnum.HG17, GenomeBuildVersionEnum.matchGenomVersion("NCBI Build 35"));
        assertEquals(GenomeBuildVersionEnum.HG18, GenomeBuildVersionEnum.matchGenomVersion("hg18"));
        assertEquals(GenomeBuildVersionEnum.HG18, GenomeBuildVersionEnum.matchGenomVersion("NCBI Build 36.1"));
        assertEquals(GenomeBuildVersionEnum.HG19, GenomeBuildVersionEnum.matchGenomVersion("hg19"));
    }

}
