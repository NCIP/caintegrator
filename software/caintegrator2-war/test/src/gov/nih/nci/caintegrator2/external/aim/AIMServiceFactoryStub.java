/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import edu.northwestern.radiology.aim.jaxb.ImagingObservationCharacteristic;
import edu.northwestern.radiology.aim.jaxb.impl.ImageAnnotationImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationCharacteristicImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationImpl;
import edu.northwestern.radiology.aim.jaxb.impl.AnnotationImpl.ImagingObservationCollectionImpl;
import edu.northwestern.radiology.aim.jaxb.impl.ImagingObservationImpl.ImagingObservationCharacteristicCollectionImpl;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class AIMServiceFactoryStub implements AIMServiceFactory {


    public AIMSearchService createAIMSearchService(ServerConnectionProfile connection) throws ConnectionException {
        return new ServiceClientStub();
    }
    
    private class ServiceClientStub implements AIMSearchService {
        private boolean alreadyCalled = false;
        
        public ImageAnnotation getImageSeriesAnnotation(String seriesInstanceUID) throws ConnectionException {
            if (!alreadyCalled) {
                alreadyCalled = true;
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
                return imageAnnotation;
            }
            return null;
        }
        
    }

}
