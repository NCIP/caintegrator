/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.security.SecurityHelper;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.caintegrator2.web.DisplayableGenomicSource;
import gov.nih.nci.caintegrator2.web.DisplayableImageSource;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation entry point for the WorkspaceService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class WorkspaceServiceImpl implements WorkspaceService {
    
    private CaIntegrator2Dao dao;
    private SecurityManager securityManager;
    
    private static final int TWELVE_HOURS = 12;

    /**
     * {@inheritDoc}
     */
    public UserWorkspace getWorkspace() {
        String username = SecurityHelper.getCurrentUsername();
        UserWorkspace userWorkspace = dao.getWorkspace(username);
        if (userWorkspace == null) {
            userWorkspace = createUserWorkspace(username);
            saveUserWorkspace(userWorkspace);
        }
        return userWorkspace;
    }

    private UserWorkspace createUserWorkspace(String username) {
        UserWorkspace userWorkspace;
        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(username);
        userWorkspace.setSubscriptionCollection(new HashSet<StudySubscription>());
        return userWorkspace;
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
     * @param workspace saves workspace.
     */
    public void saveUserWorkspace(UserWorkspace workspace) {
        dao.save(workspace);
    }

    /**
     * {@inheritDoc}
     */
    public AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id) {
        return getDao().get(id, AbstractPersistedAnalysisJob.class);
    }
    
    /**
     * {@inheritDoc}
     */    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job) {
        if (job.getId() == null) {
            dao.save(job);
        } else {
            dao.merge(job);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void subscribeAll(UserWorkspace userWorkspace) {
        List<Study> myStudies = dao.getStudies(userWorkspace.getUsername());
        removeOldSubscriptions(userWorkspace, myStudies);
        subscribeAll(userWorkspace, myStudies);
    }
    
    private void subscribeAll(UserWorkspace userWorkspace, List<Study> myStudies) {
        for (Study study : myStudies) {
            subscribe(userWorkspace, study);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<StudyConfiguration> retrieveStudyConfigurationJobs(UserWorkspace userWorkspace) 
        throws CSException {
        Set<StudyConfiguration> results = securityManager.retrieveManagedStudyConfigurations(
                userWorkspace.getUsername(), 
                        dao.getStudies(userWorkspace.getUsername()));
        updateStatus(results);
        return results;
    }
    
    private void updateStatus(Set<StudyConfiguration> studsyConfigurations) {
        for (StudyConfiguration studyConfiguration : studsyConfigurations) {
            if (Status.PROCESSING.equals(studyConfiguration.getStatus())
                    && isTimeout(studyConfiguration.getDeploymentStartDate())) {
                studyConfiguration.setStatus(Status.ERROR);
                studyConfiguration.setStatusDescription("TImeout after 12 hours");
                dao.save(studyConfiguration);
            }
        }
    }
    
    private boolean isTimeout(Date date) {
        return DateUtils.addHours(date, TWELVE_HOURS).before(new Date());
    }
    
    private void removeOldSubscriptions(UserWorkspace userWorkspace, List<Study> myStudies) {
        List<Study> oldStudies = new ArrayList<Study>();
        for (StudySubscription studySubscription : userWorkspace.getSubscriptionCollection()) {
            if (!myStudies.contains(studySubscription.getStudy())) {
                oldStudies.add(studySubscription.getStudy());
            }
        }
        for (Study study : oldStudies) {
            unsubscribe(userWorkspace, study);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void subscribe(UserWorkspace workspace, Study study) {
        if (!isSubscribed(workspace, study)) {
            StudySubscription subscription = new StudySubscription();
            subscription.setStudy(study);
            workspace.getSubscriptionCollection().add(subscription);
            saveUserWorkspace(workspace);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unsubscribe(UserWorkspace workspace, Study study) {
        for (StudySubscription subscription : workspace.getSubscriptionCollection()) {
            if (subscription.getStudy().equals(study)) {
                workspace.getSubscriptionCollection().remove(subscription);
                if (workspace.getDefaultSubscription() != null
                        && workspace.getDefaultSubscription().equals(subscription)) {
                    workspace.setDefaultSubscription(null);
                }
                saveUserWorkspace(workspace);
                dao.delete(subscription);
                return;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void unsubscribeAll(Study study) {
        List<UserWorkspace> workspaces = dao.retrieveAllSubscribedWorkspaces(study);
        if (workspaces != null && !workspaces.isEmpty()) {
            for (UserWorkspace workspace : workspaces) {
                unsubscribe(workspace, study);
            }
        }
    }

    private boolean isSubscribed(UserWorkspace workspace, Study study) {
        for (StudySubscription subscription : workspace.getSubscriptionCollection()) {
            if (subscription.getStudy().equals(study)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public <T> T getRefreshedEntity(T entity) {
        Long id;
        try {
            id = (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity doesn't have a getId() method", e);
        }
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        return (T) dao.get(id, entity.getClass());
    }

    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public DisplayableStudySummary createDisplayableStudySummary(Study study) {
        DisplayableStudySummary studySummary = new DisplayableStudySummary(study);
        if (studySummary.isImagingStudy()) {
            addImagingData(studySummary);
        }
        if (studySummary.isGenomicStudy()) {
            addGenomicData(studySummary);
        }
        return studySummary;
    }

    private void addImagingData(DisplayableStudySummary studySummary) {
        for (ImageDataSourceConfiguration imageDataSource
                : studySummary.getStudy().getStudyConfiguration().getImageDataSources()) {
            DisplayableImageSource displayableImageSource = new DisplayableImageSource(imageDataSource);
            for (ImageSeriesAcquisition imageSeriesAcquisition : imageDataSource.getImageSeriesAcquisitions()) {
                if (imageSeriesAcquisition.getAssignment() != null) {
                    displayableImageSource.setNumberImageStudies(displayableImageSource.getNumberImageStudies() + 1);
                    displayableImageSource.setNumberImageSeries(displayableImageSource.getNumberImageSeries()
                            + imageSeriesAcquisition.getSeriesCollection().size());
                    displayableImageSource.setNumberImages(displayableImageSource.getNumberImages()
                            + dao.retrieveNumberImages(imageSeriesAcquisition.getSeriesCollection()));
                }
            }
            studySummary.getImageDataSources().add(displayableImageSource);
        }
    }
    
    private void addGenomicData(DisplayableStudySummary studySummary) {
        for (GenomicDataSourceConfiguration genomicConfig  
                : studySummary.getStudy().getStudyConfiguration().getGenomicDataSources()) {
            DisplayableGenomicSource displayableGenomicSource = new DisplayableGenomicSource(genomicConfig);
            List<Platform> platforms = dao.retrievePlatformsForGenomicSource(genomicConfig);
            if (platforms != null && !platforms.isEmpty()) {
                displayableGenomicSource.getPlatforms().addAll(platforms);
            }
            studySummary.getGenomicDataSources().add(displayableGenomicSource);
        }
    }

    /**
     * @return the securityManager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * {@inheritDoc}
     */
    public void createGeneList(GeneList geneList, List<String> geneSymbols) {
        for (String symbol : geneSymbols) {
            geneList.getGeneCollection().add(lookupOrCreateGene(symbol));
        }
        geneList.getSubscription().getListCollection().add(geneList);
        saveUserWorkspace(geneList.getSubscription().getUserWorkspace());
    }
    
    private Gene lookupOrCreateGene(String symbol) {
        Gene gene = getDao().getGene(symbol);
        if (gene == null) {
            gene = new Gene();
            gene.setSymbol(symbol);
        }
        return gene;
    }

}
