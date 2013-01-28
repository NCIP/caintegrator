/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.WildCardTypeEnum;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class IdentifierCriterionRow extends AbstractCriterionRow {

    private AbstractCriterionWrapper identifierCriterionWrapper;


    /**
     * @param group
     */
    IdentifierCriterionRow(CriteriaGroup group) {
        super(group);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        List<String> names = new ArrayList<String>();
        names.add(IdentifierCriterionWrapper.SUBJECT_IDENTIFIER_FIELD_NAME);
        if (getGroup().getSubscription().getStudy().hasImageDataSources()) {
            names.add(IdentifierCriterionWrapper.IMAGE_SERIES_IDENTIFIER_FIELD_NAME);
        }
        return names;
    }

    IdentifierCriterionWrapper createIdentifierCriterionWrapper(String fieldName) {
        IdentifierCriterion criterion = new IdentifierCriterion();
        criterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        criterion.setEntityType(EntityTypeEnum.SUBJECT);
        if (IdentifierCriterionWrapper.IMAGE_SERIES_IDENTIFIER_FIELD_NAME.equals(fieldName)) {
            criterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        }
        return new IdentifierCriterionWrapper(criterion, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return getIdentifierCriterionWrapper();
    }

    private AbstractCriterionWrapper getIdentifierCriterionWrapper() {
        return identifierCriterionWrapper;
    }

    private void setIdentifierCriterionWrapper(AbstractCriterionWrapper identifierCriterionWrapper) {
        if (this.identifierCriterionWrapper != null) {
            removeCriterionFromQuery();
        }
        this.identifierCriterionWrapper = identifierCriterionWrapper;
        if (identifierCriterionWrapper != null) {
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
        return CriterionRowTypeEnum.UNIQUE_IDENTIIFER.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleFieldNameChange(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            setIdentifierCriterionWrapper(null);
        }
        setIdentifierCriterionWrapper(createIdentifierCriterionWrapper(fieldName));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setCriterion(AbstractCriterion criterion) {
        this.identifierCriterionWrapper = CriterionWrapperBuilder.createIdentifierCriterionWrapper(criterion,
                this);
    }
}
