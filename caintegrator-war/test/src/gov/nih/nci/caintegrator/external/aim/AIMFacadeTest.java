/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import edu.northwestern.radiology.aim.jaxb.ImagingObservationCharacteristic;
import edu.northwestern.radiology.aim.jaxb.impl.AnnotationImpl.ImagingObservationCollectionImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImageAnnotationImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationCharacteristicImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationImpl.ImagingObservationCharacteristicCollectionImpl;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.aim.AIMFacadeImpl;
import gov.nih.nci.caintegrator.external.aim.AIMSearchService;
import gov.nih.nci.caintegrator.external.aim.AIMServiceFactory;
import gov.nih.nci.caintegrator.external.aim.ImageSeriesAnnotationsWrapper;

/**
 *
 */
public class AIMFacadeTest {
    private AIMFacadeImpl aimFacade;

    @Before
    public void setUp() throws Exception {
        ImageAnnotation imageAnnotation = new ImageAnnotationImpl();
        List<ImagingObservationCharacteristic> imagingObservationCharacteristics =
            new ArrayList<ImagingObservationCharacteristic>();
        ImagingObservationCharacteristic characteristic1 = new ImagingObservationCharacteristicImpl();
        characteristic1.setCodeMeaning("Size");
        characteristic1.setCodeValue("1");
        ImagingObservationCharacteristic characteristic2 = new ImagingObservationCharacteristicImpl();
        characteristic2.setCodeMeaning("Width");
        characteristic2.setCodeValue("2");
        imagingObservationCharacteristics.add(characteristic1);
        imagingObservationCharacteristics.add(characteristic2);
        ImagingObservationImpl imagingObservation = new ImagingObservationImpl();
        imagingObservation.setImagingObservationCharacteristicCollection(new ImagingObservationCharacteristicCollectionImpl());
        imagingObservation.getImagingObservationCharacteristicCollection().
            getImagingObservationCharacteristics().addAll(imagingObservationCharacteristics);
        imageAnnotation.setImagingObservationCollection(new ImagingObservationCollectionImpl());
        imageAnnotation.getImagingObservationCollection().getImagingObservations().add(imagingObservation);

        AIMSearchService aimService = mock(AIMSearchService.class);
        when(aimService.getImageSeriesAnnotation(anyString())).thenReturn(imageAnnotation, new ImageAnnotation[] {null});
        AIMServiceFactory aimServiceFactory = mock(AIMServiceFactory.class);
        when(aimServiceFactory.createAIMSearchService(any(ServerConnectionProfile.class))).thenReturn(aimService);

        aimFacade = new AIMFacadeImpl();
        aimFacade.setAimServiceFactory(aimServiceFactory);
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
