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
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs study deployments and notifies clients.
 */
@Transactional (propagation = Propagation.REQUIRED)
public class DeploymentServiceImpl implements DeploymentService {
    private static final Integer MAX_DESCRIPTION_LENGTH = 255;
    private static final Logger LOGGER = Logger.getLogger(DeploymentServiceImpl.class);
    
    private CaArrayFacade caArrayFacade;
    private CaIntegrator2Dao dao;
    private ArrayDataService arrayDataService;
    private BioconductorService bioconductorService;
    private DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory = new DnaAnalysisHandlerFactoryImpl();
    private ExpressionHandlerFactory expressionHandlerFactory = new ExpressionHandlerFactoryImpl();
    private GenePatternClientFactory genePatternClientFactory;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.AvoidReassigningParameters") // preferable in this instance for error handling.
    public Status prepareForDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener) {
        try {
            studyConfiguration = getDao().get(studyConfiguration.getId(), StudyConfiguration.class);
            return startDeployment(studyConfiguration, listener);
        } catch (Exception e) {
            return handleDeploymentFailure(studyConfiguration, listener, e);
        }
    }

    /**
     * {@inheritDoc}
     * @throws ValidationException 
     * @throws DataRetrievalException 
     * @throws ConnectionException 
     */
    @SuppressWarnings("PMD.AvoidReassigningParameters") // preferable in this instance for error handling.
    public Status performDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener)
    throws ConnectionException, DataRetrievalException, ValidationException {
            studyConfiguration = getDao().get(studyConfiguration.getId(), StudyConfiguration.class);
            if (!Status.PROCESSING.equals(studyConfiguration.getStatus())) {
                startDeployment(studyConfiguration, listener);
            }
            return doDeployment(studyConfiguration, listener);
    }

    /**
     * {@inheritDoc}
     */
    public Status handleDeploymentFailure(StudyConfiguration studyConfiguration,
            DeploymentListener listener, Throwable e) {
        LOGGER.error("Deployment of study " + studyConfiguration.getStudy().getShortTitleText()
                + " failed.", e);
        studyConfiguration.setStatusDescription((e.getMessage() != null)
                ? Cai2Util.trimStringIfTooLong(e.getMessage(), MAX_DESCRIPTION_LENGTH) 
                : Cai2Util.trimStringIfTooLong(e.toString(), MAX_DESCRIPTION_LENGTH));
        studyConfiguration.setStatus(Status.ERROR);
        updateStatus(studyConfiguration, listener);
        return studyConfiguration.getStatus();
    }

    private void updateStatus(StudyConfiguration studyConfiguration, DeploymentListener listener) {
        getDao().save(studyConfiguration);
        if (listener != null) {
            listener.statusUpdated(studyConfiguration);
        }
    }

    private Status doDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        if (!studyConfiguration.getGenomicDataSources().isEmpty()) {
            GenomicDataHelper genomicDataHelper = new GenomicDataHelper(getCaArrayFacade(), 
                    getArrayDataService(), getDao(), getBioconductorService(), getDnaAnalysisHandlerFactory());
            genomicDataHelper.setExpressionHandlerFactory(getExpressionHandlerFactory());
            genomicDataHelper.setGenePatternClientFactory(getGenePatternClientFactory());
            genomicDataHelper.loadData(studyConfiguration);
        }
        studyConfiguration.setStatus(Status.DEPLOYED);
        studyConfiguration.setDeploymentFinishDate(new Date());
        studyConfiguration.setStatusDescription("Minutes for deployment (approx):  " 
                + DateUtil.compareDatesInMinutes(studyConfiguration.getDeploymentStartDate(), 
                                                 studyConfiguration.getDeploymentFinishDate()));
        updateStatus(studyConfiguration, listener);
        return studyConfiguration.getStatus();
    }

    private Status startDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener) {
        studyConfiguration.setDeploymentStartDate(new Date());
        studyConfiguration.setDeploymentFinishDate(null);
        studyConfiguration.setStatusDescription(null);
        studyConfiguration.setStatus(Status.PROCESSING);
        updateStatus(studyConfiguration, listener);
        return studyConfiguration.getStatus();
    }

    /**
     * @return the caArrayFacade
     */
    public CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    /**
     * @param caArrayFacade the caArrayFacade to set
     */
    public void setCaArrayFacade(CaArrayFacade caArrayFacade) {
        this.caArrayFacade = caArrayFacade;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the bioconductorService
     */
    public BioconductorService getBioconductorService() {
        return bioconductorService;
    }

    /**
     * @param bioconductorService the bioconductorService to set
     */
    public void setBioconductorService(BioconductorService bioconductorService) {
        this.bioconductorService = bioconductorService;
    }

    /**
     * @return the dnaAnalysisHandlerFactory
     */
    public DnaAnalysisHandlerFactory getDnaAnalysisHandlerFactory() {
        return dnaAnalysisHandlerFactory;
    }

    /**
     * @param dnaAnalysisHandlerFactory the dnaAnalysisHandlerFactory to set
     */
    public void setDnaAnalysisHandlerFactory(DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory) {
        this.dnaAnalysisHandlerFactory = dnaAnalysisHandlerFactory;
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
     * @return the expressionHandlerFactory
     */
    public ExpressionHandlerFactory getExpressionHandlerFactory() {
        return expressionHandlerFactory;
    }

    /**
     * @param expressionHandlerFactory the expressionHandlerFactory to set
     */
    public void setExpressionHandlerFactory(ExpressionHandlerFactory expressionHandlerFactory) {
        this.expressionHandlerFactory = expressionHandlerFactory;
    }

}
