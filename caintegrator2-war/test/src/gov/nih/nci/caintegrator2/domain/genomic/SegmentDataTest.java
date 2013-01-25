/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class SegmentDataTest {

    @Test
    public void testCompareTo() {
        SegmentData segmentData1 = new SegmentData();
        SegmentData segmentData2 = createSegmentData("1", 1);
        SegmentData segmentData3 = createSegmentData("1", 2);
        SegmentData segmentData4 = createSegmentData("2", 1);
        SegmentData segmentData5 = createSegmentData("X", 1);
        SegmentData segmentData6 = createSegmentData("X", 2);
        assertTrue(segmentData1.compareTo(segmentData2) > 0);
        assertTrue(segmentData2.compareTo(segmentData1) < 0);
        assertTrue(segmentData1.compareTo(segmentData1) == 0);
        assertTrue(segmentData2.compareTo(segmentData2) == 0);
        assertTrue(segmentData2.compareTo(segmentData3) < 0);
        assertTrue(segmentData3.compareTo(segmentData4) < 0);
        assertTrue(segmentData4.compareTo(segmentData5) < 0);
        assertTrue(segmentData5.compareTo(segmentData6) < 0);
        ArrayData arrayData = new ArrayData();
        arrayData.getSegmentDatas().add(segmentData2);
        arrayData.getSegmentDatas().add(segmentData4);
        arrayData.getSegmentDatas().add(segmentData6);
        arrayData.getSegmentDatas().add(segmentData5);
        arrayData.getSegmentDatas().add(segmentData3);
        arrayData.getSegmentDatas().add(segmentData1);
        Iterator<SegmentData> iterator = arrayData.getSegmentDatas().iterator();
        assertEquals(segmentData2, iterator.next());
        assertEquals(segmentData3, iterator.next());
        assertEquals(segmentData4, iterator.next());
        assertEquals(segmentData5, iterator.next());
        assertEquals(segmentData6, iterator.next());
        assertEquals(segmentData1, iterator.next());
    }

    private SegmentData createSegmentData(String chromosome, int startPosition) {
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.getLocation().setChromosome(chromosome);
        segmentData.getLocation().setStartPosition(startPosition);
        return segmentData;
    }

}
