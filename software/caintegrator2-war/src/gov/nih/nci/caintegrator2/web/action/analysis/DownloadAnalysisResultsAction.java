/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobTypeEnum;
import gov.nih.nci.caintegrator2.web.DownloadableFile;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.ajax.PersistedAnalysisJobAjaxUpdater;

/**
 * 
 */
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
        addActionError("Must specify the type of file being downloaded.");
        return INPUT;
        
    }

    private String handleInputDownload(DownloadableFile downloadableFile) {
        
        if (job.getInputZipFile() != null) {
            downloadableFile.setPath(job.getInputZipFile().getPath());
            getDisplayableWorkspace().setTemporaryDownloadFile(downloadableFile);
            return DOWNLOAD_RESULTS_FILE;
        }
        addActionError("The job doens't contain an input file");
        return INPUT;
    }

    private String handleResultDownload(DownloadableFile downloadableFile) {
        if (job.getResultsZipFile() != null) {
            downloadableFile.setPath(job.getResultsZipFile().getPath());
            getDisplayableWorkspace().setTemporaryDownloadFile(downloadableFile);
            return DOWNLOAD_RESULTS_FILE;
        } 
        addActionError("The job doesn't contain a results file");
        return INPUT;
    }
    
    private String getFilenamePrefix() {
        switch(AnalysisJobTypeEnum.getByValue(job.getJobType())) {
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
