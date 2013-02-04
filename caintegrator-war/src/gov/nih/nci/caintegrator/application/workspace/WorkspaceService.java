/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.workspace;

import gov.nih.nci.caintegrator.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.domain.application.AbstractList;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.DisplayableStudySummary;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Set;

/**
 * Provides <code>UserWorkspace</code> access and management functionality.
 */
public interface WorkspaceService extends CaIntegrator2EntityRefresher {
    
    /**
     * Returns the workspace belonging to the current user.
     * 
     * @return the current user's workspace.
     */
    UserWorkspace getWorkspace();
    
    /**
     * Returns the users workspace (read only, for anonymous user which won't be saved).
     * @return the anonymous user's workspace.
     */
    UserWorkspace getWorkspaceReadOnly();
    
    /**
     * Refreshes studies for hibernate.
     * @param workspace to refresh studies for.
     */
    void refreshWorkspaceStudies(UserWorkspace workspace);
    
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
     * @param isPublicSubscription - determines if it's a public subscription or not.
     */
    void subscribe(UserWorkspace workspace, Study study, boolean isPublicSubscription);
        
    /**
     * Subscribe to all studies that the user has read access.
     * @param userWorkspace - object to use.
     */
    void subscribeAll(UserWorkspace userWorkspace);

    /**
     * Subscribe to all studies for anonymous user.
     * @param userWorkspace - object to use.
     */
    void subscribeAllReadOnly(UserWorkspace userWorkspace);
    
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
     * Retrieve all platforms from a study.
     * @param study - object to use.
     * @return a set of platform from the study.
     */
    Set<Platform> retrievePlatformsInStudy(Study study);
    
    /**
     * Creates a <code> DisplayableStudySummary </code> from the given Study. 
     * @param study - object to use.
     * @return - DisplayableStudySummary object created from the study.
     */
    DisplayableStudySummary createDisplayableStudySummary(Study study);
    
    /**
     * @param geneList the gene list to create
     * @param geneSymbols the list of gene symbols
     */
    void createGeneList(GeneList geneList, Set<String> geneSymbols);
    
    /**
     * @param subjectList the subject list to create
     * @param subjects the set of subject identifier
     * @throws ValidationException if identifier is too long.
     */
    void createSubjectList(SubjectList subjectList, Set<String>subjects) throws ValidationException;

    /**
     * @param abstractList the list to make global
     */
    void makeListGlobal(AbstractList abstractList);

    /**
     * @param abstractList the list to make private
     */
    void makeListPrivate(AbstractList abstractList);

    /**
     * @param subscription the study subscription that request this action.
     * @param abstractList to delete from the workspace.
     */
    void deleteAbstractList(StudySubscription subscription, AbstractList abstractList);
    
    /**
     * Clears the current session.
     */
    void clearSession();

}
