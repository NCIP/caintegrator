/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;

public class DateAnnotationValueTestIntegration extends AbstractHibernateMappingTestIntegration<DateAnnotationValue>  {

    @Override
    protected AbstractTestDataGenerator<DateAnnotationValue> getDataGenerator() {
        return DateAnnotationValueGenerator.INSTANCE;
    }

}
