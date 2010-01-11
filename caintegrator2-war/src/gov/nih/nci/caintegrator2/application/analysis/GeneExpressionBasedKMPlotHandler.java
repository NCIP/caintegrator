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
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectGroup;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * KM Plot Handler for Gene Expression Based KM Plots.
 */
class GeneExpressionBasedKMPlotHandler extends AbstractKMPlotHandler {

    private final KMGeneExpressionBasedParameters kmParameters;
        
    GeneExpressionBasedKMPlotHandler(StudySubscription studySubscription, CaIntegrator2Dao dao, 
                                 SurvivalValueDefinition survivalValueDefinition, 
                                 QueryManagementService queryManagementService, 
                                 KMGeneExpressionBasedParameters kmParameters) {
        super(studySubscription, dao, survivalValueDefinition, queryManagementService);
        this.kmParameters = kmParameters;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    KMPlot createPlot(KMPlotService kmPlotService) throws InvalidCriterionException {
        validateSurvivalValueDefinition();
        KMPlotConfiguration configuration = new KMPlotConfiguration();
        Collection <SubjectGroup> subjectGroupCollection = new HashSet<SubjectGroup>();
        for (String geneSymbol : kmParameters.getGenesFoundInStudy()) {
            retrieveSubjectGroups(getStudySubscription(), subjectGroupCollection, geneSymbol);    
        }
        filterGroupsWithoutSurvivalData(configuration, subjectGroupCollection);
        configuration.getGenesNotFound().addAll(kmParameters.getGenesNotFound());
        if (kmParameters.getGenesFoundInStudy().size() > 1) {
            configuration.setTitle("OVERLAYED " + configuration.getTitle());
        }
        return kmPlotService.generatePlot(configuration);
    }


    private void retrieveSubjectGroups(StudySubscription subscription, 
                                       Collection<SubjectGroup> subjectGroupCollection,
                                       String geneSymbol) 
        throws InvalidCriterionException {
        Cai2Util.setColorPalette(3 * kmParameters.getGenesFoundInStudy().size());
        // Up Regulated
        SubjectGroup upRegulatedGroup = retrieveGroup(geneSymbol, geneSymbol + " >= " 
                                                                + kmParameters.getOverexpressedFoldChangeNumber() 
                                                                + "-fold Overexpressed",
                                                                RegulationTypeEnum.UP,
                                                                subscription);
        subjectGroupCollection.add(upRegulatedGroup);
        upRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Down Regulated
        SubjectGroup downRegulatedGroup = retrieveGroup(geneSymbol, geneSymbol + " >= " 
                                                        + kmParameters.getUnderexpressedFoldChangeNumber() 
                                                        + "-fold Underexpressed",
                                                        RegulationTypeEnum.DOWN,
                                                        subscription);
        subjectGroupCollection.add(downRegulatedGroup);
        downRegulatedGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
        
        // Intermediate
        SubjectGroup intermediateGroup = retrieveGroup(geneSymbol, geneSymbol + " intermediate",
                                                        RegulationTypeEnum.UNCHANGED,
                                                        subscription);
        subjectGroupCollection.add(intermediateGroup);
        intermediateGroup.setColor(Cai2Util.getColor(subjectGroupCollection.size()));
    }
    
    private SubjectGroup retrieveGroup(String geneSymbol, String groupName, RegulationTypeEnum regulationType, 
                                       StudySubscription subscription) throws InvalidCriterionException {
        SubjectGroup group = new SubjectGroup();
        group.setName(groupName);
        Collection<FoldChangeCriterion> criterionCollection = new HashSet<FoldChangeCriterion>();
        criterionCollection.add(retrieveFoldChangeCriterion(geneSymbol, regulationType));
        Collection<ResultRow> rows = retrieveFoldChangeRows(subscription, criterionCollection);
        assignRowsToGroup(group, rows);
        return group;
    }

    private void assignRowsToGroup(SubjectGroup group, Collection<ResultRow> rows) {
        for (ResultRow row : rows) {
        StudySubjectAssignment subjectAssignment = row.getSubjectAssignment();
            SubjectSurvivalData subjectSurvivalData = createSubjectSurvivalData(subjectAssignment);
            if (subjectSurvivalData != null) {
                group.getSurvivalData().add(subjectSurvivalData);
            }
        }
    }
    
    private FoldChangeCriterion retrieveFoldChangeCriterion(String geneSymbol, RegulationTypeEnum regulationType) {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(regulationType);
        criterion.setFoldsUp(kmParameters.getOverexpressedFoldChangeNumber().floatValue());
        criterion.setFoldsDown(kmParameters.getUnderexpressedFoldChangeNumber().floatValue());
        criterion.setControlSampleSetName(kmParameters.getControlSampleSetName());
        criterion.setGeneSymbol(geneSymbol);
        return criterion;
    }
    
    private Collection<ResultRow> retrieveFoldChangeRows(StudySubscription subscription, 
                                  Collection<FoldChangeCriterion> foldChangeCriterionCollection) 
                                  throws InvalidCriterionException {
        Query query = new Query();
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.getCompoundCriterion().getCriterionCollection().addAll(foldChangeCriterionCollection);
        query.setSubscription(subscription);
        QueryResult queryResult = getQueryManagementService().execute(query);
        return queryResult.getRowCollection();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void setupAndValidateParameters(AnalysisService analysisService) throws GenesNotFoundInStudyException {
        if (StringUtils.isNotBlank(kmParameters.getGeneSymbol())) {
            kmParameters.getGenesFoundInStudy().clear();
            kmParameters.getGenesFoundInStudy().addAll(Cai2Util.
                    createListFromCommaDelimitedString(kmParameters.getGeneSymbol()));
            kmParameters.getGenesNotFound().clear();
            kmParameters.getGenesNotFound().addAll(analysisService.validateGeneSymbols(getStudySubscription(), 
                    kmParameters.getGenesFoundInStudy()));
            kmParameters.getGenesFoundInStudy().removeAll(kmParameters.getGenesNotFound());
        }
    }
}