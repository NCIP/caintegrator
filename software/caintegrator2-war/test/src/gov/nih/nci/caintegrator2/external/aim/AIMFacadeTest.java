/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
public class AIMFacadeTest {
    AIMFacadeImpl aimFacade;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("aim-test-config.xml", AIMFacadeTest.class); 
        aimFacade = (AIMFacadeImpl) context.getBean("aimFacade");
    }


    @Test
    public void testRetrieveImageSeriesAnnotations() throws ConnectionException {
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setIdentifier("1.1.1");
        ImageSeries imageSeries2NoAdd = new ImageSeries();
        imageSeries2NoAdd.setIdentifier("1.1.2");
        List<ImageSeries> imageSeriesCollection = new ArrayList<ImageSeries>();
        imageSeriesCollection.add(imageSeries);
        imageSeriesCollection.add(imageSeries2NoAdd);
        Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotations
            = aimFacade.retrieveImageSeriesAnnotations(null, imageSeriesCollection);
        assertTrue(imageSeriesAnnotations.containsKey(imageSeries));
        assertFalse(imageSeriesAnnotations.containsKey(imageSeries2NoAdd));
        assertEquals(2, 
                imageSeriesAnnotations.get(imageSeries).getAnnotationGroupToDefinitionMap().get("Imaging Observations").keySet().size());
    }
        
}
