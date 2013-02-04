/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;

/**
 * 
 */
public class ResultColumnTestIntegration extends AbstractHibernateMappingTestIntegration<ResultColumn> {


    @Override
    protected AbstractTestDataGenerator<ResultColumn> getDataGenerator() {
        return ResultColumnGenerator.INSTANCE;
    }

}
