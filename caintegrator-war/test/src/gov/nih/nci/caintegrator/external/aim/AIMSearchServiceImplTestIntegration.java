/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.aim.AIMSearchServiceImpl;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class AIMSearchServiceImplTestIntegration {
    
    private AIMSearchServiceImpl aimSearchService;
    
    @Before
    public void setUp() throws Exception {
        ServerConnectionProfile conn = new ServerConnectionProfile();
        conn.setUrl("http://node01.cci.emory.edu/wsrf/services/cagrid/AIMTCGADataService");
        aimSearchService = new AIMSearchServiceImpl(conn);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.external.aim.AIMSearchServiceImpl#getImageSeriesAnnotation(java.lang.String)}.
     * @throws ConnectionException 
     */
    @Test
    public void testGetImageSeriesAnnotation() throws ConnectionException {
        assertTrue(aimSearchService.retrieveAllSeriesIdentifiers().contains("1.3.6.1.4.1.9328.50.45.288861537619222119588401224045738055768"));
        ImageAnnotation annotation = aimSearchService.getImageSeriesAnnotation("1.3.6.1.4.1.9328.50.45.288861537619222119588401224045738055768");
        assertEquals(21, annotation.getImagingObservationCollection().getImagingObservations().get(0).getImagingObservationCharacteristicCollection().getImagingObservationCharacteristics().size());
        
        annotation = aimSearchService.getImageSeriesAnnotation("FAKE");
        assertEquals(null, annotation);
        
        
    }

}
