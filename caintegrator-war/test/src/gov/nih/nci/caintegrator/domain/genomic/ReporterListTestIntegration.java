/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;

/**
 * 
 */
public class ReporterListTestIntegration extends AbstractHibernateMappingTestIntegration<ReporterList> {


    @Override
    protected AbstractTestDataGenerator<ReporterList> getDataGenerator() {
        return ReporterListGenerator.INSTANCE;
    }

}
