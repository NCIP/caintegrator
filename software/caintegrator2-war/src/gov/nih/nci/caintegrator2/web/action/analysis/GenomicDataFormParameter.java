/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.GenomicDataParameterValue;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
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
    
    private Query selectedQuery;

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
        if (selectedQuery == null) {
            return ALL_DATA;
        } else {
            return selectedQuery.getName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        setSelectedQuery(getForm().getGenomicQuery(value));
    }

    /**
     * @return the available choices
     */
    public Collection<String> getChoices() {
        List<String> choices = new ArrayList<String>();
        choices.add(ALL_DATA);
        choices.addAll(getForm().getGenomicQueryNames());
        return choices;
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
            setSelectedQuery(Cai2Util.createAllGenomicDataQuery(studySubscription, null));
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
        }
    }

    private void refreshSelectedQuery(StudySubscription studySubscription) {
        for (Query nextQuery : studySubscription.getQueryCollection()) {
            if (nextQuery.equals(getSelectedQuery())) {
                setSelectedQuery(nextQuery);
            }
        }
    }

}
