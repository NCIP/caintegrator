/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.translational;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.Set;

public final class StudyTestDataGenerator extends AbstractTestDataGenerator<Study> {
    
    public static final StudyTestDataGenerator INSTANCE = new StudyTestDataGenerator();

    private StudyTestDataGenerator() {
        super();
    }
    
    @Override
    public void compareFields(Study original, Study retrieved) {
        assertEquals(original.getLongTitleText(), retrieved.getLongTitleText());
        assertEquals(original.getShortTitleText(), retrieved.getShortTitleText());
    }

    @Override
    public Study createPersistentObject() {
        return new Study();
    }

    @Override
    public void setValues(Study study, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        study.setShortTitleText(getUniqueString());
        study.setLongTitleText(getUniqueString());
    }

}
