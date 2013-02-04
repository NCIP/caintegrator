/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

/**
 * Action used to edit the type and annotation of an imaging file column by a Study Manager.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See selectDataElement()
public class DefineImagingFileColumnAction extends AbstractFileColumnAction {

    private static final long serialVersionUID = 1L;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateDataSourceStatus() {
        getStudyManagementService().updateImageDataSourceStatus(getStudyConfiguration()); // If it changes state
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public EntityTypeEnum getEntityType() {
        return EntityTypeEnum.IMAGESERIES;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCancelAction() {
        return "cancelImagingFileColumn";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityTypeForSearch() {
        return "image";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewDefinitionAction() {
        return "createNewImagingDefinition";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSaveAnnotationDefinitionAction() {
        return "updateImagingAnnotationDefinition";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSaveColumnTypeAction() {
        return "saveImagingColumnType";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectDataElementAction() {
        return "selectImagingDataElement";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectDefinitionAction() {
        return "selectImagingDefinition";
    }

}
