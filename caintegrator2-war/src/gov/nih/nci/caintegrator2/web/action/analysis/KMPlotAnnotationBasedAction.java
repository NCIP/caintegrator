/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caArray
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caArray Software License (the License) is between NCI and You. You (or 
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
 * its rights in the caArray Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caArray Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator Software and any 
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
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator2.application.analysis.KMAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DatePermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Action dealing with Kaplan-Meier Annotaion Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class KMPlotAnnotationBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;

    private KMPlotAnnotationBasedActionForm kaplanMeierFormValues = new KMPlotAnnotationBasedActionForm();
    
    // JSP Select List Options
    private Map<String, AnnotationDefinition> annotationDefinitions = new HashMap<String, AnnotationDefinition>();
    private Map<String, String> permissibleValues = new HashMap<String, String>();
    private Map<String, SurvivalValueDefinition> survivalValueDefinitions = 
                                        new HashMap<String, SurvivalValueDefinition>();

    private KMAnnotationBasedParameters kmPlotParameters = new KMAnnotationBasedParameters();
    
    /**
     * Refreshes the current clinical source configuration.
     */
    @Override
    public void prepare() {
        super.prepare();
        populateSurvivalValueDefinitions();
        retrieveFormValues();
        refreshObjectInstances();
    }
    
    private void populateSurvivalValueDefinitions() {
        if (getStudy() != null 
            && getStudy().getSurvivalValueDefinitionCollection() != null
            && survivalValueDefinitions.size() 
                != getStudy().getSurvivalValueDefinitionCollection().size()) {
            survivalValueDefinitions = new HashMap<String, SurvivalValueDefinition>();
            for (SurvivalValueDefinition def 
                    : getStudy().getSurvivalValueDefinitionCollection()) {
                survivalValueDefinitions.put(def.getId().toString(), def);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (getStudySubscription() == null) {
            addActionError("Please select a study under \"My Studies\".");
        }
        if (survivalValueDefinitions.isEmpty()) {
            addActionError("There are no survival value definitions defined for this study, "
                    + "unable to create Kaplan-Meier plot.");
        }
    }
    
    private void retrieveFormValues() {
        if (kaplanMeierFormValues.getSelectedAnnotationId() != null 
                && !StringUtils.isEmpty(kaplanMeierFormValues.getSelectedAnnotationId())) {
               kmPlotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(kaplanMeierFormValues.getSelectedAnnotationId()));
           }
       if (kaplanMeierFormValues.getSurvivalValueDefinitionId() != null 
                && !StringUtils.isEmpty(kaplanMeierFormValues.getSurvivalValueDefinitionId())) {
               kmPlotParameters.getSurvivalValueDefinition().setId(
                       Long.valueOf(kaplanMeierFormValues.getSurvivalValueDefinitionId()));
       }
       retrieveFormSelectedValues();
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks and switch statement
    private void retrieveFormSelectedValues() {
        if (!kaplanMeierFormValues.getSelectedValuesIds().isEmpty()) {
            refreshSelectedAnnotationInstance();
            kmPlotParameters.getSelectedValues().clear();
            
            AnnotationTypeEnum permissibleValuesType = AnnotationTypeEnum.getByValue(
                                        kmPlotParameters.getSelectedAnnotation().getType());
            for (String id : kaplanMeierFormValues.getSelectedValuesIds()) {
                AbstractPermissibleValue value = null;
                switch(permissibleValuesType) {
                case STRING:
                    value = new StringPermissibleValue();
                    break;
                case NUMERIC:
                    value = new NumericPermissibleValue();
                    break;
                case DATE:
                    value = new DatePermissibleValue();
                    break;
                default:
                    addActionError("Internal Error, Unknown Permissible Values Type.");
                    return;
                }
                value.setId(Long.valueOf(id));
                kmPlotParameters.getSelectedValues().add(value);
            }
        }
    }
    
    private void refreshObjectInstances() {
        refreshSelectedAnnotationInstance();
        refreshSelectedAnnotationValuesInstance();
        if (kmPlotParameters.getSurvivalValueDefinition().getId() != null) {
            kmPlotParameters.setSurvivalValueDefinition(getStudyManagementService().
                    getRefreshedStudyEntity(kmPlotParameters.getSurvivalValueDefinition()));
        }
    }
    
    private void refreshSelectedAnnotationInstance() {
        if (kmPlotParameters.getSelectedAnnotation().getId() != null) {
            kmPlotParameters.setSelectedAnnotation(getStudyManagementService().
                        getRefreshedStudyEntity(kmPlotParameters.getSelectedAnnotation()));
        }
    }
    
    private void refreshSelectedAnnotationValuesInstance() {
        if (!kmPlotParameters.getSelectedValues().isEmpty()) {
            Collection <AbstractPermissibleValue> newValues = new HashSet<AbstractPermissibleValue>();
            for (AbstractPermissibleValue value : kmPlotParameters.getSelectedValues()) {
                AbstractPermissibleValue newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            kmPlotParameters.getSelectedValues().clear();
            kmPlotParameters.getSelectedValues().addAll(newValues);
        }
    }
    
    private void clear() {
        kaplanMeierFormValues.clear();
        clearAnnotationDefinitions();
    }
    private void clearAnnotationDefinitions() {
        annotationDefinitions = new HashMap<String, AnnotationDefinition>();
        kmPlotParameters.setSelectedAnnotation(new AnnotationDefinition());
        kaplanMeierFormValues.setSelectedAnnotationId(null);
        clearPermissibleValues();
    }
    private void clearPermissibleValues() {
        SessionHelper.setKmPlot(null);
        permissibleValues = new HashMap<String, String>();
        kmPlotParameters.getSelectedValues().clear();
        kaplanMeierFormValues.getSelectedValuesIds().clear();
    }
    
    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        clear();
        return SUCCESS;
    }
    
    /**
     * This is used to update the Annotation Definitions on the Kaplan-Meier input form when
     * a user selects a valid Annotation Type.
     * @return Struts return value.
     */
    public String updateAnnotationDefinitions() {
        if (!validateAnnotationType()) {
            return INPUT;
        }
        clearPermissibleValues();
        if (!loadAnnotationDefinitions()) {
            return INPUT;
        }
        return SUCCESS;
    }

    private boolean validateAnnotationType() {
        try {
            if (kaplanMeierFormValues.getAnnotationTypeSelection() == null) {
                addActionError("Annotation Type unknown for selected annotation.");
                return false;
            }
            EntityTypeEnum.checkType(kaplanMeierFormValues.getAnnotationTypeSelection());
        } catch (IllegalArgumentException e) {
            addActionError("Annotation Type unknown for selected annotation.");
            return false;
        }
        return true;
    }


    private boolean loadAnnotationDefinitions() {
        if (kaplanMeierFormValues.getAnnotationTypeSelection() == null 
                || kaplanMeierFormValues.getAnnotationTypeSelection().equals("invalidSelection")) {
            addActionError("Must select Annotation Type first");
            return false;
        }
        EntityTypeEnum annotationEntityType = 
            EntityTypeEnum.getByValue(kaplanMeierFormValues.getAnnotationTypeSelection());
        annotationDefinitions = new HashMap<String, AnnotationDefinition>();
        Collection<AnnotationDefinition> studyDefinitionCollection = new HashSet<AnnotationDefinition>();
        switch (annotationEntityType) {
        case SUBJECT:
            studyDefinitionCollection = getStudy().getSubjectAnnotationCollection();
            break;
        case IMAGESERIES:
            studyDefinitionCollection = getStudy().getImageSeriesAnnotationCollection();
            break;
        case SAMPLE:
            studyDefinitionCollection = getStudy().getSampleAnnotationCollection();
            break;
        default:
            addActionError("Unknown Annotation Type");
            return false;
        }
        loadValidAnnotationDefinitions(studyDefinitionCollection);
        return true;
    }
    
    private void loadValidAnnotationDefinitions(Collection<AnnotationDefinition> definitions) {
        for (AnnotationDefinition definition : definitions) {
            if (definition.getPermissibleValueCollection() != null 
                && !definition.getPermissibleValueCollection().isEmpty()) {
                annotationDefinitions.put(definition.getId().toString(), definition);
            }
        }
    }
    
    /**
     * This is used to update the Permissible Values on the Kaplan-Meier input form when
     * a user selects a valid AnnotationDefinition.
     * @return Struts return value.
     */
    public String updatePermissibleValues() {
        loadAnnotationDefinitions();
        if (kmPlotParameters.getSelectedAnnotation() == null) {
            addActionError("Plesae select a valid annotation");
            return INPUT;
        }
        clearPermissibleValues();
        loadPermissibleValues();
        return SUCCESS;
    }

    private void loadPermissibleValues() {
        for (AbstractPermissibleValue value 
              : kmPlotParameters.getSelectedAnnotation().getPermissibleValueCollection()) {
            permissibleValues.put(value.getId().toString(), 
                    PermissibleValueUtil.getDisplayString(value));
        }
    }
    
    /**
     * When the form is filled out and the user clicks "Create Plot" this calls the
     * analysis service to generate a KMPlot object.
     * @return Struts return value.
     */
    public String createPlot() {
        loadAnnotationDefinitions();
        loadPermissibleValues();
        if (!kmPlotParameters.validate()) {
            for (String errorMessages : kmPlotParameters.getErrorMessages()) {
                addActionError(errorMessages);
            }
            return INPUT;
        }
        kmPlotParameters.setEntityType(EntityTypeEnum.getByValue(kaplanMeierFormValues.getAnnotationTypeSelection()));
        KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
        SessionHelper.setKmPlot(plot);
        return SUCCESS;
    }
    
    
    
    /**
     * Determines if the "Create Plot" button should be displayed.
     * @return T/F value.
     */
    @Override
    public boolean isCreatable() {
        if (kaplanMeierFormValues.getSelectedAnnotationId() != null 
            && !"-1".equals(kaplanMeierFormValues.getSelectedAnnotationId())
            && kaplanMeierFormValues.getAnnotationTypeSelection() != null 
            && kaplanMeierFormValues.getSurvivalValueDefinitionId() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the annotationDefinitions
     */
    public Map<String, AnnotationDefinition> getAnnotationDefinitions() {
        return annotationDefinitions;
    }

    /**
     * @param annotationDefinitions the annotationDefinitions to set
     */
    public void setAnnotationDefinitions(Map<String, AnnotationDefinition> annotationDefinitions) {
        this.annotationDefinitions = annotationDefinitions;
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
     * @return the survivalValueDefinitions
     */
    public Map<String, SurvivalValueDefinition> getSurvivalValueDefinitions() {
        return survivalValueDefinitions;
    }

    /**
     * @param survivalValueDefinitions the survivalValueDefinitions to set
     */
    public void setSurvivalValueDefinitions(Map<String, SurvivalValueDefinition> survivalValueDefinitions) {
        this.survivalValueDefinitions = survivalValueDefinitions;
    }

    /**
     * @return the kaplanMeierFormValues
     */
    public KMPlotAnnotationBasedActionForm getKaplanMeierFormValues() {
        return kaplanMeierFormValues;
    }

    /**
     * @param kaplanMeierFormValues the kaplanMeierFormValues to set
     */
    public void setKaplanMeierFormValues(KMPlotAnnotationBasedActionForm kaplanMeierFormValues) {
        this.kaplanMeierFormValues = kaplanMeierFormValues;
    }

    /**
     * @return the kmPlotParameters
     */
    public KMAnnotationBasedParameters getKmPlotParameters() {
        return kmPlotParameters;
    }

    /**
     * @param kmPlotParameters the kmPlotParameters to set
     */
    public void setKmPlotParameters(KMAnnotationBasedParameters kmPlotParameters) {
        this.kmPlotParameters = kmPlotParameters;
    }

}
