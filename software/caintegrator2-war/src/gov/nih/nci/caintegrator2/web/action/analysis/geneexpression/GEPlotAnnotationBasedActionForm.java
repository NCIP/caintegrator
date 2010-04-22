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
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Form used to store input values for Annotation Based GE Plots. 
 */
public class GEPlotAnnotationBasedActionForm {
    
    private String annotationGroupSelection;
    private String selectedAnnotationId;
    private Collection <String> selectedValuesIds = new HashSet<String>();
    private boolean permissibleValuesNeedUpdate = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private boolean addControlSamplesGroup = false;
    private String controlSampleSetName;
    private String platformName;
    private String geneSymbol;
    private String reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
    private List<String> controlSampleSets = new ArrayList<String>();
    
    
    // JSP Select List Options
    private Map<String, AnnotationFieldDescriptor> annotationFieldDescriptors = 
        new HashMap<String, AnnotationFieldDescriptor>();
    private Map<String, String> permissibleValues = new HashMap<String, String>();
    


    /**
     * Clears all the variables to null.
     */
    public void clear() {
        annotationGroupSelection = null;
        selectedAnnotationId = null;
        selectedValuesIds = new HashSet<String>();
        addPatientsNotInQueriesGroup = false;
        addControlSamplesGroup = false;
        geneSymbol = null;
        reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
        platformName = null;
        controlSampleSets = new ArrayList<String>();
        clearAnnotationDefinitions();
    }
    
    /**
     * Clears the annotation definitions.
     */
    public void clearAnnotationDefinitions() {
        annotationFieldDescriptors = new HashMap<String, AnnotationFieldDescriptor>();
        setSelectedAnnotationId(null);
        clearPermissibleValues();
    }
    
    /**
     * Clears the permissible values.
     */
    public void clearPermissibleValues() {
        permissibleValues = new HashMap<String, String>();
        getSelectedValuesIds().clear();
    }
    
    /**
     * @return the annotationTypeSelection
     */
    public String getAnnotationGroupSelection() {
        return annotationGroupSelection;
    }

    /**
     * @param annotationGroupSelection the annotationGroupSelection to set
     */
    public void setAnnotationGroupSelection(String annotationGroupSelection) {
        this.annotationGroupSelection = annotationGroupSelection;
    }

    /**
     * @return the selectedAnnotationId
     */
    public String getSelectedAnnotationId() {
        return selectedAnnotationId;
    }

    /**
     * @param selectedAnnotationId the selectedAnnotationId to set
     */
    public void setSelectedAnnotationId(String selectedAnnotationId) {
        this.selectedAnnotationId = selectedAnnotationId;
    }

    /**
     * @return the selectedValuesIds
     */
    public Collection<String> getSelectedValuesIds() {
        return selectedValuesIds;
    }


    /**
     * @param selectedValuesIds the selectedValuesIds to set
     */
    public void setSelectedValuesIds(Collection<String> selectedValuesIds) {
        this.selectedValuesIds = selectedValuesIds;
    }

    /**
     * @return the annotationFieldDescriptors
     */
    public Map<String, AnnotationFieldDescriptor> getAnnotationFieldDescriptors() {
        return annotationFieldDescriptors;
    }

    /**
     * @param annotationFieldDescriptors the annotationFieldDescriptors to set
     */
    public void setAnnotationFieldDescriptors(Map<String, AnnotationFieldDescriptor> annotationFieldDescriptors) {
        this.annotationFieldDescriptors = annotationFieldDescriptors;
    }

    /**
     * @return the permissibleValues
     */
    public Map<String, String> getPermissibleValues() {
        return permissibleValues;
    }

    /**
     * @param permissibleValues the permissibleValues to set
     */
    public void setPermissibleValues(Map<String, String> permissibleValues) {
        this.permissibleValues = permissibleValues;
    }

    /**
     * @return the permissibleValuesNeedUpdate
     */
    public boolean isPermissibleValuesNeedUpdate() {
        return permissibleValuesNeedUpdate;
    }

    /**
     * @param permissibleValuesNeedUpdate the permissibleValuesNeedUpdate to set
     */
    public void setPermissibleValuesNeedUpdate(boolean permissibleValuesNeedUpdate) {
        this.permissibleValuesNeedUpdate = permissibleValuesNeedUpdate;
    }

    /**
     * @return the addPatientsNotInQueriesGroup
     */
    public boolean isAddPatientsNotInQueriesGroup() {
        return addPatientsNotInQueriesGroup;
    }

    /**
     * @param addPatientsNotInQueriesGroup the addPatientsNotInQueriesGroup to set
     */
    public void setAddPatientsNotInQueriesGroup(boolean addPatientsNotInQueriesGroup) {
        this.addPatientsNotInQueriesGroup = addPatientsNotInQueriesGroup;
    }

    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the reporterType
     */
    public String getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the addControlSamplesGroup
     */
    public boolean isAddControlSamplesGroup() {
        return addControlSamplesGroup;
    }

    /**
     * @param addControlSamplesGroup the addControlSamplesGroup to set
     */
    public void setAddControlSamplesGroup(boolean addControlSamplesGroup) {
        this.addControlSamplesGroup = addControlSamplesGroup;
    }

    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }

    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }

    /**
     * @return the controlSampleSets
     */
    public List<String> getControlSampleSets() {
        return controlSampleSets;
    }

    /**
     * @param controlSampleSets the controlSampleSets to set
     */
    public void setControlSampleSets(List<String> controlSampleSets) {
        this.controlSampleSets = controlSampleSets;
    }

}
