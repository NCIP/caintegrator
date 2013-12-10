/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * AIM Facade intergration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
public class AIMFacadeTestIntegration {
    @Autowired
    private AIMFacadeImpl aimFacade;
    private ServerConnectionProfile connection;

    @Before
    public void setUp() throws Exception {
        connection = new ServerConnectionProfile();
        connection.setUrl("http://node01.cci.emory.edu/wsrf/services/cagrid/AIMTCGADataService");
    }

    @Test
    public void testRetrieveImageSeriesAnnotations() throws ConnectionException {
        ImageSeries imageSeries =
                createImageSeries("1.3.6.1.4.1.9328.50.45.239261393324265132190998071373586264552"); // Real.
        ImageSeries imageSeries2 =
                createImageSeries("1.3.6.1.4.1.9328.50.45.184391227224635150626093626236425843441"); // Real.
        ImageSeries imageSeries3 =
                createImageSeries("1.3.6.1.4.1.9328.50.45.263636323693757141556368836667354272766"); // Real.
        ImageSeries imageSeries4 = createImageSeries("FAKE"); // Not Real.

        List<ImageSeries> imageSeriesCollection = new ArrayList<ImageSeries>();
        imageSeriesCollection.add(imageSeries);
        imageSeriesCollection.add(imageSeries2);
        imageSeriesCollection.add(imageSeries3);
        imageSeriesCollection.add(imageSeries4);
        imageSeriesCollection.add(createImageSeries("1.3.6.1.4.1.9328.50.46.145267262126068158967843466003517627535"));
        imageSeriesCollection.add(createImageSeries("1.3.6.1.4.1.9328.50.46.134415699017245527869615131670064141114"));
        imageSeriesCollection.add(createImageSeries("1.3.6.1.4.1.9328.50.46.97643358689862616374909009862171869978"));
        imageSeriesCollection.add(createImageSeries("1.3.6.1.4.1.9328.50.46.38078973032022842982142055297931730041"));
        imageSeriesCollection.add(createImageSeries("1.3.6.1.4.1.9328.50.46.223790420624969264518080364078948505666"));

        Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotations
            = aimFacade.retrieveImageSeriesAnnotations(connection, imageSeriesCollection);
        assertTrue(imageSeriesAnnotations.containsKey(imageSeries));
        assertTrue(imageSeriesAnnotations.containsKey(imageSeries2));
        assertTrue(imageSeriesAnnotations.containsKey(imageSeries3));
        assertFalse(imageSeriesAnnotations.containsKey(imageSeries4));
        assertEquals(8, imageSeriesAnnotations.keySet().size());
        assertEquals(21, imageSeriesAnnotations.get(imageSeries)
                .getAnnotationGroupToDefinitionMap().get("Imaging Observations").keySet().size());
    }

    private ImageSeries createImageSeries(String id) {
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setIdentifier(id);
        return imageSeries;
    }
}
