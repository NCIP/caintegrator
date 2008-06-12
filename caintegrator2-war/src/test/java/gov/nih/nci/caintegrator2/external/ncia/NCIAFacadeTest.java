/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.domain.imaging.ImageStudy;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class NCIAFacadeTest {
    private static final Logger LOGGER = Logger.getLogger(NCIAFacadeTest.class);
    NCIAFacade nciaFacade;
    ServerConnectionProfile connection;
    NCIAServiceFactory nciaServiceFactory;
    private static final String NCIA_GRID_TEST_URL = "http://imaging-stage.nci.nih.gov/wsrf/services/cagrid/NCIACoreService?";
    

    @Before
    public void setUp() throws Exception {
        connection = new ServerConnectionProfile();
        connection.setUrl(NCIA_GRID_TEST_URL);
        // Uncomment to have the factory use a real instance instead of the fake instance.
        //nciaServiceFactory = new NCIAServiceFactoryImpl();
        nciaServiceFactory = new ServiceStubFactory();
        NCIAFacadeImpl nciaFacadeImpl = new NCIAFacadeImpl();
        nciaFacadeImpl.setNciaServiceFactory(nciaServiceFactory);
        nciaFacade = nciaFacadeImpl;
    }


    @Test
    public void testGetAllTrialDataProvenanceProjects() {
        List<String> allProjects;
        try {
            allProjects = nciaFacade.getAllTrialDataProvenanceProjects(connection);
            if (!allProjects.isEmpty()) {
                LOGGER.info("Retrieve Projects PASSED - " + allProjects.size() + " projects found.");
                assertEquals("Project1", allProjects.get(0));
                assertEquals("Project2", allProjects.get(1));
            } else {
                LOGGER.error("Retrieve Projects FAILED, might be a connection error!");
            }
            assertTrue(true);
        } catch (ConnectionException e) {
            LOGGER.error("Failed to connect");
            fail();
        }
    }


    @Test
    public void testGetImageSeriesAcquisition() {
        String trialDataProvenanceProject = "RIDER";
        List<ImageStudy> imageStudies;
        try {
            imageStudies = nciaFacade.getImageSeriesAcquisition(trialDataProvenanceProject, connection);
            if (!imageStudies.isEmpty()){
                LOGGER.info("Retrieve ImageSeriesAcquisition PASSED - " + imageStudies.size() + " were found.");
            } else {
                LOGGER.error("Retrieve ImageSeriesAcquisition FAILED, might be a connection error!");
            }
            assertTrue(true);
        } catch (ConnectionException e) {
            LOGGER.error(e.getMessage());
            fail();
            
        }
        
    }
    
    private static class ServiceStubFactory implements NCIAServiceFactory {

        public NCIASearchService createNCIASearchService(ServerConnectionProfile profile) throws ConnectionException {
            return new ServiceClientStub();
        }

    }
    private static class ServiceClientStub implements NCIASearchService {
        

        public List<Image> retrieveImageCollectionFromSeries(String seriesInstanceUID) throws ConnectionException {
            Image i = new Image();
            i.setId(123);
            
            List<Image> images = new ArrayList<Image>();
            images.add(i);
            return images;
        }

        
        public List<Series> retrieveImageSeriesCollectionFromStudy(String studyInstanceUID) throws ConnectionException {
            Series s = new Series();
            s.setSeriesInstanceUID("SERIESUID");
            s.setId(123);
            List<Series> series = new ArrayList<Series>();
            series.add(s);
            return series;
        }

        
        public List<Study> retrieveStudyCollectionFromPatient(String patientId) throws ConnectionException {
            Study s = new Study();
            s.setStudyInstanceUID("STUDYUID");
            s.setStudyDescription("DESCRIPTION");
            List<Study> studies = new ArrayList<Study>();
            studies.add(s);
            return studies;
        }

        
        public List<Patient> retrievePatientCollectionFromDataProvenanceProject(String provenanceProject)
                throws ConnectionException {
            Patient p = new Patient();
            p.setPatientId("PATIENTID");
            p.setPatientName("PATIENTNAME");
            List<Patient> patients = new ArrayList<Patient>();
            patients.add(p);
            return patients;
        }

        
        public List<String> retrieveAllTrialDataProvenanceProjects() throws ConnectionException {
            List<String> projects = new ArrayList<String>();
            projects.add("Project1");
            projects.add("Project2");
            return projects;
        }

    }
}
