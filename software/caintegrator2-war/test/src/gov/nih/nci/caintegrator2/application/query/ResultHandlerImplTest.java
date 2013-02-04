/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class ResultHandlerImplTest {


    @Test
    public void testCreateResults() {
        ResultHandler resultHandler = new ResultHandlerImpl();
        Query query = new Query();
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        ResultRow row = new ResultRow();
        row.setSampleAcquisition(new SampleAcquisition());
        row.setImageSeries(new ImageSeries());
        row.setSubjectAssignment(new StudySubjectAssignment());
        row.setValueCollection(new ArrayList<ResultValue>());
        resultRows.add(row);
        
        Collection <ResultColumn> columnCollection = new HashSet<ResultColumn>();
        ResultColumn col1 = new ResultColumn();
        col1.setEntityType(EntityTypeEnum.IMAGESERIES);
        columnCollection.add(col1);
        col1.setColumnIndex(0);
        ResultColumn col2 = new ResultColumn();
        col2.setEntityType(EntityTypeEnum.SAMPLE);
        columnCollection.add(col2);
        col2.setColumnIndex(1);
        ResultColumn col3 = new ResultColumn();
        col3.setEntityType(EntityTypeEnum.SUBJECT);
        columnCollection.add(col3);
        col3.setColumnIndex(2);
        query.setColumnCollection(columnCollection);
        assertNotNull(resultHandler.createResults(query, resultRows));
    }

}
