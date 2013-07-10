/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.web.DownloadableFile;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator.web.ajax.PersistedAnalysisJobAjaxUpdater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class DownloadAnalysisResultsAction  extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;
    private static final String DOWNLOAD_RESULTS_FILE = "downloadResultFile";

    private StudyManagementService studyManagementService;
    private AbstractPersistedAnalysisJob job;
    private Long jobId;
    private String type;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setJob(getWorkspaceService().getPersistedAnalysisJob(jobId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (jobId == null) {
            addActionError("No job id for GISTIC specified.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        DownloadableFile downloadableFile = new DownloadableFile();
        downloadableFile.setContentType(DownloadableFile.CONTENT_TYPE_ZIP);
        downloadableFile.setDeleteFile(false);
        downloadableFile.setFilename(getFilenamePrefix() + type + ".zip");
        if (PersistedAnalysisJobAjaxUpdater.DownloadType.RESULTS.getType().equals(type)) {
            return handleResultDownload(downloadableFile);
        } else if (PersistedAnalysisJobAjaxUpdater.DownloadType.INPUT.getType().equals(type)) {
            return handleInputDownload(downloadableFile);
        }
        addActionError(getText("struts.messages.error.must.specify.file.type"));
        return INPUT;

    }

    private String handleInputDownload(DownloadableFile downloadableFile) {

        if (job.getInputZipFile() != null) {
            downloadableFile.setPath(job.getInputZipFile().getPath());
            getDisplayableWorkspace().setTemporaryDownloadFile(downloadableFile);
            return DOWNLOAD_RESULTS_FILE;
        }
        addActionError(getText("struts.messages.error.no.input.file"));
        return INPUT;
    }

    private String handleResultDownload(DownloadableFile downloadableFile) {
        if (job.getResultsZipFile() != null) {
            downloadableFile.setPath(job.getResultsZipFile().getPath());
            getDisplayableWorkspace().setTemporaryDownloadFile(downloadableFile);
            return DOWNLOAD_RESULTS_FILE;
        }
        addActionError(getText("struts.messages.error.no.results.file"));
        return INPUT;
    }

    private String getFilenamePrefix() {
        switch(job.getJobType()) {
        case CMS:
            return "cms";
        case GENE_PATTERN:
            return "genePattern";
        case PCA:
            return "pca";
        case GISTIC:
            return "gistic";
        default:
            throw new IllegalStateException("Job type unknown");
        }
    }

    /**
     * @return the jobId
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    @Autowired
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }

    /**
     * @return the job
     */
    public AbstractPersistedAnalysisJob getJob() {
        return job;
    }

    /**
     * @param job the job to set
     */
    public void setJob(AbstractPersistedAnalysisJob job) {
        this.job = job;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
