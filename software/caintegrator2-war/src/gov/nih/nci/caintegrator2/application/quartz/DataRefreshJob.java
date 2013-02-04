/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.quartz;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quartz job to check caArray experiments for updated data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DataRefreshJob extends QuartzJobBean implements Runnable {
    private static final Logger LOG = Logger.getLogger(DataRefreshJob.class);
    private CaArrayFacade caArrayFacade;
    private CaIntegrator2Dao dao;
    private String user;
    private boolean singleSignOnInstallation = false;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!isSingleSignOnInstallation()) {
            List<GenomicDataSourceConfiguration> dataSources = dao.getAllGenomicDataSources();
            for (GenomicDataSourceConfiguration dataSource : dataSources) {
                handleModification(dataSource);
            }
            dao.markStudiesAsNeedingRefresh();
            LOG.info("Data Refresh Job successfully executed.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        List<Study> configs = dao.getStudies(user);
        List<GenomicDataSourceConfiguration> dataSources = new ArrayList<GenomicDataSourceConfiguration>();
        for (Study study : configs) {
            dataSources.addAll(study.getStudyConfiguration().getGenomicDataSources());
        }
        for (GenomicDataSourceConfiguration gds : dataSources) {
            handleModification(gds);
        }
        dao.markStudiesAsNeedingRefresh();
        LOG.info("SSO Data Refresh Job successfully completed for " + user);
    }

    /**
     * Kicks off the thread.
     */
    public void startTask() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * @param dataSource
     */
    private void handleModification(GenomicDataSourceConfiguration dataSource) {
        try {
            Date lastModifiedDate = caArrayFacade.getLastDataModificationDate(dataSource);
            boolean modified = lastModifiedDate != null && lastModifiedDate.after(dataSource.getLastModifiedDate());
            dataSource.setDataRefreshed(modified);
            dao.save(dataSource);
        } catch (ExperimentNotFoundException e) {
            LOG.error("Unable to find experiment " + dataSource.getExperimentIdentifier(), e);
        } catch (ConnectionException e) {
            LOG.error("Error connecting to data source for experiment " + dataSource.getExperimentIdentifier(), e);
        } catch (Exception e) {
            LOG.error("An unexpected error has occurred while trying to handle experiment "
                    + dataSource.getExperimentIdentifier(), e);
        }
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }

    /**
     * @param caArrayFacade the caArrayFacade to set
     */
    public void setCaArrayFacade(CaArrayFacade caArrayFacade) {
        this.caArrayFacade = caArrayFacade;
    }

    /**
     * @return the singleSignOnInstallation
     */
    public boolean isSingleSignOnInstallation() {
        return singleSignOnInstallation;
    }

    /**
     * @param singleSignOnInstallation the singleSignOnInstallation to set
     */
    public void setSingleSignOnInstallation(boolean singleSignOnInstallation) {
        this.singleSignOnInstallation = singleSignOnInstallation;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }
}
