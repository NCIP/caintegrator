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
package gov.nih.nci.caintegrator2.web.action.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.web.action.SecurityHelper;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.translational.Study;


/**
 * Helper class for ManageQueryAction.
 */
public class ManageQueryHelper {
    private boolean advancedView = false;
    // The below list contains all the user's query criteria 
    // (one object per row).
    private List<QueryAnnotationCriteria> queryCriteriaRowList;
    // The first three Collections below store data type specific
    // annotation definitions as stored in the DB.
    private Collection<AnnotationDefinition> clinicalAnnotationDefinitions;
    private Collection<AnnotationDefinition> sampleAnnotationDefinitions;
    private Collection<AnnotationDefinition> imageAnnotationDefinitions;
    // The objects below store annotation specific presentation-facing and 
    // query building data:
    private ClinicalAnnotationSelection clinicalAnnotationSelections;
    // TODO Code the classes below
    //private SampleAnnotationSelection sampleAnnotationSelections;
    //private ImageAnnotationSelection imageAnnotationSelections;
    // The currently selected set of annotations
    //private AnnotationSelection currentAnnotationSelections;
    
    /**
     * Default constructor.
     */
    public ManageQueryHelper() {
        // Default constructor
    }
    
    /**
     * @return the advancedView
     */
    public boolean isAdvancedView() {
        return advancedView;
    }

    /**
     * @param advancedView the advancedView to set
     */
    public void setAdvancedView(boolean advancedView) {
        this.advancedView = advancedView;
    }
    
    /**
     * @return the queryCriteriaRowList
     */
    public List<QueryAnnotationCriteria> getQueryCriteriaRowList() {
        if (this.queryCriteriaRowList == null) {
            this.queryCriteriaRowList = new ArrayList<QueryAnnotationCriteria>(); 
        }
        
        return this.queryCriteriaRowList;
    }

    /**
     * @param queryCriteriaRowList the queryCriteriaRowList to set
     */
    public void setQueryCriteriaRowList(List<QueryAnnotationCriteria> queryCriteriaRowList) {
        this.queryCriteriaRowList = queryCriteriaRowList;
    }
    
    /**
     * @param queryAnnotationCriteria the QueryAnnotationCriteria to add
     */
    public void addQueryAnnotationCriteriaToList(QueryAnnotationCriteria queryAnnotationCriteria) {
        this.getQueryCriteriaRowList().add(queryAnnotationCriteria);
    }
      
    /**
     * @return the clinicalAnnotationDefinitions
     */
    public Collection<AnnotationDefinition> getClinicalAnnotationDefinitions() {
        return clinicalAnnotationDefinitions;
    }

    /**
     * @param clinicalAnnotationDefinitions the clinicalAnnotationDefinitions to set
     */
    public void setClinicalAnnotationDefinitions(Collection<AnnotationDefinition> clinicalAnnotationDefinitions) {
        this.clinicalAnnotationDefinitions = clinicalAnnotationDefinitions;
    }

    /**
     * @return the sampleAnnotationDefinitions
     */
    public Collection<AnnotationDefinition> getSampleAnnotationDefinitions() {
        return sampleAnnotationDefinitions;
    }

    /**
     * @param sampleAnnotationDefinitions the sampleAnnotationDefinitions to set
     */
    public void setSampleAnnotationDefinitions(Collection<AnnotationDefinition> sampleAnnotationDefinitions) {
        this.sampleAnnotationDefinitions = sampleAnnotationDefinitions;
    }

    /**
     * @return the imageAnnotationDefinitions
     */
    public Collection<AnnotationDefinition> getImageAnnotationDefinitions() {
        return imageAnnotationDefinitions;
    }

    /**
     * @param imageAnnotationDefinitions the imageAnnotationDefinitions to set
     */
    public void setImageAnnotationDefinitions(Collection<AnnotationDefinition> imageAnnotationDefinitions) {
        this.imageAnnotationDefinitions = imageAnnotationDefinitions;
    }

    /**
     * @return the clinicalAnnotationSelections
     */
    public ClinicalAnnotationSelection getClinicalAnnotationSelections() {
        return clinicalAnnotationSelections;
    }

    /**
     * @param clinicalAnnotationSelections the clinicalAnnotationSelections to set
     */
    public void setClinicalAnnotationSelections(ClinicalAnnotationSelection clinicalAnnotationSelections) {
        this.clinicalAnnotationSelections = clinicalAnnotationSelections;
    }
    
    /**
     * Fetch and store the annotation select lists.
     * @param studyManagementService enables retrieval of a study
     */
    public void prepopulateAnnotationSelectLists(StudyManagementService studyManagementService) {
        Study study;
        StudyConfiguration studyConfiguration;
        
        // TODO Use the currently selected study title below
        studyConfiguration = getCurrentStudyConfiguration(studyManagementService, "Rembrandt/VASARI");
        study = studyConfiguration.getStudy();
        
        // Get all data type annotation field definitions:
        populateClinicalAnnotationDefinitions(study);
        // TODO get sample and imaging
        
        // Instantiate the annotation selection objects:
        populateAnnotationSelections(studyConfiguration);
    }
    
    /**
     * @param studyManagementService the studyManagementService object.
     * @param studyShortTitle the short title of the study to retrieve.
     * @return StudyConfiguration the studyConfiguration that wraps the named study.
     */
    public StudyConfiguration getCurrentStudyConfiguration(StudyManagementService studyManagementService, 
            String studyShortTitle) {
        List<StudyConfiguration> studyConfigurations;
        
        studyConfigurations = studyManagementService.getManagedStudies(SecurityHelper.getCurrentUsername());
        StudyConfiguration sc = null;
        Iterator<StudyConfiguration> iter = studyConfigurations.iterator();
        while (iter.hasNext()) {
            sc = iter.next(); 
            if ((sc.getStudy().getShortTitleText()).equals(studyShortTitle)) {
                break;
            }
        }
        
        return sc;
    }
    
    private void populateAnnotationSelections(StudyConfiguration studyConfiguration) {
        populateClinicalAnnotationSelections(studyConfiguration);
        
        // TODO populate sample and image selection objects
    }
    
    private void populateClinicalAnnotationDefinitions(Study study) {
        Collection<AnnotationDefinition> annoDefs = study.getSubjectAnnotationCollection();
        // Below should trigger Hibernate lazy loading
        if (annoDefs != null && !annoDefs.isEmpty()) {
            clinicalAnnotationDefinitions = annoDefs;
        }
    }
    
    private void populateClinicalAnnotationSelections(StudyConfiguration studyConfiguration) {
        if (this.clinicalAnnotationSelections == null) {
            this.clinicalAnnotationSelections = new ClinicalAnnotationSelection();
        }
        if (clinicalAnnotationDefinitions != null && !clinicalAnnotationDefinitions.isEmpty()) {
            ArrayList<String> annotationSelections = new ArrayList<String>(10);
            Iterator<AnnotationDefinition> annoDefsIter = clinicalAnnotationDefinitions.iterator();
            while (annoDefsIter.hasNext()) {
                annotationSelections.add(annoDefsIter.next().getDisplayName());
            }
            annotationSelections.trimToSize();
            // Set the annotation display selection list
            clinicalAnnotationSelections.setAnnotationSelections(annotationSelections);
            // Set the annotation definitions
            clinicalAnnotationSelections.setAnnotationDefinitions(clinicalAnnotationDefinitions);
            clinicalAnnotationSelections.setStudyConfiguration(studyConfiguration);
        }
    }
    
    /**
     * Configures clinical row.
     */
    public void configureClinicalQueryCriterionRow() {
        //this.setCurrentAnnotationSelections(this.getClinicalAnnotationSelections());
        // Add new query criteria row to queryCriteriaRowList
        QueryAnnotationCriteria queryAnnotationCriteria = new QueryAnnotationCriteriaImpl();
        //queryAnnotationCriteria.setAnnotationSelections(this.getCurrentAnnotationSelections());
        queryAnnotationCriteria.setAnnotationSelections(this.getClinicalAnnotationSelections());
        queryAnnotationCriteria.setAnnotationValue("");
        queryAnnotationCriteria.setRowType("clinical"); //TODO make clinical a constant
        queryAnnotationCriteria.setRowLable("Clinical");
        this.addQueryAnnotationCriteriaToList(queryAnnotationCriteria);
    }

    /**
     * @return the currentAnnotationSelections
     */
//    public AnnotationSelection getCurrentAnnotationSelections() {
//        return currentAnnotationSelections;
//    }

    /**
     * @param currentAnnotationSelections the currentAnnotationSelections to set
     */
//    public void setCurrentAnnotationSelections(AnnotationSelection currentAnnotationSelections) {
//        this.currentAnnotationSelections = currentAnnotationSelections;
//    }

    /**
     * @param selectedValues The array of values to be added.
     */
    public void updateSelectedClinicalValues(String[] selectedValues) {

        QueryAnnotationCriteria tempAnnotationCriteria = null;
        Iterator<QueryAnnotationCriteria> rowIter = this.getQueryCriteriaRowList().iterator();
        int i = 0;
        while (rowIter.hasNext()) {
            tempAnnotationCriteria = rowIter.next();
            if (selectedValues[i] != null) {
                tempAnnotationCriteria.setAnnotationSelection(selectedValues[i]);
                this.queryCriteriaRowList.set(i, tempAnnotationCriteria);
            }
            i++;
        }
    }
    
    /**
     * @param selectedValues The array of values to be added.
     */
    public void updateSelectedOperatorValues(String[] selectedValues) {

        QueryAnnotationCriteria tempAnnotationCriteria = null;
        Iterator<QueryAnnotationCriteria> rowIter = this.getQueryCriteriaRowList().iterator();
        int i = 0;
        while (rowIter.hasNext()) {
            tempAnnotationCriteria = rowIter.next();
            if (selectedValues[i] != null) {
                tempAnnotationCriteria.setAnnotationOperatorSelection(selectedValues[i]);
                this.queryCriteriaRowList.set(i, tempAnnotationCriteria);
            }
            i++;
        }
    }    

    /**
     * @param selectedValues The array of values to be added.
     */
    public void updateSelectedUserValues(String[] selectedValues) {

        QueryAnnotationCriteria tempAnnotationCriteria = null;
        Iterator<QueryAnnotationCriteria> rowIter = this.getQueryCriteriaRowList().iterator();
        int i = 0;
        while (rowIter.hasNext()) {
            tempAnnotationCriteria = rowIter.next();
            if (selectedValues[i] != null) {
                tempAnnotationCriteria.setAnnotationValue(selectedValues[i]);
                this.queryCriteriaRowList.set(i, tempAnnotationCriteria);
            }
            i++;
        }
    }    
}
