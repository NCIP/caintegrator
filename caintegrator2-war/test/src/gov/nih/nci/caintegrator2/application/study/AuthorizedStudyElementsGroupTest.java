/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.security.authorization.domainobjects.Group;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AuthorizedStudyElementsGroupTest {

    @Test
    public void testGetAuthorizedStudyElementsGroup() {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        String groupName = "Group 1";
        String groupDescription = "This is a test group for unit test";
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        Long studyConfigId = 12345L;
        studyConfiguration.setId(studyConfigId);

        Group group = new Group();
        group.setGroupName(groupName);
        group.setGroupDesc(groupDescription);

        authorizedStudyElementsGroup.setAuthorizedGroup(group);
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfiguration);

        assertEquals(groupName,authorizedStudyElementsGroup.getAuthorizedGroup().getGroupName());
        assertEquals(groupDescription,authorizedStudyElementsGroup.getAuthorizedGroup().getGroupDesc());
        assertEquals(studyConfigId,authorizedStudyElementsGroup.getStudyConfiguration().getId());

    }

    @Test
    public void testAddAuthorizedStudyElementsGroups() {
        //create StudyConfiguration
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        Study study = new Study();
        String studyName = "Test Study Name for Authorized Elements Group Testing";
        study.setShortTitleText(studyName);
        studyConfiguration.setStudy(study);

        //create first AuthorizedStudyElementsGroup
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup1 = new AuthorizedStudyElementsGroup();
        String group1Name = "Group 1";
        String group1Description = "This is test group 1 for unit test";
        Group group = new Group();
        group.setGroupName(group1Name);
        group.setGroupDesc(group1Description);
        authorizedStudyElementsGroup1.setAuthorizedGroup(group);
        // create authorizedannotationfieldescriptor
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor1 = new AuthorizedAnnotationFieldDescriptor();
        AnnotationFieldDescriptor annotationFieldDescriptor1 = new AnnotationFieldDescriptor();
        String afd1Name = "AFD Number 1";
        annotationFieldDescriptor1.setName(afd1Name);
        authorizedAnnotationFieldDescriptor1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedAnnotationFieldDescriptor1.setAnnotationFieldDescriptor(annotationFieldDescriptor1);
        List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptorList1 =
            new ArrayList<AuthorizedAnnotationFieldDescriptor>();
        authorizedAnnotationFieldDescriptorList1.add(authorizedAnnotationFieldDescriptor1);
        authorizedStudyElementsGroup1.setAuthorizedAnnotationFieldDescriptors(authorizedAnnotationFieldDescriptorList1);
        authorizedStudyElementsGroup1.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor1);
        // create first authorizedgenomicdatasource
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration1 =
                                                    new AuthorizedGenomicDataSourceConfiguration();
        GenomicDataSourceConfiguration genomicDataSourceConfiguration1 = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration1.setStudyConfiguration(studyConfiguration);
        authorizedGenomicDataSourceConfiguration1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedGenomicDataSourceConfiguration1.setGenomicDataSourceConfiguration(genomicDataSourceConfiguration1);
        List<AuthorizedGenomicDataSourceConfiguration> authorizedGenomicDataSourceConfigurationsList1 =
            new ArrayList<AuthorizedGenomicDataSourceConfiguration>();
        authorizedStudyElementsGroup1.setAuthorizedGenomicDataSourceConfigurations(authorizedGenomicDataSourceConfigurationsList1);
        authorizedStudyElementsGroup1.getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration1);
        // create first authorizedQuery
        Query query1 = new Query();
        String queryName = "Query Number 1";
        query1.setName(queryName);
        AuthorizedQuery authorizedQuery1 = new AuthorizedQuery();
        authorizedQuery1.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup1);
        authorizedQuery1.setQuery(query1);
        List<AuthorizedQuery> authorizedQueryList1 =
            new ArrayList<AuthorizedQuery>();
        authorizedQueryList1.add(authorizedQuery1);
        authorizedStudyElementsGroup1.setAuthorizedQuerys(authorizedQueryList1);
        authorizedStudyElementsGroup1.getAuthorizedQuerys().add(authorizedQuery1);


        // create second AuthorizedStudyElementsGroup
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup2 = new AuthorizedStudyElementsGroup();
        String group2Name = "Group 2";
        String group2Description = "This is test group 2 for unit test";
        group = new Group();
        group.setGroupName(group2Name);
        group.setGroupDesc(group2Description);
        authorizedStudyElementsGroup2.setAuthorizedGroup(group);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor2 = new AuthorizedAnnotationFieldDescriptor();
        AnnotationFieldDescriptor annotationFieldDescriptor2 = new AnnotationFieldDescriptor();
        String afd2Name = "AFD Number 2";
        annotationFieldDescriptor2.setName(afd2Name);
        authorizedAnnotationFieldDescriptor2.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup2);
        authorizedAnnotationFieldDescriptor2.setAnnotationFieldDescriptor(annotationFieldDescriptor2);
        List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptorList2 =
            new ArrayList<AuthorizedAnnotationFieldDescriptor>();
        authorizedAnnotationFieldDescriptorList2.add(authorizedAnnotationFieldDescriptor2);
        authorizedStudyElementsGroup2.setAuthorizedAnnotationFieldDescriptors(authorizedAnnotationFieldDescriptorList2);
        authorizedStudyElementsGroup2.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor2);

        // create groups list and add them
        List<AuthorizedStudyElementsGroup> authorizedStudyElementsGroups =
            new ArrayList<AuthorizedStudyElementsGroup>();

        // the authorizedStudyElementsGroup is ready, now add it to the list in the studyConfiguration
        studyConfiguration.setAuthorizedStudyElementsGroups(authorizedStudyElementsGroups);
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup1);

        // test various connections in the authorizedStudyElementsGroup structure
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGroup().getGroupName());
        assertEquals(group1Description,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGroup().getGroupDesc());
        assertEquals(afd1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedAnnotationFieldDescriptors().get(0).getAnnotationFieldDescriptor().getName());
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedAnnotationFieldDescriptors().get(0).getAuthorizedStudyElementsGroup().getAuthorizedGroup().getGroupName());
        assertEquals(group1Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGenomicDataSourceConfigurations().get(0).getAuthorizedStudyElementsGroup().getAuthorizedGroup().getGroupName());
        assertEquals(studyName,studyConfiguration.getAuthorizedStudyElementsGroups().get(0).getAuthorizedGenomicDataSourceConfigurations().get(0).getGenomicDataSourceConfiguration().getStudyConfiguration().getStudy().getShortTitleText());

        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup2);
        assertEquals(group2Name,studyConfiguration.getAuthorizedStudyElementsGroups().get(1).getAuthorizedGroup().getGroupName());
        assertEquals(group2Description,studyConfiguration.getAuthorizedStudyElementsGroups().get(1).getAuthorizedGroup().getGroupDesc());

    }
}
