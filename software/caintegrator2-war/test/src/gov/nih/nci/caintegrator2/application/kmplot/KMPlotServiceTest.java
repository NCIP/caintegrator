/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import static org.junit.Assert.*;

import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class KMPlotServiceTest {

    private CaIntegratorKMPlotServiceStub caIntegratorPlotStub = new CaIntegratorKMPlotServiceStub();
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();
    
    @Before
    public void setUp() {
        caIntegratorPlotStub.clear();
        plotService.setCaIntegratorPlotService(caIntegratorPlotStub);
    }

    @Test
    public void testGeneratePlot() {
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        configuration.setTitle("title");
        configuration.setDurationLabel("duration");
        configuration.setProbabilityLabel("probability");
        SubjectGroup group1 = createGroup();
        configuration.getGroups().add(group1);
        SubjectGroup group2 = createGroup();
        configuration.getGroups().add(group2);
        KMPlot plot = plotService.generatePlot(configuration);
        KMCriteriaDTO criteriaDTO = caIntegratorPlotStub.kmCriteriaDTO;
        assertEquals("title", criteriaDTO.getPlotTitle());
        assertEquals("duration", criteriaDTO.getDurationAxisLabel());
        assertEquals("probability", criteriaDTO.getProbablityAxisLabel());
        assertEquals(2, criteriaDTO.getSampleGroupCriteriaDTOCollection().size());
        KMSampleGroupCriteriaDTO groupDTO = criteriaDTO.getSampleGroupCriteriaDTOCollection().iterator().next();
        assertEquals(Color.BLACK, groupDTO.getColor());
        assertEquals("group", groupDTO.getSampleGroupName());
        assertEquals(1, groupDTO.getKmSampleDTOCollection().size());
        KMSampleDTO sampleDTO = groupDTO.getKmSampleDTOCollection().iterator().next();
        assertEquals(1, sampleDTO.getSurvivalLength());
        assertEquals(true, sampleDTO.getCensor());
        assertEquals(1.1, (double) plot.getPValue(group1, group2), 0.0);
        assertTrue(caIntegratorPlotStub.computeLogRankPValueBetweenCalled);
    }

    private SubjectGroup createGroup() {
        SubjectGroup group = new SubjectGroup();
        group.setColor(Color.BLACK);
        group.setName("group");
        SubjectSurvivalData survivalData = new SubjectSurvivalData(1, false);
        group.getSurvivalData().add(survivalData);
        return group;
    }

}
