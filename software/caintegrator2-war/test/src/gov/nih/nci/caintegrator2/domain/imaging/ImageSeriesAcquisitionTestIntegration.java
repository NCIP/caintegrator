/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.imaging;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.data.AbstractHibernateMappingTestIntegration;

public class ImageSeriesAcquisitionTestIntegration extends AbstractHibernateMappingTestIntegration<ImageSeriesAcquisition> {


    @Override
    protected AbstractTestDataGenerator<ImageSeriesAcquisition> getDataGenerator() {
        return ImageSeriesAcquisitionGenerator.INSTANCE;
    }

}
