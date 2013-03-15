/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.data.AbstractHibernateMappingTestIntegration;

/**
 * Annotation file mapping integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class AnnotationFileMappingTestIntegration extends AbstractHibernateMappingTestIntegration<AnnotationFile> {

    @Override
    protected AbstractTestDataGenerator<AnnotationFile> getDataGenerator() {
        return AnnotationFileGenerator.INSTANCE;
    }

}
