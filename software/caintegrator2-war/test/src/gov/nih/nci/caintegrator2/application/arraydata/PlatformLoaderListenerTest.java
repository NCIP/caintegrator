package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    public void testOnMessage() throws JMSException {
        listener.onMessage(new SpyMessage());
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        ObjectMessage testMessage = new SpyObjectMessage();
        testMessage.setObject("A String");
        listener.onMessage(testMessage);
        assertFalse(arrayDataServiceStub.loadArrayDesignCalled);
        testMessage.setObject(new AffymetrixPlatformSource(TestArrayDesignFiles.HG_U133_PLUS_2_ANNOTATION_FILE));
        listener.onMessage(testMessage);
        assertTrue(arrayDataServiceStub.loadArrayDesignCalled);
        try{
            new AffymetrixPlatformSource(null);
        } catch (IllegalArgumentException e){
            assertEquals("Annotation file must exist.", e.getMessage());
        }
    }

}
