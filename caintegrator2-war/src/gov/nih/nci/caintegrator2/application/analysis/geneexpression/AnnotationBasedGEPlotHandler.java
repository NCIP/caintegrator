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
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfiguration;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotConfigurationFactory;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GE Plot Handler for Annotation Based GE Plots.
 */
class AnnotationBasedGEPlotHandler extends AbstractGEPlotHandler {

    private final GEPlotAnnotationBasedParameters parameters;
    private final Set<StudySubjectAssignment> usedSubjects = new HashSet<StudySubjectAssignment>();
        
    AnnotationBasedGEPlotHandler(CaIntegrator2Dao dao, 
                                 QueryManagementService queryManagementService, 
                                 GEPlotAnnotationBasedParameters parameters) {
        super(dao, queryManagementService);
        this.parameters = parameters;
    }
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup createPlots(GeneExpressionPlotService gePlotService, 
                                               StudySubscription subscription) {
        
        List<GenomicDataQueryResult> genomicResults = new ArrayList<GenomicDataQueryResult>();
        for (AbstractPermissibleValue permissibleValue : parameters.getSelectedValues()) {
            GenomicDataQueryResult result = retrieveGenomicResults(permissibleValue, subscription);
            fillUsedSubjects(result);
            genomicResults.add(result);
        }
        if (parameters.isAddPatientsNotInQueriesGroup()) {
            genomicResults.add(0, addAllOthersGroup(subscription));
        }
        
        GeneExpressionPlotConfiguration configuration = 
                GeneExpressionPlotConfigurationFactory.createPlotConfiguration(genomicResults);
        return gePlotService.generatePlots(configuration);
    }

    private void fillUsedSubjects(GenomicDataQueryResult queryResults) {
        if (parameters.isAddPatientsNotInQueriesGroup()) {
            for (GenomicDataResultRow row : queryResults.getRowCollection()) {
                for (GenomicDataResultValue value : row.getValueCollection()) {
                    usedSubjects.add(value.getColumn().getSampleAcquisition().getAssignment());
                }
            }
        }
    }

    private GenomicDataQueryResult addAllOthersGroup(StudySubscription subscription) {
        Query query = new Query();
        query.setName("All Others");
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        return retrieveAllOtherGenomicResults(subscription, query);
    }

    private GenomicDataQueryResult retrieveGenomicResults(AbstractPermissibleValue permissibleValue,
                                                          StudySubscription subscription) {
        Query query = new Query();
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGeneSymbol(parameters.getGeneSymbol());
        SelectedValueCriterion selectedValueCriterion = new SelectedValueCriterion();
        selectedValueCriterion.setAnnotationDefinition(parameters.getSelectedAnnotation());
        selectedValueCriterion.setEntityType(parameters.getEntityType());
        selectedValueCriterion.setValueCollection(new HashSet<AbstractPermissibleValue>());
        selectedValueCriterion.getValueCollection().add(permissibleValue);
        query.setName(permissibleValue.toString());
        
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        query.getCompoundCriterion().getCriterionCollection().add(selectedValueCriterion);
        query.setResultType(ResultTypeEnum.GENOMIC);
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

    private GenomicDataQueryResult retrieveAllOtherGenomicResults(StudySubscription subscription, Query query) {
        CompoundCriterion newCompoundCriterion = new CompoundCriterion();
        newCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        newCompoundCriterion.getCriterionCollection().add(query.getCompoundCriterion());
        newCompoundCriterion.getCriterionCollection().add(retrieveAllOtherCompoundCriterion());
        query.setCompoundCriterion(newCompoundCriterion);
        query.setReporterType(parameters.getReporterType());
        query.setSubscription(subscription);
        return getQueryManagementService().executeGenomicDataQuery(query);
    }

    private CompoundCriterion retrieveAllOtherCompoundCriterion() {
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGeneSymbol(parameters.getGeneSymbol());
        CompoundCriterion geneNameCompoundCriterion = new CompoundCriterion();
        geneNameCompoundCriterion.getCriterionCollection().add(geneNameCriterion);
        geneNameCompoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        geneNameCompoundCriterion.getCriterionCollection().add(retrieveUsedSubjectsCriterion());
        return geneNameCompoundCriterion;
    }
    
    private CompoundCriterion retrieveUsedSubjectsCriterion() {
        CompoundCriterion idCriteria = new CompoundCriterion();
        idCriteria.setBooleanOperator(BooleanOperatorEnum.AND);
        idCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        for (StudySubjectAssignment assignment : usedSubjects) {
            IdentifierCriterion idCriterion = new IdentifierCriterion();
            idCriterion.setStringValue(assignment.getIdentifier());
            idCriterion.setWildCardType(WildCardTypeEnum.NOT_EQUAL_TO);
            idCriterion.setEntityType(EntityTypeEnum.SUBJECT);
            idCriteria.getCriterionCollection().add(idCriterion);
        }
        return idCriteria;
    }

}
