/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import org.junit.Test;

public class StudySubscriptionTest {

    @Test
    public void testAll() {
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        subscription.getStudy().setStudyConfiguration(studyConfiguration);
        GeneList geneList1 = new GeneList();
        geneList1.setName("myGeneList");
        GeneList geneList2 = new GeneList();
        geneList2.setName("global-GeneList");
        subscription.getListCollection().add(geneList1);
        studyConfiguration.getListCollection().add(geneList2);
        
        assertEquals(2, subscription.getAllGeneListNames().size());
        assertEquals(geneList1.getName(), subscription.getSelectedGeneList("myGeneList").getName());
        assertEquals(geneList2.getName(), subscription.getSelectedGeneList("[Global]-global-GeneList").getName());
    }
}
