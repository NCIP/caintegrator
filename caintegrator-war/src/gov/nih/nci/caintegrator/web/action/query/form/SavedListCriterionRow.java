/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectListCriterion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class SavedListCriterionRow extends AbstractCriterionRow {

    private AbstractCriterionWrapper subjectListCriterionWrapper;


    /**
     * @param group
     */
    SavedListCriterionRow(CriteriaGroup group) {
        super(group);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        List<String> names = new ArrayList<String>();
        names.add(SubjectListCriterionWrapper.SUBJECT_LIST_FIELD_NAME);
        names.add(SubjectListCriterionWrapper.SUBJECT_GLOBAL_LIST_FIELD_NAME);
        return names;
    }

    SubjectListCriterionWrapper createSubjectListWrapper(String fieldName) {
        if (SubjectListCriterionWrapper.SUBJECT_LIST_FIELD_NAME.equals(fieldName)
                || SubjectListCriterionWrapper.SUBJECT_GLOBAL_LIST_FIELD_NAME.equals(fieldName)) {
            return new SubjectListCriterionWrapper(new SubjectListCriterion(), getSubscription(), fieldName, this);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getSubjectListCriterionWrapper();
    }

    private AbstractCriterionWrapper getSubjectListCriterionWrapper() {
        return subjectListCriterionWrapper;
    }

    private void setIdentifierCriterionWrapper(AbstractCriterionWrapper criterionWrapper) {
        if (this.subjectListCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.subjectListCriterionWrapper = criterionWrapper;
        if (subjectListCriterionWrapper != null) {
            addCriterionToQuery();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName() {
        if (getCriterionWrapper() == null) {
            return "";
        } else {
            return getCriterionWrapper().getFieldName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return CriterionRowTypeEnum.SAVED_LIST.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            setIdentifierCriterionWrapper(null);
        }
        setIdentifierCriterionWrapper(createSubjectListWrapper(fieldName));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.subjectListCriterionWrapper = CriterionWrapperBuilder.createSavedListCriterionWrapper(criterion,
                this, getSubscription(), getFieldName());
    }

    private StudySubscription getSubscription() {
        return getGroup().getSubscription();
    }

}
