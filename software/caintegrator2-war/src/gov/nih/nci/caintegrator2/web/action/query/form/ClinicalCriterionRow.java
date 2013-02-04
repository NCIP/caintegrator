/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

/**
 * Holds information for a single clinical criterion.
 */
public class ClinicalCriterionRow extends AbstractAnnotationCriterionRow {

    ClinicalCriterionRow(CriteriaGroup group) {
        super(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AnnotationDefinitionList getAnnotationDefinitionList() {
        return getGroup().getForm().getClinicalAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    EntityTypeEnum getEntityType() {
        return EntityTypeEnum.SUBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return CriterionRowTypeEnum.CLINICAL.getValue();
    }

}
