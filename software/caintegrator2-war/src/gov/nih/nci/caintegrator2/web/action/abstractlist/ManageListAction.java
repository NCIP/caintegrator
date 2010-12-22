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
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Provides functionality to list and add array designs.
 */
@SuppressWarnings("PMD") // See createPlatform method
public class ManageListAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;
    private FileManager fileManager;
    private File listFile;
    private String listFileContentType;
    private String listFileFileName;
    private ListTypeEnum listType = ListTypeEnum.GENE;
    private String listName;
    private String description;
    private boolean visibleToOther = false;
    private String subjectInputElements;
    private String geneInputElements;
    private String selectedAction;
    private String listId;
    private Set<String> elementList = new HashSet<String>();

    private static final String CREATE_LIST_ACTION = "createList";
    private static final String CANCEL_ACTION = "cancel";
    private static final String LIST_NAME = "listName";
    private static final String LIST_FILE = "listFile";
    private static final String EDIT_GENE_PAGE = "editGenePage";
    private static final String EDIT_GLOBAL_GENE_PAGE = "editGlobalGenePage";
    private static final String EDIT_SUBJECT_PAGE = "editSubjectPage";
    private static final String EDIT_GLOBAL_SUBJECT_PAGE = "editGlobalSubjectPage";
    private static final String HOME_PAGE = "homePage";

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        setInvalidDataBeingAccessed(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }
    
    /**
     * @return the Struts result.
     */
    public String execute() {
        if (CREATE_LIST_ACTION.equals(selectedAction)) {
            try {
                return createList();
            } catch (ValidationException e) {
                addActionError(e.getMessage());
                return INPUT;
            }
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            return HOME_PAGE;
        }
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        prepareValueStack();
        if (getCurrentStudy() == null) {
            setInvalidDataBeingAccessed(true);
        }
        if (CREATE_LIST_ACTION.equalsIgnoreCase(selectedAction)) {
            validateForCreate();
        } else {
            super.validate();
        }
    }

    private void validateForCreate() {
        validateListName();
        validateFileType();
        if (!hasActionErrors() && !hasFieldErrors()) {
            validateListData();
        }
    }
    
    private void validateFileType() {
        if (StringUtils.isNotBlank(listFileContentType) && !listFileContentType.startsWith("text/")
                && !getListFileFileName().endsWith(".csv")) {
            addFieldError(LIST_FILE, getText("struts.messages.error.content.type.not.allowed", 
                    getArgs("", "", "", listFileContentType)));
        }
    }

    private void validateListName() {
        if (StringUtils.isEmpty(getListName())) {
            addFieldError(LIST_NAME, getText("struts.messages.error.name.required", getArgs("List")));
        } else if (duplicateListName()) {
            addFieldError(LIST_NAME, getText("struts.messages.error.duplicate.name", getArgs("List", getListName())));
        }
    }
    
    private boolean duplicateListName() {
        if (ListTypeEnum.GENE.equals(listType)) {
            if ((!isVisibleToOther() && getStudySubscription().getGeneList(getListName()) != null)
                || (isVisibleToOther() && getStudy().getStudyConfiguration().getGeneList(getListName()) != null)) {
                return true;
            }
        } else {
            if ((!isVisibleToOther() && getStudySubscription().getSubjectList(getListName()) != null)
                || (isVisibleToOther() && getStudy().getStudyConfiguration().getSubjectList(getListName()) != null)) {
                return true;
            }
        }
        return false;
    }

    private void validateListData() {
        elementList.clear();
        if (ListTypeEnum.GENE.equals(listType)) {
            extractInputElements(getGeneInputElements());
        } else {
            extractInputElements(getSubjectInputElements());
        }
        extractInputElements(getListFile());
        if (elementList.isEmpty()) {
            addActionError(getText("struts.messages.error.list.nothing.to.save"));
        }
    }
    
    private void extractInputElements(String inputElements) {
        if (!StringUtils.isEmpty(inputElements)) {
            for (String element : inputElements.split(",")) {
                elementList.add(element.trim());
            }
        }
    }
    
    private void extractInputElements(File uploadFile) {
        if (uploadFile != null) {
            CSVReader reader;
            try {
                reader = new CSVReader(new FileReader(uploadFile));
                String[] elements;
                while ((elements = reader.readNext()) != null) {
                    for (String element : elements) {
                        String trimmedElement = element.trim();
                        if (trimmedElement.contains("\t")) {
                            addFieldError(LIST_FILE, getText("struts.messages.error.file.tab.should.be.comma", 
                                    getArgs("")));
                            return;
                        }
                        elementList.add(element.trim()); 
                    }
                }
                if (elementList.isEmpty()) {
                    addFieldError(LIST_FILE, getText("struts.messages.error.file.empty", getArgs("")));
                }
            } catch (IOException e) {
                addFieldError(LIST_FILE, getText("struts.messages.error.file.read", getArgs("list")));
            }
        }
    }

    /**
     * @return SUCCESS
     * @throws ValidationException 
     */
    private String createList() throws ValidationException {
        AbstractList newList = (ListTypeEnum.GENE.equals(listType)) ? new GeneList() : new SubjectList();
        newList.setName(getListName());
        newList.setDescription(getDescription());
        newList.setLastModifiedDate(new Date());
        if (isVisibleToOther()) {
            newList.setVisibility(Visibility.GLOBAL);
            newList.setStudyConfiguration(getSubscription().getStudy().getStudyConfiguration());
        } else {
            newList.setVisibility(Visibility.PRIVATE);
            newList.setSubscription(getSubscription());
        }
        if (ListTypeEnum.GENE.equals(listType)) {
            getWorkspaceService().createGeneList((GeneList) newList, elementList);
            return (isVisibleToOther()) ? EDIT_GLOBAL_GENE_PAGE : EDIT_GENE_PAGE;
        } else {
            getWorkspaceService().createSubjectList((SubjectList) newList, elementList);
            return (isVisibleToOther()) ? EDIT_GLOBAL_SUBJECT_PAGE : EDIT_SUBJECT_PAGE;
        }
    }

    /**
     * @return the listFile
     */
    public File getListFile() {
        return listFile;
    }

    /**
     * @param listFile the geneListFile to set
     */
    public void setListFile(File listFile) {
        this.listFile = listFile;
    }

    /**
     * @return the listFileContentType
     */
    public String getListFileContentType() {
        return listFileContentType;
    }

    /**
     * @param listFileContentType the listFileContentType to set
     */
    public void setListFileContentType(String listFileContentType) {
        this.listFileContentType = listFileContentType;
    }

    /**
     * @return the listFileFileName
     */
    public String getListFileFileName() {
        return listFileFileName;
    }

    /**
     * @param listFileFileName the listFileFileName to set
     */
    public void setListFileFileName(String listFileFileName) {
        this.listFileFileName = listFileFileName;
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
     * @return the listName
     */
    public String getListName() {
        return listName;
    }

    /**
     * @param listName the listName to set
     */
    public void setListName(String listName) {
        this.listName = listName;
    }

    /**
     * @return the listId
     */
    public String getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(String listId) {
        this.listId = listId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the elementList
     */
    public Set<String> getElementList() {
        return elementList;
    }
    
    /**
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }

    /**
     * @return the visibleToOther
     */
    public boolean isVisibleToOther() {
        return visibleToOther;
    }

    /**
     * @param visibleToOther the visibleToOther to set
     */
    public void setVisibleToOther(boolean visibleToOther) {
        this.visibleToOther = visibleToOther;
    }

    /**
     * @return the listType
     */
    public String getListType() {
        return listType.getValue();
    }

    /**
     * @param listType the listType to set
     */
    public void setListType(String listType) {
        this.listType = ListTypeEnum.getByValue(listType);
    }

    /**
     * @return the subjectInputElements
     */
    public String getSubjectInputElements() {
        return subjectInputElements;
    }

    /**
     * @param subjectInputElements the subjectInputElements to set
     */
    public void setSubjectInputElements(String subjectInputElements) {
        this.subjectInputElements = subjectInputElements;
    }

    /**
     * @return the geneInputElements
     */
    public String getGeneInputElements() {
        return geneInputElements;
    }

    /**
     * @param geneInputElements the geneInputElements to set
     */
    public void setGeneInputElements(String geneInputElements) {
        this.geneInputElements = geneInputElements;
    }
}
