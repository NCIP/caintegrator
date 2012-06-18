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
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.CaIntegrator2BaseService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation entry point for the WorkspaceService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class WorkspaceServiceImpl extends CaIntegrator2BaseService implements WorkspaceService  {

    private static final int MAX_SUBJECT_IDENTIFIER_LEGNTH = 50;
    private SecurityManager securityManager;


    /**
     * {@inheritDoc}
     */
    @Override
    public UserWorkspace getWorkspace() {
        String username = SecurityHelper.getCurrentUsername();
        UserWorkspace userWorkspace = retrieveExistingUserWorkspace(username);
        if (userWorkspace == null) {
            userWorkspace = createUserWorkspace(username);
            saveUserWorkspace(userWorkspace);
        }
        return userWorkspace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserWorkspace getWorkspaceReadOnly() {
        return getWorkspace();
    }

    private UserWorkspace retrieveExistingUserWorkspace(String username) {
        return UserWorkspace.ANONYMOUS_USER_NAME.equals(username) ? null : getDao().getWorkspace(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshWorkspaceStudies(UserWorkspace workspace) {
        for (StudySubscription subscription : workspace.getSubscriptionCollection()) {
            subscription.setStudy(getRefreshedEntity(subscription.getStudy()));
            HibernateUtil.loadCollection(subscription.getStudy());
            subscription.getStudy().setStudyConfiguration(
                    getRefreshedEntity(subscription.getStudy().getStudyConfiguration()));
        }
    }

    private UserWorkspace createUserWorkspace(String username) {
        UserWorkspace userWorkspace;
        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(username);
        return userWorkspace;
    }

    /**
     * @param workspace saves workspace.
     */
    @Override
    public void saveUserWorkspace(UserWorkspace workspace) {
        if (!workspace.isAnonymousUser()) {
            getDao().save(workspace);
        } else {
            fillStudySubscriptionIds(workspace);
        }
    }

    private void fillStudySubscriptionIds(UserWorkspace workspace) {
        int number = 1;
        for (StudySubscription subscription : workspace.getSubscriptionCollection()) {
            subscription.setId(Long.valueOf(number++));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id) {
        return getDao().get(id, AbstractPersistedAnalysisJob.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job) {
        if (job.getId() == null) {
            getDao().save(job);
        } else {
            getDao().merge(job);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void subscribeAllReadOnly(UserWorkspace userWorkspace) {
        subscribeAll(userWorkspace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeAll(UserWorkspace userWorkspace) {
        List<Study> myStudies = new ArrayList<Study>();
        List<Study> publicStudies = getDao().getPublicStudies();
        if (userWorkspace.isAnonymousUser()) {
            myStudies.addAll(publicStudies);
        } else {
            myStudies.addAll(getDao().getStudies(userWorkspace.getUsername()));
            publicStudies.removeAll(myStudies);
        }
        myStudies.removeAll(publicStudies);
        Set<Study> allVisibleStudies = new HashSet<Study>(myStudies);
        allVisibleStudies.addAll(publicStudies);
        removeOldSubscriptions(userWorkspace, allVisibleStudies);
        subscribeAll(userWorkspace, myStudies, false);
        subscribeAll(userWorkspace, publicStudies, true);
    }

    private void subscribeAll(UserWorkspace userWorkspace, List<Study> studies, boolean isPublicSubscription) {
        for (Study study : studies) {
            subscribe(userWorkspace, study, isPublicSubscription);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<StudyConfiguration> retrieveStudyConfigurationJobs(UserWorkspace userWorkspace)
        throws CSException {
        Set<StudyConfiguration> results = securityManager.retrieveManagedStudyConfigurations(
                userWorkspace.getUsername(),
                getDao().getStudies(userWorkspace.getUsername()));
        updateStatus(results);
        return results;
    }

    private void updateStatus(Set<StudyConfiguration> studsyConfigurations) {
        for (StudyConfiguration studyConfiguration : studsyConfigurations) {
            if (Status.PROCESSING.equals(studyConfiguration.getStatus())
                    && DateUtil.isTimeout(studyConfiguration.getDeploymentStartDate(), DateUtil.FOURTY_EIGHT_HOURS)) {
                studyConfiguration.setStatus(Status.ERROR);
                studyConfiguration.setStatusDescription("Timeout after "
                        + DateUtil.FOURTY_EIGHT_HOURS + " hours.");
                getDao().save(studyConfiguration);
            }
        }
    }

    private void removeOldSubscriptions(UserWorkspace userWorkspace, Collection<Study> myStudies) {
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
    @Override
    public void subscribe(UserWorkspace workspace, Study study, boolean isPublicSubscription) {
        if (!isSubscribed(workspace, study)) {
            StudySubscription subscription = new StudySubscription();
            subscription.setStudy(study);
            subscription.setPublicSubscription(isPublicSubscription);
            workspace.getSubscriptionCollection().add(subscription);
            saveUserWorkspace(workspace);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribe(UserWorkspace workspace, Study study) {
        for (StudySubscription subscription : workspace.getSubscriptionCollection()) {
            if (subscription.getStudy().equals(study)) {
                unsubscribe(workspace, subscription);
                return;
            }
        }
    }

    private void unsubscribe(UserWorkspace workspace, StudySubscription subscription) {
        workspace.getSubscriptionCollection().remove(subscription);
        if (workspace.getDefaultSubscription() != null
                && workspace.getDefaultSubscription().equals(subscription)) {
            workspace.setDefaultSubscription(null);
        }
        saveUserWorkspace(workspace);
        if (!workspace.isAnonymousUser()) {
            getDao().delete(subscription);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribeAll(Study study) {
        List<UserWorkspace> workspaces = getDao().retrieveAllSubscribedWorkspaces(study);
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
    @Override
    public Set<Platform> retrievePlatformsInStudy(Study study) {
        Set<Platform> allPlatforms = new HashSet<Platform>();
        for (GenomicDataSourceConfiguration genomicSource : study.getStudyConfiguration().getGenomicDataSources()) {
            allPlatforms.addAll(getDao().retrievePlatformsForGenomicSource(genomicSource));
        }
        return allPlatforms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                            + getDao().retrieveNumberImages(imageSeriesAcquisition.getSeriesCollection()));
                }
            }
            studySummary.getImageDataSources().add(displayableImageSource);
        }
    }

    private void addGenomicData(DisplayableStudySummary studySummary) {
        for (GenomicDataSourceConfiguration genomicConfig
                : studySummary.getStudy().getStudyConfiguration().getGenomicDataSources()) {
            DisplayableGenomicSource displayableGenomicSource = new DisplayableGenomicSource(genomicConfig);
            List<Platform> platforms = getDao().retrievePlatformsForGenomicSource(genomicConfig);
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
    @Override
    public void createGeneList(GeneList geneList, Set<String> geneSymbols) {
        for (String symbol : geneSymbols) {
            geneList.getGeneCollection().add(getDao().lookupOrCreateGene(symbol));
        }
        saveAbstractList(geneList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSubjectList(SubjectList subjectList, Set<String> subjects) throws ValidationException {
        for (String subject : subjects) {
            if (MAX_SUBJECT_IDENTIFIER_LEGNTH < subject.length()) {
                throw new ValidationException("The identifier '" + subject + "' exceeds the maximum length of "
                        + MAX_SUBJECT_IDENTIFIER_LEGNTH);
            }
            subjectList.getSubjectIdentifiers().add(new SubjectIdentifier(subject));
        }
        saveAbstractList(subjectList);
    }

    private void saveAbstractList(AbstractList abstractList) {
        if (Visibility.GLOBAL.equals(abstractList.getVisibility())) {
            abstractList.getStudyConfiguration().getListCollection().add(abstractList);
        } else {
            abstractList.getSubscription().getListCollection().add(abstractList);
        }
        saveUserWorkspace(getWorkspace());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeListGlobal(AbstractList abstractList) {
        abstractList.setVisibility(Visibility.GLOBAL);
        abstractList.getSubscription().getListCollection().remove(abstractList);
        abstractList.getStudyConfiguration().getListCollection().add(abstractList);
        UserWorkspace userWorkspace = abstractList.getSubscription().getUserWorkspace();
        abstractList.setSubscription(null);
        saveUserWorkspace(userWorkspace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeListPrivate(AbstractList abstractList) {
        abstractList.setVisibility(Visibility.PRIVATE);
        abstractList.getStudyConfiguration().getListCollection().remove(abstractList);
        abstractList.getSubscription().getListCollection().add(abstractList);
        abstractList.setStudyConfiguration(null);
        saveUserWorkspace(abstractList.getSubscription().getUserWorkspace());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAbstractList(StudySubscription subscription, AbstractList abstractList) {
        if (abstractList instanceof SubjectList) {
            for (Query query : subscription.getQueryCollection()) {
                Set<SubjectListCriterion> subjectListCriteria =
                    QueryUtil.getCriterionTypeFromQuery(query, SubjectListCriterion.class);
                for (SubjectListCriterion subjectListCriterion : subjectListCriteria) {
                    subjectListCriterion.getSubjectListCollection().remove(abstractList);
                }
            }
        }
        if (Visibility.GLOBAL.equals(abstractList.getVisibility())) {
            abstractList.getStudyConfiguration().getListCollection().remove(abstractList);
        } else {
            abstractList.getSubscription().getListCollection().remove(abstractList);
        }
        getDao().delete(abstractList);
        saveUserWorkspace(getWorkspace());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearSession() {
        getDao().clearSession();
    }

}
