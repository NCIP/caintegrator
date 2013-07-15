/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.kmplot;

import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of KMPlotter that directly uses original caIntegrator KMPlot code.
 */
@Service("kmPlotService")
public class KMPlotServiceCaIntegratorImpl implements KMPlotService {

    private gov.nih.nci.caintegrator.plots.services.KMPlotService caIntegratorPlotService;

    /**
     * {@inheritDoc}
     */
    public KMPlot generatePlot(KMPlotConfiguration configuration) {
        KMPlotImpl plot = new KMPlotImpl();
        plot.setConfiguration(configuration);
        Map<SubjectGroup, KMSampleGroupCriteriaDTO> groupToDTOMap =
            new HashMap<SubjectGroup, KMSampleGroupCriteriaDTO>();
        KMCriteriaDTO kmCriteriaDTO = createCriteriaDTO(configuration, groupToDTOMap);
        plot.setPlotChart((JFreeChart) caIntegratorPlotService.getChart(kmCriteriaDTO));
        setPValues(plot, groupToDTOMap);
        return plot;
    }

    private void setPValues(KMPlotImpl plot, Map<SubjectGroup, KMSampleGroupCriteriaDTO> groupToDTOMap) {
        for (SubjectGroup group1 : groupToDTOMap.keySet()) {
            for (SubjectGroup group2 : groupToDTOMap.keySet()) {
                if (!group1.equals(group2)) {
                    Double pValue = caIntegratorPlotService.computeLogRankPValueBetween(groupToDTOMap.get(group1),
                            groupToDTOMap.get(group2));
                    plot.setPValue(group1, group2, pValue);
                }
            }
        }
    }

    private KMCriteriaDTO createCriteriaDTO(KMPlotConfiguration configuration,
            Map<SubjectGroup, KMSampleGroupCriteriaDTO> groupToDTOMap) {
        KMCriteriaDTO criteriaDTO = new KMCriteriaDTO();
        criteriaDTO.setDurationAxisLabel(configuration.getDurationLabel());
        criteriaDTO.setProbablityAxisLabel(configuration.getProbabilityLabel());
        criteriaDTO.setPlotTitle(configuration.getTitle());
        criteriaDTO.setSampleGroupCriteriaDTOCollection(new HashSet<KMSampleGroupCriteriaDTO>());
        addSubjectGroups(configuration.getGroups(), groupToDTOMap, criteriaDTO, true);
        addSubjectGroups(configuration.getFilteredGroups(), groupToDTOMap, criteriaDTO, false);
        return criteriaDTO;
    }


    private void addSubjectGroups(List<SubjectGroup> subjectGroups,
                                  Map<SubjectGroup, KMSampleGroupCriteriaDTO> groupToDTOMap,
                                  KMCriteriaDTO criteriaDTO,
                                  boolean containsSubjects) {
        for (SubjectGroup group : subjectGroups) {
            KMSampleGroupCriteriaDTO sampleGroupCriteriaDTO = createSampleGroupCriteriaDTO(group);
            criteriaDTO.getSampleGroupCriteriaDTOCollection().add(sampleGroupCriteriaDTO);
            if (containsSubjects) {
                groupToDTOMap.put(group, sampleGroupCriteriaDTO);
            }
        }
    }

    private KMSampleGroupCriteriaDTO createSampleGroupCriteriaDTO(SubjectGroup group) {
        KMSampleGroupCriteriaDTO groupCriteriaDTO = new KMSampleGroupCriteriaDTO();
        groupCriteriaDTO.setColor(group.getColor());
        groupCriteriaDTO.setSampleGroupName(group.getName());
        groupCriteriaDTO.setKmSampleDTOCollection(new HashSet<KMSampleDTO>());
        for (SubjectSurvivalData survivalData : group.getSurvivalData()) {
            groupCriteriaDTO.getKmSampleDTOCollection().add(createSampleDTO(survivalData));
        }
        return groupCriteriaDTO;
    }

    private KMSampleDTO createSampleDTO(SubjectSurvivalData survivalData) {
        return new KMSampleDTO(survivalData.getSurvivalLength(), !survivalData.isCensor());
    }

    /**
     * @return the caIntegratorPlotService
     */
    public gov.nih.nci.caintegrator.plots.services.KMPlotService getCaIntegratorPlotService() {
        return caIntegratorPlotService;
    }

    /**
     * @param caIntegratorPlotService the caIntegratorPlotService to set
     */
    @Autowired
    public void setCaIntegratorPlotService(gov.nih.nci.caintegrator.plots.services.KMPlotService
            caIntegratorPlotService) {
        this.caIntegratorPlotService = caIntegratorPlotService;
    }

}
