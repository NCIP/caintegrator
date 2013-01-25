/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.web.ajax.DataElementSearchAjaxUpdater;

/**
 * Action used to edit the type and annotation of an imaging file column by a Study Manager.
 */
public class DefineImagingFieldDescriptorAction extends AbstractFieldDescriptorAction {

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
    public String getCancelAction() {
        return "cancelImagingFieldDescriptor";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntityTypeForSearch() {
        return DataElementSearchAjaxUpdater.ReturnTypeEnum.IMAGING_SOURCE.toString();
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
    public String getSaveFieldDescriptorTypeAction() {
        return "saveImagingFieldDescriptorType";
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
