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
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class EditAnnotationGroupAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private FileManager fileManager;
    private File annotationGroupFile;
    private String annotationGroupFileContentType;
    private String annotationGrouptFileFileName;
    private AnnotationGroup annotationGroup = new AnnotationGroup();
    private String selectedAction;
    private String groupName;
    private List<DisplayableAnnotationFieldDescriptor> displayableFields =
        new ArrayList<DisplayableAnnotationFieldDescriptor>();
    private final List<AnnotationGroup> selectableAnnotationGroups = new ArrayList<AnnotationGroup>();
    private final Map<String, AnnotationGroup> annotationGroupNameToGroupMap = new HashMap<String, AnnotationGroup>();

    private static final String CANCEL_ACTION = "cancel";
    private static final String SAVE_ACTION = "save";
    private static final String ANNOTATION_GROUP_NAME = "groupName";

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (isExistingGroup()) {
            annotationGroup = getStudyManagementService().getRefreshedEntity(annotationGroup);
            setupAnnotationGroups();
            setupDisplayableFields();
        }
    }

    private void setupAnnotationGroups() {
        selectableAnnotationGroups.clear();
        List<AnnotationGroup> sortedAnnotationGroups = getStudy().getSortedAnnotationGroups();
        for (AnnotationGroup group : sortedAnnotationGroups) {
            group = getStudyManagementService().getRefreshedEntity(group);
            selectableAnnotationGroups.add(group);
            annotationGroupNameToGroupMap.put(group.getName(), group);
        }
    }

    private void setupDisplayableFields() {
        displayableFields.clear();
        for (AnnotationFieldDescriptor fieldDescriptor : annotationGroup.getAnnotationFieldDescriptors()) {
            displayableFields.add(new DisplayableAnnotationFieldDescriptor(fieldDescriptor));
        }
        Collections.sort(displayableFields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!CANCEL_ACTION.equals(selectedAction)) {
            prepareValueStack();
        }
        if (SAVE_ACTION.equals(selectedAction)) {
            clearErrorsAndMessages();
            validateAnnotationGroupName();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (CANCEL_ACTION.equals(selectedAction)) {
            return SUCCESS;
        } else if (SAVE_ACTION.equals(selectedAction)) {
            return save();
        }
        setGroupName(getAnnotationGroup().getName());
        return SUCCESS;
    }

    private void validateAnnotationGroupName() {
        if (StringUtils.isBlank(getGroupName())) {
            addFieldError(ANNOTATION_GROUP_NAME, getText("struts.messages.error.name.required",
                    getArgs("Annotation Group")));
        } else if (!getGroupName().equals(getAnnotationGroup().getName())
                && getStudy().getAnnotationGroup(getGroupName()) != null) {
            addFieldError(ANNOTATION_GROUP_NAME,
                    getText("struts.messages.error.duplicate.name", getArgs("Annotation Group", getGroupName())));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    /**
     * @return String.
     */
    public String save() {
        try {
            annotationGroup.setName(getGroupName());
            getStudyManagementService().saveAnnotationGroup(
                    annotationGroup, getStudyConfiguration(), annotationGroupFile);
            setStudyLastModifiedByCurrentUser(null,
                    LogEntry.getSystemLogSave(annotationGroup));
        } catch (ConnectionException e) {
            return errorSavingAnnotationGroup("Error connecting to caDSR: ");
        } catch (ValidationException e) {
            return errorSavingAnnotationGroup("Unable to save annotation group: " + e.getMessage());
        } catch (IOException e) {
            return errorSavingAnnotationGroup("Error reading upload file: " + e.getMessage());
        }
        return SUCCESS;
    }

    private String errorSavingAnnotationGroup(String errorMsg) {
        addActionError(errorMsg);
        annotationGroup = new AnnotationGroup();
        return ERROR;
    }

    /**
     * Saves field descriptors.
     * @return struts return value.
     */
    public String saveFieldDescriptors() {
        for (DisplayableAnnotationFieldDescriptor displayableFieldDescriptor : displayableFields) {
            if (displayableFieldDescriptor.isGroupChanged()) {
                displayableFieldDescriptor.getFieldDescriptor().switchAnnotationGroup(
                        annotationGroupNameToGroupMap.get(displayableFieldDescriptor.getAnnotationGroupName()));
            }
        }
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogSave(annotationGroup));
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
    }

    /**
     * Delete an annotation grouping.
     * @return string
     */
    public String delete() {
        setStudyLastModifiedByCurrentUser(null,
                LogEntry.getSystemLogDelete(annotationGroup));
        getStudyManagementService().delete(getStudyConfiguration(), annotationGroup);
        return SUCCESS;
    }

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the annotationGroup
     */
    public AnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * @param annotationGroup the annotationGroup to set
     */
    public void setAnnotationGroup(AnnotationGroup annotationGroup) {
        this.annotationGroup = annotationGroup;
    }

    /**
     * If group already exists.
     * @return t/f value.
     */
    public boolean isExistingGroup() {
        if (annotationGroup.getId() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the displayableFields
     */
    public List<DisplayableAnnotationFieldDescriptor> getDisplayableFields() {
        return displayableFields;
    }

    /**
     * @return the annotationGroupFile
     */
    public File getAnnotationGroupFile() {
        return annotationGroupFile;
    }

    /**
     * @param annotationGroupFile the annotationGroupFile to set
     */
    public void setAnnotationGroupFile(File annotationGroupFile) {
        this.annotationGroupFile = annotationGroupFile;
    }

    /**
     * @return the annotationGroupFileContentType
     */
    public String getAnnotationGroupFileContentType() {
        return annotationGroupFileContentType;
    }

    /**
     * @param annotationGroupFileContentType the annotationGroupFileContentType to set
     */
    public void setAnnotationGroupFileContentType(String annotationGroupFileContentType) {
        this.annotationGroupFileContentType = annotationGroupFileContentType;
    }

    /**
     * @return the annotationGrouptFileFileName
     */
    public String getAnnotationGrouptFileFileName() {
        return annotationGrouptFileFileName;
    }

    /**
     * @param annotationGrouptFileFileName the annotationGrouptFileFileName to set
     */
    public void setAnnotationGrouptFileFileName(String annotationGrouptFileFileName) {
        this.annotationGrouptFileFileName = annotationGrouptFileFileName;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = (groupName != null) ? groupName.trim() : groupName;
    }

    /**
     * @param displayableFields the displayableFields to set
     */
    public void setDisplayableFields(List<DisplayableAnnotationFieldDescriptor> displayableFields) {
        this.displayableFields = displayableFields;
    }


    /**
     * @return the selectableAnnotationGroups
     */
    public List<AnnotationGroup> getSelectableAnnotationGroups() {
        return selectableAnnotationGroups;
    }
}
