/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.application.query.domain.QueryGenerator;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.data.AbstractHibernateMappingTestIntegration;

public class QueryMappingTestIntegration extends AbstractHibernateMappingTestIntegration<Query>  {

    @Override
    protected AbstractTestDataGenerator<Query> getDataGenerator() {
        return QueryGenerator.INSTANCE;
    }

}
