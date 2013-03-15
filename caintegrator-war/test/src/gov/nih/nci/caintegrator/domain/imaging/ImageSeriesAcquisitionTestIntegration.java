/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.imaging;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;

/**
 * Image series acquisition integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class ImageSeriesAcquisitionTestIntegration
    extends AbstractHibernateMappingTestIntegration<ImageSeriesAcquisition> {


    @Override
    protected AbstractTestDataGenerator<ImageSeriesAcquisition> getDataGenerator() {
        return ImageSeriesAcquisitionGenerator.INSTANCE;
    }

}
