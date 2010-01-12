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

import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import org.apache.commons.lang.StringUtils;

/**
 * Provides functionality to list and add array designs.
 */
@SuppressWarnings("PMD") // See createPlatform method
public abstract class EditAbstractListAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private String listOldName;
    private String listName;
    private String selectedAction;
    private boolean editOn = true;

    static final String EDIT_ACTION = "editList";
    static final String DELETE_ACTION = "deleteList";
    static final String RENAME_ACTION = "renameList";
    static final String LIST_NAME = "listName";
    static final String HOME_PAGE = "homePage";
    static final String EDIT_PAGE = "editPage";
    
    /**
     * @return the Struts result.
     */
    public String execute() {
        if (EDIT_ACTION.equals(selectedAction)) {
            return editList();
        } else if (DELETE_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return deleteList();
        } else if (RENAME_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return renameList();
        }
        return SUCCESS;
    }
    
    abstract AbstractList getAbstractList(String name);
    
    /**
     * Go to the edit page.
     * @return the Struts result.
     */
    public String editList() {
        setListOldName(getListName());
        return SUCCESS;
    }
    
    /**
     * Rename the subject list.
     * @return the Struts result.
     */
    public String renameList() {
        AbstractList list = getAbstractList(listOldName);
        if (list != null) {
            list.setName(listName);
            getWorkspaceService().saveUserWorkspace(getWorkspace());
            listOldName = listName;
            return EDIT_PAGE;
        } else {
            listNoLongerAvailable();
            return SUCCESS;
        }
    }
    
    /**
     * Delete the subject list.
     * @return the Struts result.
     */
    public String deleteList() {
        AbstractList list = getAbstractList(listName);
        if (list != null) {
            getWorkspaceService().deleteAbstractList(list);
            return HOME_PAGE;
        } else {
            listNoLongerAvailable();
            return SUCCESS;
        }
    }
    
    private void listNoLongerAvailable() {
        addActionError("The subject list '" + listOldName + "' is no longer available.");
        editOn = false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if (EDIT_ACTION.equalsIgnoreCase(selectedAction)) {
            prepareValueStack();
            validateList();
        } else if (RENAME_ACTION.equalsIgnoreCase(selectedAction)) {
            prepareValueStack();
            validateListName();
        }
    }
    
    private void validateList() {
        if (getListName() == null) {
            addActionError("The requested list '" + getListName() + "' doesn't exist.");
            editOn = false;
        }
    }
    
    private void validateListName() {
        if (StringUtils.isEmpty(getListName())) {
            addFieldError(LIST_NAME, "List name is required");
        } else if (getAbstractList(listName) != null) {
            addFieldError(LIST_NAME, "List name is duplicate: " + getListName());
            setListName(listOldName);
        }
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
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }

    /**
     * @return the listOldName
     */
    public String getListOldName() {
        return listOldName;
    }

    /**
     * @param listOldName the listOldName to set
     */
    public void setListOldName(String listOldName) {
        this.listOldName = listOldName;
    }

    /**
     * @return the editOn
     */
    public boolean isEditOn() {
        return editOn;
    }
}
