/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SampleRefreshTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

public class GenomicDataSourceConfigurationTest {

    private GenomicDataSourceConfiguration configuration;
    private List<Sample> samples;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        samples = new ArrayList<Sample>();
        Sample sample1 = new Sample();
        sample1.setId(1L);
        sample1.setName("sample1");
        samples.add(sample1);
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("sample2");
        samples.add(sample2);
        Sample sample3 = new Sample();
        sample3.setId(3L);
        sample3.setName("sample3");
        samples.add(sample3);

        configuration = new GenomicDataSourceConfiguration();
        configuration.getSamples().addAll(samples);

        Sample extraSample = new Sample();
        extraSample.setId(4L);
        extraSample.setName("Extra Sample");
        SampleSet sampleSet = new SampleSet();
        sampleSet.getSamples().add(extraSample);
        configuration.getControlSampleSetCollection().add(sampleSet);

        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setDeploymentFinishDate(DateUtils.addDays(new Date(), -1));
        studyConfiguration.getGenomicDataSources().add(configuration);
        configuration.setStudyConfiguration(studyConfiguration);
        studyConfiguration.setStudy(new Study());
    }

    /**
     * Test's sample retrieval with all samples mapped.
     */
    @Test
    public void getSamplesAllMapped() {
        StudySubjectAssignment ssa = new StudySubjectAssignment();
        SampleAcquisition sa1 = new SampleAcquisition();
        sa1.setSample(samples.get(0));

        SampleAcquisition sa2 = new SampleAcquisition();
        sa2.setSample(samples.get(1));
        SampleAcquisition sa3 = new SampleAcquisition();
        sa3.setSample(samples.get(2));

        ssa.getSampleAcquisitionCollection().add(sa1);
        ssa.getSampleAcquisitionCollection().add(sa2);
        ssa.getSampleAcquisitionCollection().add(sa3);

        Study study = configuration.getStudyConfiguration().getStudy();
        study.getAssignmentCollection().add(ssa);

        Map<String, Date> refreshSampleNames = new HashMap<String, Date>();
        refreshSampleNames.put("sample1", new Date());
        refreshSampleNames.put("sample2", new Date());
        refreshSampleNames.put("sample3", null);
        configuration.setRefreshSampleNames(refreshSampleNames);

        assertEquals(3, configuration.getSamples().size());
        assertEquals(3, configuration.getMappedSamples().size());
        assertTrue(configuration.getMappedSamples().contains(samples.get(0)));
        assertTrue(configuration.getMappedSamples().contains(samples.get(1)));
        assertTrue(configuration.getMappedSamples().contains(samples.get(2)));
        for (Sample s : configuration.getMappedSamples()) {
            assertTrue(SampleRefreshTypeEnum.UNCHANGED == s.getRefreshType()
                    || SampleRefreshTypeEnum.UPDATE_ON_REFRESH == s.getRefreshType()
                    || SampleRefreshTypeEnum.ADD_ON_REFRESH == s.getRefreshType());
        }
        assertEquals(0, configuration.getUnmappedSamples().size());
        for (Sample s : configuration.getUnmappedSamples()) {
            assertEquals(SampleRefreshTypeEnum.ADD_ON_REFRESH, s.getRefreshType());
        }
    }

    /**
     * Test's sample retrieval with only some samples mapped.
     */
    @Test
    public void getSamplesSomeUnmapped() {
        StudySubjectAssignment ssa = new StudySubjectAssignment();
        SampleAcquisition sa1 = new SampleAcquisition();
        sa1.setSample(samples.get(0));

        ssa.getSampleAcquisitionCollection().add(sa1);

        Study study = configuration.getStudyConfiguration().getStudy();
        study.getAssignmentCollection().add(ssa);

        Map<String, Date> refreshSampleNames = new HashMap<String, Date>();
        refreshSampleNames.put("Unknown Sample", new Date());
        refreshSampleNames.put("Another Unknown Sample", null);
        configuration.setRefreshSampleNames(refreshSampleNames);

        assertEquals(3, configuration.getSamples().size());
        assertEquals(1, configuration.getMappedSamples().size());
        assertTrue(configuration.getMappedSamples().contains(samples.get(0)));
        assertFalse(configuration.getMappedSamples().contains(samples.get(1)));
        assertFalse(configuration.getMappedSamples().contains(samples.get(2)));
        for (Sample s : configuration.getMappedSamples()) {
            assertEquals(SampleRefreshTypeEnum.DELETE_ON_REFRESH, s.getRefreshType());
        }
        assertEquals(4, configuration.getUnmappedSamples().size());
        for (Sample s : configuration.getUnmappedSamples()) {
            assertTrue(SampleRefreshTypeEnum.DELETE_ON_REFRESH == s.getRefreshType()
                    || SampleRefreshTypeEnum.ADD_ON_REFRESH == s.getRefreshType());
        }
    }

    /**
     * Test's sample retrieval with no samples mapped.
     */
    @Test
    public void getSamplesNoneMapped() {
        Map<String, Date> refreshSampleNames = new HashMap<String, Date>();
        refreshSampleNames.put("Unknown Sample", new Date());
        refreshSampleNames.put("Another Unknown Sample", null);
        refreshSampleNames.put("sample4", null);
        configuration.setRefreshSampleNames(refreshSampleNames);

        Sample sample = new Sample();
        sample.setName("sample4");
        sample.setId(5L);
        configuration.getSamples().add(sample);

        assertEquals(4, configuration.getSamples().size());
        assertEquals(0, configuration.getMappedSamples().size());
        assertEquals(6, configuration.getUnmappedSamples().size());
        assertTrue(configuration.getUnmappedSamples().contains(samples.get(0)));
        assertTrue(configuration.getUnmappedSamples().contains(samples.get(1)));
        assertTrue(configuration.getUnmappedSamples().contains(samples.get(2)));

        for (Sample s : configuration.getUnmappedSamples()) {
            assertNotNull(s.getRefreshType());
        }
    }

    /**
     * Test's the retrieval of control samples.
     */
    @Test
    public void getControlSamples() {
        configuration.getControlSampleSetCollection().clear();
        assertTrue(configuration.getControlSamples().isEmpty());

        SampleSet sampleSet = new SampleSet();
        sampleSet.getSamples().addAll(samples);
        configuration.getControlSampleSetCollection().add(sampleSet);
        assertFalse(configuration.getControlSamples().isEmpty());
        assertEquals(3, configuration.getControlSamples().size());
        assertTrue(configuration.getControlSamples().contains(samples.get(0)));
        assertTrue(configuration.getControlSamples().contains(samples.get(1)));
        assertTrue(configuration.getControlSamples().contains(samples.get(2)));
    }
}
