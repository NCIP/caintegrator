/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;

/**
 * 
 */
public class ResultColumnTestIntegration extends AbstractHibernateMappingTestIntegration<ResultColumn> {


    @Override
    protected AbstractTestDataGenerator<ResultColumn> getDataGenerator() {
        return ResultColumnGenerator.INSTANCE;
    }

}
