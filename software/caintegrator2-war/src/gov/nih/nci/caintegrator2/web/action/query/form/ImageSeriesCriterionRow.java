/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;


/**
 * Holds information for a single clinical criterion.
 */
public class ImageSeriesCriterionRow extends AbstractAnnotationCriterionRow {

    ImageSeriesCriterionRow(CriteriaGroup group) {
        super(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AnnotationDefinitionList getAnnotationDefinitionList() {
        return getGroup().getForm().getImageSeriesAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    EntityTypeEnum getEntityType() {
        return EntityTypeEnum.IMAGESERIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return CriterionRowTypeEnum.IMAGE_SERIES.getValue();
    }

}
