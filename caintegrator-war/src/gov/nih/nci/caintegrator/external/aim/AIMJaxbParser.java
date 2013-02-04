/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;



/**
 * Converts an XML String to jaxb.ImageAnnotation.
 * This class is used with the JaxB beans because of the following issue (Tony Pan explains):
 * 
 * The problem is that ImageReference is an abstract xsd type that is mapped to a java bean that’s abstract (can’t be 
 * instantiated).  Apache Axis apparently is unable to parse the xsi:type and instantiate the correct subclass of 
 * ImageReference.
 */
public final class AIMJaxbParser {
    
    private AIMJaxbParser() { }
    
    /**
     * Retrieves ImageAnnotation (JaxB) from XML string.
     * @param xmlImageAnnotation xml string representing an ImageAnnotation object.
     * @return ImageAnnotationObject.
     */
    public static ImageAnnotation retrieveImageAnnotationFromXMLString(String xmlImageAnnotation) {
        try {
            JAXBContext jc = JAXBContext.newInstance(ImageAnnotation.class.getPackage().getName());
            Unmarshaller um = jc.createUnmarshaller();
            return (ImageAnnotation) um.unmarshal(new InputSource(new StringReader(xmlImageAnnotation)));
            
        } catch (Exception e) {
            return null;
        } 

    }
}
