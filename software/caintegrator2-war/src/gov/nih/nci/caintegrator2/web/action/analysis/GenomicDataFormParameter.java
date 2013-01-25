/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Parameter that selects the source of genomic data.
 */
public class GenomicDataFormParameter extends AbstractAnalysisFormParameter {

    /**
     * String value used to indicate that all genomic data should be used.
     */
    public static final String ALL_DATA = "All Genomic Data";
    
    /**
     * String value used to indicated that all genomic data for a specified platform should be used.
     */
    public static final String ALL_DATA_SPECIFY_PLATFORM = "All Genomic Data for Platform: ";
    
    private Query selectedQuery;
    private String selectedPlatform = "";
    private String selectedOptionValue = "";

    GenomicDataFormParameter(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        super(form, parameterValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayType() {
        return "select";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return selectedOptionValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        selectedPlatform = "";
        selectedOptionValue = value;
        setSelectedQuery(getForm().getGenomicQuery(value));
        if (getSelectedQuery() == null 
             && value.contains(ALL_DATA_SPECIFY_PLATFORM)) {
            selectedPlatform = value.replace(ALL_DATA_SPECIFY_PLATFORM, "");
        }
    }

    /**
     * @return the available choices
     */
    public Collection<String> getChoices() {
        List<String> choices = new ArrayList<String>();
        addAllDataToChoices(choices);
        choices.addAll(getForm().getGenomicQueryNames());
        return choices;
    }

    private void addAllDataToChoices(List<String> choices) {
        if (getForm().isMultiplePlatformsInStudy()) {
            for (String platformName : getForm().getPlatformNames()) {
                choices.add(ALL_DATA_SPECIFY_PLATFORM + platformName);
            }
        } else {
            choices.add(ALL_DATA);
        }
    }

    Query getSelectedQuery() {
        return selectedQuery;
    }

    private void setSelectedQuery(Query selectedQuery) {
        this.selectedQuery = selectedQuery;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void configureForInvocation(StudySubscription studySubscription, 
                                       QueryManagementService queryManagementService) throws InvalidCriterionException {
        if (getSelectedQuery() == null) {
            setSelectedQuery(QueryUtil.createAllGeneExpressionDataQuery(studySubscription, null, selectedPlatform));
        } else {
            refreshSelectedQuery(studySubscription);
        }
        GenomicDataQueryResult genomicData = queryManagementService.executeGenomicDataQuery(getSelectedQuery());
        initializeReporterGenes(genomicData);
        ((GenomicDataParameterValue) getParameterValue()).setGenomicData(genomicData);
    }

    private void initializeReporterGenes(GenomicDataQueryResult genomicData) {
        for (GenomicDataResultRow row : genomicData.getRowCollection()) {
            HibernateUtil.loadCollection(row.getReporter().getGenes());
            HibernateUtil.loadCollection(row.getReporter().getSamplesHighVariance());
        }
    }

    private void refreshSelectedQuery(StudySubscription studySubscription) {
        for (Query nextQuery : studySubscription.getQueryCollection()) {
            if (nextQuery.equals(getSelectedQuery())) {
                setSelectedQuery(nextQuery);
            }
        }
    }

    /**
     * @return the selectedPlatform
     */
    public String getSelectedPlatform() {
        return selectedPlatform;
    }

}
