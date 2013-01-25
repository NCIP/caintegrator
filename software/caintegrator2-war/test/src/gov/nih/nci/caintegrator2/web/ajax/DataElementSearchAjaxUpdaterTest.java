/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.freestylesearch.util.SearchException;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.acegisecurity.context.SecurityContextHolder;
import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


public class DataElementSearchAjaxUpdaterTest {

    private DataElementSearchAjaxUpdater updater;
    private StudyManagementServiceStubForSearch studyManagementService;

    @Before
    public void setUp() throws Exception {
        updater = new DataElementSearchAjaxUpdater();
        studyManagementService = new StudyManagementServiceStubForSearch();
        studyManagementService.clear();
        updater.setStudyManagementService(studyManagementService);
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        WebContextFactory.setWebContextBuilder(new WebContextBuilderStub());
    }

    @Test
    public void testRunSearch() throws InterruptedException, ServletException, IOException {
        String entityType = "subject";
        String studyConfId = "1";
        String fileColId = "1";
        String keywords = "test keywords";
        String searchResultJsp = "searchResult.jsp";
      
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
        searchResultJsp = "";
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
        while (true) {
            Thread.sleep(600);
            if (!updater.isCurrentlyRunning()) { // Need to verify all Threads are not running before proceeding.
                break;
            }
        }
        assertTrue(studyManagementService.getMatchingDataElementsCalled);
        assertTrue(studyManagementService.getMatchingDefinitionsCalled);
        
        keywords = "exceptionTest";
        entityType = "image";
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
        while (true) {
            Thread.sleep(600);
            if (!updater.isCurrentlyRunning()) { // Need to verify all Threads are not running before proceeding.
                break;
            }
        }
        
        entityType = "image";
        keywords = "empty";
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
        while (true) {
            Thread.sleep(600);
            if (!updater.isCurrentlyRunning()) { // Need to verify all Threads are not running before proceeding.
                break;
            }
        }
        keywords = null;
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        studyManagementService.delayTime = 2000;
        updater.initializeJsp("searchResult.jsp");
        String entityType = "subject";
        String studyConfId = "1";
        String fileColId = "1";
        String keywords = "test keywords";
        String searchResultJsp = "searchResult.jsp";
        updater.runSearch(entityType, studyConfId, fileColId, keywords, searchResultJsp);
        Thread.sleep(600);
        assertTrue(updater.isCurrentlyRunning());
        updater.initializeJsp("searchResult.jsp");
        assertTrue(updater.isCurrentlyRunning());
    }
    
    private class StudyManagementServiceStubForSearch extends StudyManagementServiceStub {
        public int delayTime = 1000;
        
        @Override
        public List<CommonDataElement> getMatchingDataElements(List<String> keywords) {
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
            }
            if ("exceptionTest".equals(keywords.get(0))) {
                throw new SearchException("Failure Test!");
            }
            if ("empty".equals(keywords.get(0))) {
                return new ArrayList<CommonDataElement>();
            }
            super.getMatchingDataElements(keywords);
            List<CommonDataElement> cdes = new ArrayList<CommonDataElement>();
            cdes.add(new CommonDataElement());
            cdes.add(new CommonDataElement());
            return cdes;
        }
        
        @Override
        public List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords) {
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
            }
            if ("empty".equals(keywords.get(0))) {
                return new ArrayList<AnnotationDefinition>();
            }
            super.getMatchingDefinitions(keywords);
            List<AnnotationDefinition> annotationDefinitions = new ArrayList<AnnotationDefinition>();
            annotationDefinitions.add(new AnnotationDefinition());
            annotationDefinitions.add(new AnnotationDefinition());
            return annotationDefinitions;
        }
    }

}
