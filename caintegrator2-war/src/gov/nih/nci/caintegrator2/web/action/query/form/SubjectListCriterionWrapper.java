/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Wraps access to a <code>IdentifierCriterion</code>.
 */
public final class SubjectListCriterionWrapper extends AbstractCriterionWrapper implements OperatorHandler {
    
    /**
     * My Subject List label.
     */
    public static final String SUBJECT_LIST_FIELD_NAME = "*My Subject List";
    /**
     * Global Subject List label.
     */
    public static final String SUBJECT_GLOBAL_LIST_FIELD_NAME = "*Global Subject List";
    private final SubjectListCriterion criterion;
    private final SavedListCriterionRow row;
    private final StudySubscription studySubscription;
    private final String fieldName;

    SubjectListCriterionWrapper(SubjectListCriterion criterion, StudySubscription studySubscription, 
            String fieldName, SavedListCriterionRow row) {
        this.criterion = criterion;
        this.row = row;
        this.studySubscription = studySubscription;
        this.fieldName = fieldName;
        getParameters().add(createMultiSelectParameter());
    }
    
    private AbstractCriterionParameter createMultiSelectParameter() {
        ValuesSelectedHandler<SubjectList> handler = 
            new ValuesSelectedHandler<SubjectList>() {

                public void valuesSelected(List<SubjectList> values) {
                    criterion.getSubjectListCollection().clear();
                    criterion.getSubjectListCollection().addAll(values);
                }
            
        };
        MultiSelectParameter<SubjectList> parameter = 
            new MultiSelectParameter<SubjectList>(0, row.getRowIndex(), 
                    getOptions(), handler, criterion.getSubjectListCollection());
        parameter.setLabel("in list (select one or more Subject Lists)");
        parameter.setOperatorHandler(this);
        return parameter;
    }
    
    private OptionList<SubjectList> getOptions() {
        List<SubjectList> orderedValues = new ArrayList<SubjectList>();
        orderedValues.addAll(getSubjectLists());
        OptionList<SubjectList> options = new OptionList<SubjectList>();
        for (SubjectList value : orderedValues) {
            options.addOption(value.getName(), value);
        }
        return options;
    }

    private Collection<? extends SubjectList> getSubjectLists() {
        return SUBJECT_LIST_FIELD_NAME.equalsIgnoreCase(fieldName)
            ? studySubscription.getSubjectLists()
            : studySubscription.getStudy().getStudyConfiguration().getSubjectLists();
    }

    @Override
    String getFieldName() {
        return fieldName;
    }


    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.SUBJECT_LIST;
    }

    @Override
    AbstractCriterion getCriterion() {
        return criterion;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum[] getAvailableOperators() {
        return new CriterionOperatorEnum[0];
    }
    
    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum getOperator() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        // No-op, this should never change.
    }
}
