/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
