/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.northwestern.radiology.aim.AnnotationImagingObservationCollection;
import edu.northwestern.radiology.aim.ImageAnnotation;
import edu.northwestern.radiology.aim.ImagingObservation;

/**
 * 
 */
public final class ImageAnnotationXMLParser {
    
    private ImageAnnotationXMLParser() { }
    
    /**
     * Parses the XML to an ImageAnnotation.
     * @param xmlImageAnnotation xml string.
     * @return ImageAnnotation object.
     */
    public static ImageAnnotation retrieveImageAnnotationFromXMLString(String xmlImageAnnotation) {
        ImageAnnotation imageAnnotation = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource objectSource = new InputSource(new StringReader(xmlImageAnnotation));
            Document dom = db.parse(objectSource);
            Element element = dom.getDocumentElement();
            NodeList imagingObservationNodeList = element.getElementsByTagName("ImagingObservationCharacteristic");
            if (imagingObservationNodeList != null && imagingObservationNodeList.getLength() > 0) {
                imageAnnotation = retrieveImagingObservations(imagingObservationNodeList);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Problem parsing XML.", e);
        } 
        return imageAnnotation;
    }

    private static ImageAnnotation retrieveImagingObservations(NodeList imagingObservationNodeList) {
        ImageAnnotation imageAnnotation = new ImageAnnotation();
        List<ImagingObservation> imagingObservations = new ArrayList<ImagingObservation>();
        imageAnnotation.setImagingObservationCollection(new AnnotationImagingObservationCollection());
        for (int i = 0; i < imagingObservationNodeList.getLength(); i++) {
            Element observationElement = (Element) imagingObservationNodeList.item(i);
            ImagingObservation imagingObservation = new ImagingObservation();
            imagingObservation.setCodeMeaning(observationElement.getAttribute("codeMeaning"));
            imagingObservation.setCodeValue(observationElement.getAttribute("codeValue"));
            imagingObservations.add(imagingObservation);
        }
        imageAnnotation.getImagingObservationCollection().setImagingObservation(
                imagingObservations.toArray(new ImagingObservation[imagingObservations.size()]));
        return imageAnnotation;
    }
}
