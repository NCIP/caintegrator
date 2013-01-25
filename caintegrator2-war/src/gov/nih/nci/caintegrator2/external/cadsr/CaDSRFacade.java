/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.List;

/**
 * This is the facade that interfaces with caDSR to access Common Data Elements.
 */
public interface CaDSRFacade {
    
    /**
     * URL for caDSR CDE. 
     */
    String CDE_URL = "https://cdebrowser.nci.nih.gov/CDEBrowser/search?dataElementDetails=9" 
                      + "&PageId=DataElementsGroup"
                      + "&queryDE=yes"
                      + "&FirstTimer=yes";

    /**
     * To retrieve all candidate Data Elements given keywords.
     * @param keywords List of keywords to search caDSR for.
     * @return - DataElements that freestyle search found.
     */
    List<CommonDataElement> retreiveCandidateDataElements(List<String> keywords);
    
    /**
     * Retrieves the ValueDomain object from a data element ID.  Currently this function is disabled and
     * won't work unless the proper caDSR URL and freestyle search URL are injected into the implemented version.
     * The reason it's disabled is because caDSR 4.0 is on Stage environment, when it is in production, this can
     * be activated to work by a simple spring configuration change of the URL.
     * @param dataElementId - Id from caDSR data element.
     * @param dataElementVersion - Version of data element from caDSR.
     * @return - ValueDomain object associated with data element.
     * @throws ConnectionException - Error connecting to caDSR.
     */
    ValueDomain retrieveValueDomainForDataElement(Long dataElementId, Float dataElementVersion)
    throws ConnectionException;
    
    /**
     * To retrieve a Data Element given publicId.
     * @param dataElementId the id to search caDSR for.
     * @param dataElementVersion the version to search caDSR for.
     * @return - DataElement.
     * @throws ConnectionException - Error connecting to caDSR.
     */
    CommonDataElement retrieveDataElement(Long dataElementId, Float dataElementVersion)
    throws ConnectionException;
}
