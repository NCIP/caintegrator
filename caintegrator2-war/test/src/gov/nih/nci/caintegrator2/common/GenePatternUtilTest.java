/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;


public class GenePatternUtilTest extends AbstractMockitoTest {
    private static Long counter = 0L;

    public void setUp() throws Exception {
        when(queryManagementService.execute(any(Query.class))).thenReturn(result(counter++, ++counter));
    }


    @Test
    public void testGenePatternUtil() throws InvalidCriterionException {
        Query query1 = new Query();
        query1.setName("query1");
        query1.setCompoundCriterion(new CompoundCriterion());
        Query query2 = new Query();
        query2.setName("query2");
        query2.setCompoundCriterion(new CompoundCriterion());
        List<Query> queries = new ArrayList<Query>();
        queries.add(query1);
        queries.add(query2);
        SampleClassificationParameterValue value =
            GenePatternUtil.createSampleClassification(queryManagementService, queries, new ArrayList<String>());
        assertNotNull(value);

        assertEquals("1", GenePatternUtil.createBoolean0or1Parameter("name", true).getValue());
        assertEquals("0", GenePatternUtil.createBoolean0or1Parameter("name", false).getValue());
    }

    private QueryResult result(Long id1, Long id2) {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());
        ResultRow row1 = new ResultRow();
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setId(id1);
        sampleAcquisition.setSample(sample);
        row1.setSampleAcquisition(sampleAcquisition);

        ResultRow row2 = new ResultRow();
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        Sample sample2 = new Sample();
        sample2.setId(id2);
        sampleAcquisition2.setSample(sample2);
        row2.setSampleAcquisition(sampleAcquisition2);

        queryResult.getRowCollection().add(row1);
        queryResult.getRowCollection().add(row2);
        return queryResult;
    }
}
