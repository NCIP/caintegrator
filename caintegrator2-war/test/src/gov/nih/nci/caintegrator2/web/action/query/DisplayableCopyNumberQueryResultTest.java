/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import static org.junit.Assert.*;

import org.junit.Test;

import gov.nih.nci.caintegrator2.application.query.GenomicCriteriaMatchTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.application.SegmentDataResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;


public class DisplayableCopyNumberQueryResultTest {
    
    @Test
    public void testAll() {
        GenomicDataQueryResult genomicDataQueryResult = createGenomicDataQueryResult();
        DisplayableCopyNumberQueryResult result = new DisplayableCopyNumberQueryResult(
                genomicDataQueryResult, ResultsOrientationEnum.SUBJECTS_AS_COLUMNS);
        assertEquals(2, result.getSampleHeaders().size());
        assertEquals(0, result.getSampleRows().size());
        assertEquals("Identifier/Sample2", result.getSampleHeaders().get(1));
        assertEquals("0.123", result.getRows().get(0).getValues().get(1).getDisplayableValue());
        assertTrue(result.getRows().get(0).getValues().get(1).isMeetsCriterion());
        assertEquals(null, result.getRows().get(0).getValues().get(0));
        
        result = new DisplayableCopyNumberQueryResult(
                genomicDataQueryResult, ResultsOrientationEnum.SUBJECTS_AS_ROWS);
        assertEquals(0, result.getSampleHeaders().size());
        assertEquals(0, result.getRows().size());
        assertEquals(1, result.getSampleRows().size());
        assertEquals("0.123", result.getSampleRows().get(0).getValue().getDisplayableValue());
        assertTrue(result.getSampleRows().get(0).getValue().isMeetsCriterion());
    }
    
    private GenomicDataQueryResult createGenomicDataQueryResult() {
        GenomicDataQueryResult genomicDataQueryResult = new GenomicDataQueryResult();
        
        // Add columns
        GenomicDataResultColumn column1 = new GenomicDataResultColumn();
        column1.setSampleAcquisition(createSample("Sample1"));
        GenomicDataResultColumn column2 = new GenomicDataResultColumn();
        column2.setSampleAcquisition(createSample("Sample2"));
        genomicDataQueryResult.getColumnCollection().add(column1);
        genomicDataQueryResult.getColumnCollection().add(column2);
        
        // Add rows
        GenomicDataResultRow row1 = new GenomicDataResultRow();
        row1.setSegmentDataResultValue(createSegmentDataResultValue("1", 1234, 5678));
        GenomicDataResultRow row2 = new GenomicDataResultRow();
        row2.setSegmentDataResultValue(createSegmentDataResultValue("1", 123, 789));
        row1.getValues().add(createGenomicDataResultValue(column2));
        genomicDataQueryResult.getRowCollection().add(row1);
        genomicDataQueryResult.getRowCollection().add(row2);
        
        return genomicDataQueryResult;
    }

    private GenomicDataResultValue createGenomicDataResultValue(GenomicDataResultColumn column) {
        GenomicDataResultValue genomicDataResultValue = new GenomicDataResultValue();
        genomicDataResultValue.setColumn(column);
        genomicDataResultValue.setValue(0.123F);
        genomicDataResultValue.setCriteriaMatchType(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE);
        return genomicDataResultValue;
    }

    private SegmentDataResultValue createSegmentDataResultValue(String chromosome, int startPos, int endPos) {
        SegmentDataResultValue segmentDataResultValue = new SegmentDataResultValue();
        ChromosomalLocation chromosomalLocation = new ChromosomalLocation();
        chromosomalLocation.setChromosome(chromosome);
        chromosomalLocation.setStartPosition(startPos);
        chromosomalLocation.setEndPosition(endPos);
        segmentDataResultValue.setChromosomalLocation(chromosomalLocation);
        return segmentDataResultValue;
    }

    private SampleAcquisition createSample(String sampleName) {
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setName(sampleName);
        sampleAcquisition.setSample(sample);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        sampleAcquisition.setAssignment(assignment);
        assignment.setIdentifier("Identifier");
        return sampleAcquisition;
    }

}
