/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.web.ajax.ISubjectDataSourceAjaxUpdater;
import gov.nih.nci.caintegrator.web.ajax.SubjectDataSourceAjaxRunner;

/**
 * Action called to edit an existing clinical data source.
 */
public class LoadClinicalSourceAction extends AbstractClinicalSourceAction {

    private static final long serialVersionUID = 1L;
    private ISubjectDataSourceAjaxUpdater updater;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        getClinicalSource().setStatus(Status.PROCESSING);
        setStudyLastModifiedByCurrentUser(getClinicalSource(), 
                LogEntry.getSystemLogLoadSubjectAnnotationFile(
                        getClinicalSource().getAnnotationFile().getFile().getName()));
        updater.runJob(getStudyConfiguration().getId(), getClinicalSource().getId(), 
                SubjectDataSourceAjaxRunner.JobType.LOAD);
        return SUCCESS;
    }
    
    /**
     * Reload a clinical source file.
     * @return string
     */
    public String reLoad() {
        getClinicalSource().setStatus(Status.PROCESSING);
        setStudyLastModifiedByCurrentUser(getClinicalSource(), 
                LogEntry.getSystemLogLoadSubjectAnnotationFile(
                        getClinicalSource().getAnnotationFile().getFile().getName()));
        updater.runJob(getStudyConfiguration().getId(), getClinicalSource().getId(), 
                SubjectDataSourceAjaxRunner.JobType.RELOAD);
        return SUCCESS;
    }
    
    /**
     * Delete a clinical source file.
     * @return string
     */
    public String delete() {
        getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
        getClinicalSource().setStatus(Status.PROCESSING);
        setStudyLastModifiedByCurrentUser(getClinicalSource(),
                LogEntry.getSystemLogDeleteSubjectAnnotationFile(
                        getClinicalSource().getAnnotationFile().getFile().getName()));
        updater.runJob(getStudyConfiguration().getId(), getClinicalSource().getId(), 
                SubjectDataSourceAjaxRunner.JobType.DELETE);
        return SUCCESS;
    }

    /**
     * @return the updater
     */
    public ISubjectDataSourceAjaxUpdater getUpdater() {
        return updater;
    }

    /**
     * @param updater the updater to set
     */
    public void setUpdater(ISubjectDataSourceAjaxUpdater updater) {
        this.updater = updater;
    }
    
}
