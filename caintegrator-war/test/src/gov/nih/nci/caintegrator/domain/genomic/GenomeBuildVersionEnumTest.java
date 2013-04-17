/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 */
public class GenomeBuildVersionEnumTest {

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
