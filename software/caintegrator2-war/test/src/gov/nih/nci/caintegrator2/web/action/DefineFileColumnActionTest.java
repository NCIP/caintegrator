package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class DefineFileColumnActionTest {
    
    private DefineFileColumnAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("action-test-config.xml", DefineClinicalSourceActionTest.class); 
        action = (DefineFileColumnAction) context.getBean("defineFileColumnAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testEditFileColumn() {
        assertEquals("editFileColumn", action.editFileColumn());
    }
    
    @Test
    public void testPrepare() {
        action.getFileColumn().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }
    
    @Test
    public void testSearchDefinitions() {
        assertEquals("editFileColumn", action.searchDefinitions());
        assertTrue(studyManagementServiceStub.getMatchingDefinitionsCalled);
        assertTrue(studyManagementServiceStub.getMatchingDataElementsCalled);
    }
    
    @Test
    public void testSelectDefintion() {
        action.getDefinitions().add(null);
        assertEquals("editFileColumn", action.selectDefinition());
        assertTrue(studyManagementServiceStub.setDefinitionCalled);
    }
    
    @Test
    public void testSelectDataElement() {
        action.getDataElements().add(null);
        assertEquals("editFileColumn", action.selectDataElement());
        assertTrue(studyManagementServiceStub.setDataElementCalled);
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSetColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        action.getFileColumn().setAnnotationFile(annotationFile);
        assertEquals("Annotation", action.getColumnType());
        action.setColumnType("Identifier");
        assertEquals(annotationFile.getIdentifierColumn(), action.getFileColumn());
        assertEquals("Identifier", action.getColumnType());
        action.setColumnType("Timepoint");
        assertEquals(annotationFile.getTimepointColumn(), action.getFileColumn());
        assertEquals("Timepoint", action.getColumnType());
        action.setColumnType("Annotation");
        assertEquals("Annotation", action.getColumnType());
    }

}
