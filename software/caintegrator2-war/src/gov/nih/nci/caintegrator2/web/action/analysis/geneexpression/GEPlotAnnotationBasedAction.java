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


import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DatePermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringPermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * Action dealing with Gene Expression Annotation Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class GEPlotAnnotationBasedAction extends AbstractGeneExpressionAction {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_PLOT_URL = "/caintegrator2/retrieveAnnotationGEPlot.action?";
    private GEPlotAnnotationBasedParameters plotParameters = new GEPlotAnnotationBasedParameters();
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(ANNOTATION_TAB);
        retrieveFormValues();
        refreshObjectInstances();
    }
    
    
    private void retrieveFormValues() {
        plotParameters.setGeneSymbol(getGePlotForm().getAnnotationBasedForm().getGeneSymbol());
        if (getForm().getSelectedAnnotationId() != null 
                && !StringUtils.isEmpty(getForm().getSelectedAnnotationId())) {
                plotParameters.getSelectedAnnotation().setId(
                       Long.valueOf(getForm().getSelectedAnnotationId()));
           }
       retrieveFormSelectedValues();
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null checks and switch statement
    private void retrieveFormSelectedValues() {
        if (!getForm().getSelectedValuesIds().isEmpty()) {
            refreshSelectedAnnotationInstance();
            plotParameters.getSelectedValues().clear();
            
            AnnotationTypeEnum permissibleValuesType = AnnotationTypeEnum.getByValue(
                    plotParameters.getSelectedAnnotation().getType());
            for (String id : getForm().getSelectedValuesIds()) {
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
                plotParameters.getSelectedValues().add(value);
            }
        }
    }
    
    private void refreshObjectInstances() {
        refreshSelectedAnnotationInstance();
        refreshSelectedAnnotationValuesInstance();
    }
    
    private void refreshSelectedAnnotationInstance() {
        if (plotParameters.getSelectedAnnotation().getId() != null) {
            plotParameters.setSelectedAnnotation(getStudyManagementService().
                        getRefreshedStudyEntity(plotParameters.getSelectedAnnotation()));
        }
    }
    
    private void refreshSelectedAnnotationValuesInstance() {
        if (!plotParameters.getSelectedValues().isEmpty()) {
            Collection <AbstractPermissibleValue> newValues = new HashSet<AbstractPermissibleValue>();
            for (AbstractPermissibleValue value : plotParameters.getSelectedValues()) {
                AbstractPermissibleValue newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            plotParameters.getSelectedValues().clear();
            plotParameters.getSelectedValues().addAll(newValues);
        }
    }
    
    /**
     * Clears all input values and geneExpression plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        clearAnnotationBasedGePlot();
        getForm().clear();
        plotParameters.clear();
        return SUCCESS;
    }

    private void clearAnnotationBasedGePlot() {
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        setDisplayTab(ANNOTATION_TAB);
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
        clearAnnotationBasedGePlot();
        getForm().clearPermissibleValues();
        if (!loadAnnotationDefinitions()) {
            return INPUT;
        }
        return SUCCESS;
    }

    private boolean validateAnnotationType() {
        try {
            if (getForm().getAnnotationTypeSelection() == null) {
                addActionError("Annotation Type unknown for selected annotation.");
                return false;
            }
            EntityTypeEnum.checkType(getForm().getAnnotationTypeSelection());
        } catch (IllegalArgumentException e) {
            addActionError("Annotation Type unknown for selected annotation.");
            return false;
        }
        return true;
    }


    private boolean loadAnnotationDefinitions() {
        if (getForm().getAnnotationTypeSelection() == null 
                || getForm().getAnnotationTypeSelection().equals("invalidSelection")) {
            addActionError("Must select Annotation Type first");
            return false;
        }
        EntityTypeEnum annotationEntityType = 
            EntityTypeEnum.getByValue(getForm().getAnnotationTypeSelection());
        getForm().setAnnotationDefinitions(new HashMap<String, AnnotationDefinition>());
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
                getForm().getAnnotationDefinitions().put(definition.getId().toString(), definition);
            }
        }
    }
    
    /**
     * This is used to update the Permissible Values on the Kaplan-Meier input form when
     * a user selects a valid AnnotationDefinition.
     * @return Struts return value.
     */
    public String updatePermissibleValues() {
        if (isPermissibleValuesNeedUpdate()) {
            loadAnnotationDefinitions();
            if (plotParameters.getSelectedAnnotation() == null) {
                addActionError("Please select a valid annotation");
                return INPUT;
            }
            getForm().clearPermissibleValues();
            clearAnnotationBasedGePlot();
            loadPermissibleValues();
        }
        return SUCCESS;
    }

    private void loadPermissibleValues() {
        for (AbstractPermissibleValue value 
              : plotParameters.getSelectedAnnotation().getPermissibleValueCollection()) {
            getForm().getPermissibleValues().put(value.getId().toString(), 
                    value.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runFirstCreatePlotThread() {
        setPermissibleValuesNeedUpdate(false);
        if (!isCreatePlotRunning()) {
            setCreatePlotRunning(true);
            loadAnnotationDefinitions();
            loadPermissibleValues();
            if (plotParameters.validate()) {
                plotParameters.setEntityType(EntityTypeEnum.getByValue(getForm().getAnnotationTypeSelection()));
                GeneExpressionPlotGroup plots = getAnalysisService().
                                createGeneExpressionPlot(getStudySubscription(), plotParameters);
                SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plots);
            }
            setCreatePlotRunning(false);
        }
    }
    
    /**
     * This is set only when the dropdown on the JSP is selecting an annotation definition, so we know that
     * a permissible value change needs to occur.
     * @param needUpdate T/F value.
     */
    public void setPermissibleValuesNeedUpdate(boolean needUpdate) {
        getForm().setPermissibleValuesNeedUpdate(needUpdate);
    }
    
    private boolean isPermissibleValuesNeedUpdate() {
        return getForm().isPermissibleValuesNeedUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        if (getForm().getSelectedAnnotationId() != null 
            && !"-1".equals(getForm().getSelectedAnnotationId())
            && getForm().getAnnotationTypeSelection() != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlotUrl() {
        return ANNOTATION_PLOT_URL;
    }

    /**
     * @return
     */
    private GEPlotAnnotationBasedActionForm getForm() {
        return getGePlotForm().getAnnotationBasedForm();
    }


    /**
     * @return the plotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public GEPlotAnnotationBasedParameters getPlotParameters() {
        return plotParameters;
    }


    /**
     * @param plotParameters the plotParameters to set
     */
    public void setPlotParameters(GEPlotAnnotationBasedParameters plotParameters) {
        this.plotParameters = plotParameters;
    }

}
