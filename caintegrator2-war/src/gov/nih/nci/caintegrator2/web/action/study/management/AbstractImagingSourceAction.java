/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;

/**
 * Base class for actions that require retrieval of persistent <code>ImageAnnotationConfigurations</code>.
 */
public abstract class AbstractImagingSourceAction extends AbstractStudyAction {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;

    private ImageDataSourceConfiguration imageSourceConfiguration = new ImageDataSourceConfiguration();
    private boolean cancelAction = false;
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (getImageSourceConfiguration().getId() != null) {
            setImageSourceConfiguration(
                    getStudyManagementService().getRefreshedEntity(getImageSourceConfiguration()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!cancelAction) {
            fixUrlFromInternetExplorer();
            prepareValueStack();
        }
    }

    boolean checkErrors() {
        if (!getFieldErrors().isEmpty() || !getActionErrors().isEmpty()) {
            return false;
        }
        return true;
    }
    
    /**
     * This is because the editable-select for internet explorer submits an extra URL after comma.
     * ex: "http://url, http://url" instead of just "http://url".
     */
    abstract void fixUrlFromInternetExplorer();

    /**
     * @return the imageSource
     */
    public ImageDataSourceConfiguration getImageSourceConfiguration() {
        return imageSourceConfiguration;
    }

    /**
     * @param imageSourceConfiguration the imageSource to set
     */
    public void setImageSourceConfiguration(ImageDataSourceConfiguration imageSourceConfiguration) {
        this.imageSourceConfiguration = imageSourceConfiguration;
    }
    
    /**
     * @return the cancelAction
     */
    public boolean isCancelAction() {
        return cancelAction;
    }

    /**
     * @param cancelAction the cancelAction to set
     */
    public void setCancelAction(boolean cancelAction) {
        this.cancelAction = cancelAction;
    }
}
