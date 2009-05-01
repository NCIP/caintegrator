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
package gov.nih.nci.caintegrator2.application.analysis.grid;

import static org.junit.Assert.assertArrayEquals;
import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceImpl;
import gov.nih.nci.caintegrator2.application.query.ResultHandlerImpl;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoImpl;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.analysis.MarkerResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractTransactionalSpringContextTests;

import au.com.bytecode.opencsv.CSVReader;


public class GenePatternGridRunnerImplTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private static final String PREPROCESS_DATASET_URL = "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/PreprocessDatasetMAGEService";
    private static final String COMPARATIVE_MARKER_URL = "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc";
    private static final String PCA_URL                = "http://node255.broad.mit.edu:6060/wsrf/services/cagrid/PCA";
    
    private GenePatternGridRunnerImpl genePatternGridRunner;
    private FileManager fileManager;
    private CaIntegrator2DaoImpl dao;
    private StudyHelper studyHelper;
    
    public void setupContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("genepattern-test-config.xml", GenePatternGridRunnerImplTestIntegration.class);
        genePatternGridRunner = (GenePatternGridRunnerImpl) context.getBean("genePatternGridRunnerIntegration");
        fileManager = (FileManager) context.getBean("fileManager");
        ArrayDataServiceStub arrayDataService = new ArrayDataServiceStub(); 
        QueryManagementServiceImpl queryManagementServiceImpl = new QueryManagementServiceImpl();
        queryManagementServiceImpl.setDao(dao);
        queryManagementServiceImpl.setArrayDataService(arrayDataService);
        queryManagementServiceImpl.setResultHandler(new ResultHandlerImpl());
        genePatternGridRunner.setQueryManagementService(queryManagementServiceImpl);
        
    }

    private StudySubscription setupStudySubscription() {
        studyHelper = new StudyHelper();
        StudySubscription subscription = studyHelper.populateAndRetrieveStudy();
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername("testUser");
        subscription.setUserWorkspace(userWorkspace);
        assertNull(subscription.getId());
        dao.save(subscription);
        assertNotNull(subscription.getId());
        return subscription;
    }
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/genepattern-test-config.xml"};
    }
    
    public void testRunPreprocessComparativeMarker() throws ConnectionException, IOException {
        setupContext();
        StudySubscription subscription = setupStudySubscription();
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(PREPROCESS_DATASET_URL);
        PreprocessDatasetParameters preprocessParameters = new PreprocessDatasetParameters();
        preprocessParameters.setServer(server);
        
        // Narrow the results based on 2 clinical queries, the first one will have 5 samples, the second will have
        // a 6th sample, so combined it should produce 6 sample rows.
        Query query1 = studyHelper.createQuery(studyHelper.createCompoundCriterion4(), 
                                            new HashSet<ResultColumn>(), subscription);
        Query query2 = studyHelper.createQuery(studyHelper.createCompoundCriterion5(), 
                                            new HashSet<ResultColumn>(), subscription);
        preprocessParameters.setProcessedGctFilename("test2.gct");
        preprocessParameters.getDatasetParameters().setLogBaseTwo(true);
        preprocessParameters.getClinicalQueries().add(query1);
        query1.setName("Query 1");
        preprocessParameters.getClinicalQueries().add(query2);
        query2.setName("Query  2");
        ComparativeMarkerSelectionParameters comparativeMarkerParameters = new ComparativeMarkerSelectionParameters();
        comparativeMarkerParameters.setClassificationFileName("test2.cls");
        comparativeMarkerParameters.getClinicalQueries().addAll(preprocessParameters.getClinicalQueries());
        ServerConnectionProfile comparativeMarkerServer = new ServerConnectionProfile();
        comparativeMarkerServer.setUrl(COMPARATIVE_MARKER_URL);
        comparativeMarkerParameters.setServer(comparativeMarkerServer);

        List <MarkerResult> results = genePatternGridRunner.runPreprocessComparativeMarkerSelection(subscription, preprocessParameters, comparativeMarkerParameters);
        assertEquals(6, results.size());
        File gctFile = new File(fileManager.getUserDirectory(subscription) + File.separator 
                + preprocessParameters.getProcessedGctFilename());
        checkGctFile(gctFile, 6, 6, 0.2986583f);
        gctFile.deleteOnExit();
        
        File clsFile = new File(fileManager.getUserDirectory(subscription) + File.separator 
                + comparativeMarkerParameters.getClassificationFileName());
        clsFile.deleteOnExit();
        checkClsFile(clsFile);
    }
    
    @Test
    public void testRunPCA() throws ConnectionException {
        setupContext();
        StudySubscription subscription = setupStudySubscription();
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(PCA_URL);
        PCAParameters parameters = new PCAParameters();
        parameters.setClusterBy("columns");
        parameters.setGctFileName("testPca.gct");
        parameters.setClassificationFileName("testPca.cls");
        parameters.setServer(server);
        // Narrow the results based on 2 clinical queries, the first one will have 5 samples, the second will have
        // a 6th sample, so combined it should produce 6 sample rows.
        Query query1 = studyHelper.createQuery(studyHelper.createCompoundCriterion4(), 
                                            new HashSet<ResultColumn>(), subscription);
        Query query2 = studyHelper.createQuery(studyHelper.createCompoundCriterion5(), 
                                            new HashSet<ResultColumn>(), subscription);
        parameters.getClinicalQueries().add(query1);
        parameters.getClinicalQueries().add(query2);
        // For now, since the PCA service is up and down, don't fail on this test yet.
        boolean isConnectionException = false;
        File zipFile = null;
        try {
            zipFile = genePatternGridRunner.runPCA(subscription, parameters);
        } catch (ConnectionException e) {
            isConnectionException = true;
        }
        if (!isConnectionException) {
            assertNotNull(zipFile);
        }
    }

    private void checkClsFile(File clsFile) throws IOException {
        assertTrue(clsFile.exists());
        CSVReader reader = new CSVReader(new FileReader(clsFile), ' ');
        checkLine(reader.readNext(), "6", "2", "1");
        checkLine(reader.readNext(), "#", "Query_1", "Query__2");
        checkLine(reader.readNext(), "0", "0", "0", "0", "0", "1");
    }

    
    private void checkGctFile(File gctFile, int numSamples, int numReporters, float value) throws IOException {
        assertTrue(gctFile.exists());
        CSVReader reader = new CSVReader(new FileReader(gctFile), '\t');
        checkLine(reader.readNext(), "#1.2");
        checkLine(reader.readNext(), String.valueOf(numSamples), String.valueOf(numReporters));
        reader.readNext(); // Header
        checkValue(reader.readNext(), value); // Row values
    }

    private void checkValue(String[] readNext, float f) {
        assertEquals(String.valueOf(f), readNext[2]);
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }
    
    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setDao(CaIntegrator2DaoImpl caIntegrator2Dao) {
        this.dao = caIntegrator2Dao;
    }

}
