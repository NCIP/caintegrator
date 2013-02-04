/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import org.junit.Test;

public class GenomicDataSourceConfigurationTest {

    @Test
    public void testGetSamples() {
        GenomicDataSourceConfiguration configuration = new GenomicDataSourceConfiguration();
        Sample sample1 = new Sample();
        sample1.setId(1L);
        Sample sample2 = new Sample();
        sample2.setId(2L);
        Sample sample3 = new Sample();
        sample3.setId(3L);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        Study study = new Study();
        studyConfiguration.setStudy(study);
        SampleSet controlSampleSet1 = new SampleSet();
        studyConfiguration.getGenomicDataSources().add(configuration);
        studyConfiguration.getGenomicDataSources().get(0).getControlSampleSetCollection().add(controlSampleSet1);
        controlSampleSet1.getSamples().add(sample3);
        configuration.setStudyConfiguration(studyConfiguration);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        study.getAssignmentCollection().add(assignment);
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        assignment.getSampleAcquisitionCollection().add(sampleAcquisition);
        sampleAcquisition.setSample(sample1);
        configuration.getSamples().add(sample1);
        configuration.getSamples().add(sample2);
        configuration.getSamples().add(sample3);
        assertEquals(3, configuration.getSamples().size());
        assertEquals(1, configuration.getUnmappedSamples().size());
        assertEquals(1, configuration.getMappedSamples().size());
        assertEquals(1, configuration.getControlSamples().size());
        assertTrue(configuration.getMappedSamples().contains(sample1));
        assertTrue(configuration.getUnmappedSamples().contains(sample2));
        assertTrue(configuration.getControlSamples().contains(sample3));
    }


}
