/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;

public class NumericAnnotationValueTestIntegration extends AbstractHibernateMappingTestIntegration<NumericAnnotationValue>  {

    @Override
    protected AbstractTestDataGenerator<NumericAnnotationValue> getDataGenerator() {
        return NumericAnnotationValueGenerator.INSTANCE;
    }

}
