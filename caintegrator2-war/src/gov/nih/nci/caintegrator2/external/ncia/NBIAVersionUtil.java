/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;


/**
 * Utility class that looks at the "Series" object to determine if NBIA is version 4.4 or 4.5.
 */
public final class NBIAVersionUtil {

    private NBIAVersionUtil() { }

    /**
     * 
     * @param nbiaUrl connection URL.
     * @return version of NBIA.
     * @throws MalformedURIException - if unable to connect to NBIA.
     * @throws ResourcePropertyRetrievalException - if unable to connect to NBIA.
     */
    public static NBIAVersionEnum getVersion(String nbiaUrl) throws MalformedURIException, 
        ResourcePropertyRetrievalException  {
        EndpointReferenceType endpoint = new EndpointReferenceType(new URI(nbiaUrl));
        DomainModel domainModel = MetadataUtils.getDomainModel(endpoint);
        DomainModelExposedUMLClassCollection classes = domainModel.getExposedUMLClassCollection();
        for (UMLClass umlClass : classes.getUMLClass()) {
            if (umlClass.getClassName().equals("Series")) {
                return isNbiaV44Series(umlClass) ? NBIAVersionEnum.V4_4 : NBIAVersionEnum.V4_5;
            }
        }
        throw new IllegalStateException("Never came across Series UML Class if we got here");
    }

    private static boolean isNbiaV44Series(UMLClass seriesClass) {
        UMLClassUmlAttributeCollection attrCollection = seriesClass.getUmlAttributeCollection();
        for (UMLAttribute umlClassAttribute : attrCollection.getUMLAttribute()) {
            if (NBIAVersionEnum.V4_4.getSeriesIdAtt().equals(umlClassAttribute.getName())) {
                return true;
            }
        }
        return false;
    }

}
