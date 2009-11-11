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
