/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

/**
 * Action used to edit the type and annotation of a file column by a Study Manager.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See selectDataElement()
public class DefineFileColumnAction extends AbstractFileColumnAction {
    
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityTypeEnum getEntityType() {
        return EntityTypeEnum.SUBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCancelAction() {
        return "cancelFileColumn";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityTypeForSearch() {
        return "subject";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewDefinitionAction() {
        return "createNewDefinition";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSaveAnnotationDefinitionAction() {
        return "updateAnnotationDefinition";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSaveColumnTypeAction() {
        return "saveColumnType";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectDataElementAction() {
        return "selectDataElement";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectDefinitionAction() {
        return "selectDefinition";
    }
}
