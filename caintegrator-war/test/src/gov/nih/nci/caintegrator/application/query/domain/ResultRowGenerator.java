/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.ResultValue;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;


public final class ResultRowGenerator extends AbstractTestDataGenerator<ResultRow> {

    public static final ResultRowGenerator INSTANCE = new ResultRowGenerator();

    private ResultRowGenerator() {
        super();
    }

    @Override
    public void compareFields(ResultRow original, ResultRow retrieved) {
        assertEquals(original.getRowIndex(), retrieved.getRowIndex());
        assertEquals(original.getImageSeries().getId(),
                     retrieved.getImageSeries().getId());
        assertEquals(original.getSampleAcquisition().getId(),
                     retrieved.getSampleAcquisition().getId());
        assertEquals(original.getSubjectAssignment().getId(),
                     retrieved.getSubjectAssignment().getId());
        assertEquals(original.getValueCollection().size(), retrieved.getValueCollection().size());
    }


    @Override
    public ResultRow createPersistentObject() {
        return new ResultRow();
    }


    @Override
    public void setValues(ResultRow resultRow, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        resultRow.setRowIndex(getUniqueInt());
        resultRow.setValueCollection(new HashSet<ResultValue>());
        for (int i = 0; i < 3; i++) {
            resultRow.getValueCollection().add(ResultValueGenerator.INSTANCE.createPersistentObject());
        }
        resultRow.setImageSeries(new ImageSeries());
        resultRow.setSampleAcquisition(new SampleAcquisition());
        resultRow.setSubjectAssignment(new StudySubjectAssignment());
    }
}
