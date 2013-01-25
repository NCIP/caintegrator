/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.data.AbstractHibernateMappingTestIntegration;

public class StudyConfigurationMappingTestIntegration extends AbstractHibernateMappingTestIntegration<StudyConfiguration>  {

    @Override
    protected AbstractTestDataGenerator<StudyConfiguration> getDataGenerator() {
        return StudyConfigurationGenerator.INSTANCE;
    }

}
