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

    private ImageDataSourceConfiguration imageSourceConfiguration = new ImageDataSourceConfiguration();
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (getImageSourceConfiguration().getId() != null) {
            setImageSourceConfiguration(
                    getStudyManagementService().getRefreshedStudyEntity(getImageSourceConfiguration()));
        }
    }


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
}
