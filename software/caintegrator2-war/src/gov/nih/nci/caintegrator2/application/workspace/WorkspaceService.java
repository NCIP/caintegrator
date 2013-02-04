/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;
import gov.nih.nci.security.exceptions.CSException;

import java.util.List;
import java.util.Set;

/**
 * Provides <code>UserWorkspace</code> access and management functionality.
 */
public interface WorkspaceService {
    
    /**
     * Returns the workspace belonging to the current user.
     * 
     * @return the current user's workspace.
     */
    UserWorkspace getWorkspace();
    
    /**
     * Retrieves the studyConfigurationJobs for the user workspace.
     * 
     * @param userWorkspace workspace of the user.
     * @return all study configuraiton jobs for this users workspace.
     * @throws CSException if there's a problem accessing CSM.
    */
    Set<StudyConfiguration> retrieveStudyConfigurationJobs(UserWorkspace userWorkspace) 
        throws CSException;
   
   /**
    * Subscribes a user to a study.
     * 
     * @param workspace workspace of the user.
     * @param study - study to subscribe to.
     */
    void subscribe(UserWorkspace workspace, Study study);
        
    /**
     * Subscribe to all studies that the user has read access.
     * @param userWorkspace - object to use.
     */
    void subscribeAll(UserWorkspace userWorkspace);

    /**
     * Unsubscribes a user to a study.
     * 
     * @param workspace workspace of the user.
     * @param study - study to subscribe to.
     */
    void unsubscribe(UserWorkspace workspace, Study study);
    
    /**
     * Un-subscribes all users from the given study.
     * @param study to un-subscribe from.
     */
    void unsubscribeAll(Study study);

    /**
     * Saves the current changes.
     * @param workspace - object that needs to be updated.
     */
    void saveUserWorkspace(UserWorkspace workspace);
    
    /**
     * Get the Analysis Job.
     * @param id - id to be retrieved.
     * @return Analysis Job
     */
    AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id);
    
    /**
     * Saves the current changes.
     * @param job - object to be updated.
     */
    void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job);
    
    /**
     * Creates a <code> DisplayableStudySummary </code> from the given Study. 
     * @param study - object to use.
     * @return - DisplayableStudySummary object created from the study.
     */
    DisplayableStudySummary createDisplayableStudySummary(Study study);
    
    /**
     * 
     * @param geneList the gene list to create
     * @param geneSymbols the list of gene symbols
     */
    void createGeneList(GeneList geneList, List<String> geneSymbols);
    
    /**
     * Returns the refreshed entity attached to the current Hibernate session.
     * 
     * @param <T> type of object being returned.
     * @param entity a persistent entity with the id set.
     * @return the refreshed entity.
     */
    <T> T getRefreshedEntity(T entity);

}
