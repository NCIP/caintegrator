/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;

/**
 * 
 */
public class SelecedValueCriterionTestIntegration extends AbstractHibernateMappingTestIntegration<SelectedValueCriterion> {


    @Override
    protected AbstractTestDataGenerator<SelectedValueCriterion> getDataGenerator() {
        return SelectedValueCriterionGenerator.INSTANCE;
    }

}