/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web;


import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator2.web.action.analysis.DisplayableQuery;

import java.util.List;

import org.junit.Test;

/**
 *
 */
public class Cai2WebUtilTest extends AbstractMockitoTest {

    @Test
    public void testRetrieveDisplayableQueries() {
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studySubscription.getStudy().setStudyConfiguration(studyConfiguration);
        Query query1 = new Query();
        query1.setName("query1");
        Query query2 = new Query();
        query2.setName("query2");
        Query query3 = new Query();
        query3.setName("query3");
        query3.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        SubjectList subjectList1 = new SubjectList();
        subjectList1.setName("subjectList1");
        subjectList1.setVisibility(Visibility.PRIVATE);
        SubjectList subjectList2 = new SubjectList();
        subjectList2.setName("subjectList2");
        subjectList2.setVisibility(Visibility.GLOBAL);
        studySubscription.getListCollection().add(subjectList1);
        studySubscription.getStudy().getStudyConfiguration().getListCollection().add(subjectList2);
        studySubscription.getQueryCollection().add(query1);
        studySubscription.getQueryCollection().add(query2);
        studySubscription.getQueryCollection().add(query3);
        List<DisplayableQuery> displayableQueries =
            Cai2WebUtil.retrieveDisplayableQueries(studySubscription, queryManagementService, false);
        assertEquals("[Q]-query1", displayableQueries.get(0).getDisplayName());
        assertEquals("[Q]-query2", displayableQueries.get(1).getDisplayName());
        assertEquals("[SL-G]-subjectList2", displayableQueries.get(2).getDisplayName());
        assertEquals("[SL]-subjectList1", displayableQueries.get(3).getDisplayName());
        displayableQueries =
            Cai2WebUtil.retrieveDisplayableQueries(studySubscription, queryManagementService, true);
        assertEquals("[Q]-query1", displayableQueries.get(0).getDisplayName());
        assertEquals("[Q]-query2", displayableQueries.get(1).getDisplayName());
        assertEquals("[Q]-query3", displayableQueries.get(2).getDisplayName());
        assertEquals("[SL-G]-subjectList2", displayableQueries.get(3).getDisplayName());
        assertEquals("[SL]-subjectList1", displayableQueries.get(4).getDisplayName());

    }
}
