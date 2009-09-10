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
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotHandler;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.grid.GenePatternGridRunner;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AnalysisService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class AnalysisServiceImpl implements AnalysisService {
    
    private CaIntegrator2Dao dao;
    private KMPlotService kmPlotService;
    private GeneExpressionPlotService gePlotService;
    private QueryManagementService queryManagementService;
    private GenePatternClientFactory genePatternClientFactory;
    private GenePatternGridRunner genePatternGridRunner;
    private FileManager fileManager;
    
    /**
     * {@inheritDoc}
     */
    public List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server)
            throws GenePatternServiceException {
        return new GenePatternHelper(retrieveClient(server)).getMethods();
    }

    private GenePatternClient retrieveClient(ServerConnectionProfile server) {
        return genePatternClientFactory.retrieveClient(server);
    }

    /**
     * {@inheritDoc}
     * @throws GenePatternServiceException 
     */
    public JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) 
    throws GenePatternServiceException {
        JobInfoWrapper jobInfo = new GenePatternHelper(retrieveClient(server)).execute(invocation);
        URL serviceUrl;
        URL resultUrl;
        try {
            serviceUrl = new URL(server.getUrl());
            resultUrl = new URL(serviceUrl.getProtocol(), serviceUrl.getHost(), serviceUrl.getPort(), 
                    "/gp/pages/jobResults.jsf");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL provided for server " + server.getUrl(), e);
        }
        jobInfo.setUrl(resultUrl);
        return jobInfo;
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException {
        return genePatternGridRunner.runPreprocessComparativeMarkerSelection(updater, job);
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException, ParameterException, IOException {
        job.setSubscription(dao.get(job.getSubscription().getId(), StudySubscription.class));
        if (job.isGridServiceCall()) {
            return genePatternGridRunner.runGistic(updater, job);
        } else {
            ServerConnectionProfile server = job.getGisticAnalysisForm().getGisticParameters().getServer();
            GisticWebServiceRunner gisticWebServiceRunner = 
                new GisticWebServiceRunner(retrieveClient(server), getQueryManagementService(), getFileManager());
            return gisticWebServiceRunner.runGistic(updater, job);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job) throws ConnectionException, InvalidCriterionException {
        job.setSubscription(dao.get(job.getSubscription().getId(), StudySubscription.class));
        File gctFile = null;
        if (job.getForm().isUsePreprocessDataset()) {
            gctFile = genePatternGridRunner.runPreprocessDataset(updater,
                    job, job.getForm().getPreprocessParameters());
        }
        return genePatternGridRunner.runPCA(updater, job, gctFile);
    }

    /**
     * {@inheritDoc}
     */
    public KMPlot createKMPlot(StudySubscription subscription,
                               AbstractKMParameters kmParameters) throws InvalidCriterionException {
        AbstractKMPlotHandler kmPlotHandler = AbstractKMPlotHandler.createKMPlotHandler(
                dao, kmParameters.getSurvivalValueDefinition(), queryManagementService, kmParameters);
        return kmPlotHandler.createPlot(kmPlotService, subscription);

    }
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) 
    throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        if (StringUtils.isNotBlank(plotParameters.getGeneSymbol())) {
            validateGeneSymbols(studySubscription, plotParameters);
        }
        AbstractGEPlotHandler gePlotHandler = AbstractGEPlotHandler.createGeneExpressionPlotHandler(
                dao, queryManagementService, plotParameters, gePlotService);
        
        return gePlotHandler.createPlots(studySubscription);
    }

    private void validateGeneSymbols(StudySubscription studySubscription, AbstractGEPlotParameters plotParameters)
            throws GenesNotFoundInStudyException {
        plotParameters.getGenesNotFound().clear();
        List<String> inputGeneSymbols = Arrays.asList(plotParameters.getGeneSymbol().replaceAll(" ", "").split(","));
        Set<String> genesInStudy = dao.retrieveGeneSymbolsInStudy(inputGeneSymbols, studySubscription.getStudy());
        if (genesInStudy.isEmpty()) {
            throw new GenesNotFoundInStudyException("None of the specified genes were found in study.");
        }
        for (String geneSymbol : inputGeneSymbols) {
            if (!Cai2Util.containsIgnoreCase(genesInStudy, geneSymbol)) {
                plotParameters.getGenesNotFound().add(geneSymbol);
            }
        }
        if (!plotParameters.getGenesNotFound().isEmpty()) {
            Collections.sort(plotParameters.getGenesNotFound());
        }
    }


    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao
     *            the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * @return the kmPlotService
     */
    public KMPlotService getKmPlotService() {
        return kmPlotService;
    }

    /**
     * @param kmPlotService the kmPlotService to set
     */
    public void setKmPlotService(KMPlotService kmPlotService) {
        this.kmPlotService = kmPlotService;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the gePlotService
     */
    public GeneExpressionPlotService getGePlotService() {
        return gePlotService;
    }

    /**
     * @param gePlotService the gePlotService to set
     */
    public void setGePlotService(GeneExpressionPlotService gePlotService) {
        this.gePlotService = gePlotService;
    }

    /**
     * @return the genePatternClientFactory
     */
    public GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    /**
     * @param genePatternClientFactory the genePatternClientFactory to set
     */
    public void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the genePatternGridRunner
     */
    public GenePatternGridRunner getGenePatternGridRunner() {
        return genePatternGridRunner;
    }

    /**
     * @param genePatternGridRunner the genePatternGridRunner to set
     */
    public void setGenePatternGridRunner(GenePatternGridRunner genePatternGridRunner) {
        this.genePatternGridRunner = genePatternGridRunner;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void deleteAnalysisJob(Long jobId) {
        AbstractPersistedAnalysisJob job = getAnalysisJob(jobId);
        job.getSubscription().getAnalysisJobCollection().remove(job);
        deleteResultFile(job);
        dao.delete(job);
    }
    
    private void deleteResultFile(AbstractPersistedAnalysisJob job) {
        if (job.getResultsZipFile() != null) {
            FileUtils.deleteQuietly(job.getResultsZipFile().getFile());
        }
    }

    private AbstractPersistedAnalysisJob getAnalysisJob(Long id) {
        return dao.get(id, AbstractPersistedAnalysisJob.class);
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
