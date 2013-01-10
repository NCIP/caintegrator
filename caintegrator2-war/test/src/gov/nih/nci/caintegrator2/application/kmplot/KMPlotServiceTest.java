/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

public class KMPlotServiceTest extends AbstractMockitoTest {
    private KMPlotServiceCaIntegratorImpl plotService = new KMPlotServiceCaIntegratorImpl();

    @Before
    public void setUp() {
        plotService.setCaIntegratorPlotService(kmPlotService);
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

        assertEquals("title", plot.getConfiguration().getTitle());
        assertEquals("duration", plot.getConfiguration().getDurationLabel());
        assertEquals("probability", plot.getConfiguration().getProbabilityLabel());
        assertEquals(2, plot.getConfiguration().getGroups().size());

        verify(kmPlotService, atLeastOnce()).computeLogRankPValueBetween(any(KMSampleGroupCriteriaDTO.class), any(KMSampleGroupCriteriaDTO.class));
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
