package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.caintegrator2.TestArrayDesignFiles;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.jboss.mq.SpyMessage;
import org.jboss.mq.SpyObjectMessage;
import org.junit.Before;
import org.junit.Test;

public class PlatformLoaderListenerTest {
    
    private PlatformLoaderListener listener = new PlatformLoaderListener();
    private ArrayDataServiceStub arrayDataServiceStub = new ArrayDataServiceStub();
    
    @Before
    public void setUp() {
        arrayDataServiceStub.clear();
        listener.setArrayDataService(arrayDataServiceStub);
    }

    @Test
    public void testOnMessageAffyExp() throws JMSException, PlatformLoadingException {
        listener.onMessage(new SpyMessage());
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        ObjectMessage testMessage = new SpyObjectMessage();
        testMessage.setObject("A String");
        listener.onMessage(testMessage);
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        testMessage.setObject(new AffymetrixExpressionPlatformSource(TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE));
        listener.onMessage(testMessage);
        assertTrue(arrayDataServiceStub.loadArrayDesignCalled);
        try{
            new AffymetrixExpressionPlatformSource(null);
        } catch (IllegalArgumentException e){
            assertEquals("Annotation file must exist.", e.getMessage());
        }
    }

    @Test
    public void testOnMessageAffyDna() throws JMSException, PlatformLoadingException {
        listener.onMessage(new SpyMessage());
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        ObjectMessage testMessage = new SpyObjectMessage();
        testMessage.setObject("A String");
        listener.onMessage(testMessage);
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        List<File> annotationFiles = new ArrayList<File>();
        annotationFiles.add(TestArrayDesignFiles.MAPPING_50K_HIND_ANNOTATION_FILE);
        annotationFiles.add(TestArrayDesignFiles.MAPPING_50K_XBA_ANNOTATION_FILE);
        testMessage.setObject(new AffymetrixDnaPlatformSource(annotationFiles, "AffyDnaPlatform"));
        listener.onMessage(testMessage);
        assertTrue(arrayDataServiceStub.loadArrayDesignCalled);
        try{
            new AffymetrixDnaPlatformSource(new ArrayList<File>(), "");
        } catch (IllegalArgumentException e){
            assertEquals("Annotation file must exist.", e.getMessage());
        }
    }

    @Test
    public void testOnMessageAgilentExp() throws JMSException, PlatformLoadingException {
        listener.onMessage(new SpyMessage());
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        ObjectMessage testMessage = new SpyObjectMessage();
        testMessage.setObject("A String");
        listener.onMessage(testMessage);
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        testMessage.setObject(new AgilentPlatformSource(TestArrayDesignFiles.AGILENT_G4502A_07_01_TCGA_ADF_ANNOTATION_FILE,
                "AgilentPlatform", "agilent_annotation.csv"));
        listener.onMessage(testMessage);
        assertTrue(arrayDataServiceStub.loadArrayDesignCalled);
        try{
            new AgilentPlatformSource(null, "", "");
        } catch (IllegalArgumentException e){
            assertEquals("Annotation file must exist.", e.getMessage());
        }
    }

}
