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
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * Abstract class representing a handler for Gene Expression plot creation.
 */
public abstract class AbstractGEPlotHandler {

    private final CaIntegrator2Dao dao;
    private final QueryManagementService queryManagementService;
    private final GeneExpressionPlotService gePlotService;

    /**
     * Constructor.
     * @param dao to call database.
     * @param queryManagementService for query execution.
     * @param gePlotService used to create the ge plots.
     */
    protected AbstractGEPlotHandler(CaIntegrator2Dao dao, QueryManagementService queryManagementService,
                                    GeneExpressionPlotService gePlotService) {
        this.dao = dao;
        this.queryManagementService = queryManagementService;
        this.gePlotService = gePlotService;
    }

    /**
     * Creates the GeneExpressionPlotHandler based on Parameters.
     * @param dao to call database.
     * @param queryManagementService for query creation.
     * @param parameters used to determine type of handler to create.
     * @param gePlotService used to create the ge plots.
     * @return handler.
     */
    public static AbstractGEPlotHandler createGeneExpressionPlotHandler(CaIntegrator2Dao dao,
                                                                        QueryManagementService queryManagementService,
                                                                        AbstractGEPlotParameters parameters,
                                                                        GeneExpressionPlotService gePlotService) {
        if (parameters instanceof GEPlotAnnotationBasedParameters) {
            return new AnnotationBasedGEPlotHandler(dao, queryManagementService,
                                                   (GEPlotAnnotationBasedParameters) parameters,
                                                   gePlotService);
        } else if (parameters instanceof GEPlotGenomicQueryBasedParameters) {
            return new GenomicQueryBasedGEPlotHandler(dao, queryManagementService,
                                                    (GEPlotGenomicQueryBasedParameters) parameters,
                                                    gePlotService);
        } else if (parameters instanceof GEPlotClinicalQueryBasedParameters) {
            return new ClinicalQueryBasedGEPlotHandler(dao, queryManagementService,
                    (GEPlotClinicalQueryBasedParameters) parameters, gePlotService);
}
        throw new IllegalArgumentException("Unknown Parameter Type");
    }

    /**
     * Creates the GeneExpressionPlotGroup for the parameters.
     * @param subscription that user is currently using.
     * @return plot group.
     * @throws ControlSamplesNotMappedException when a control sample is not mapped.
     * @throws InvalidCriterionException if the criterion is not valid for the query.
     */
    public abstract GeneExpressionPlotGroup createPlots(StudySubscription subscription)
        throws ControlSamplesNotMappedException, InvalidCriterionException;

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * Creates the plot.
     * @param parameters input for graph.
     * @param configuration to add genes not found.
     * @return the gene expression plot group.
     */
    protected GeneExpressionPlotGroup createGeneExpressionPlot(AbstractGEPlotParameters parameters,
                                                               GeneExpressionPlotConfiguration configuration) {
        configuration.getGenesNotFound().addAll(parameters.getGenesNotFound());
        return gePlotService.generatePlots(configuration);
    }

    /**
     * Retrieves the control group criterion for the study subscription.
     * @param subscription to get control samples for.
     * @param controlSampleSetName to retrieve for.
     * @return criterion on the ID's of the control samples.
     * @throws ControlSamplesNotMappedException when a control sample is not mapped.
     */
    protected AbstractCriterion retrieveControlGroupCriterion(StudySubscription subscription,
            String controlSampleSetName)
    throws ControlSamplesNotMappedException {
        CompoundCriterion idCriteria = new CompoundCriterion();
        idCriteria.setBooleanOperator(BooleanOperatorEnum.OR);
        idCriteria.setCriterionCollection(new HashSet<AbstractCriterion>());
        for (Sample sample : subscription.getStudy().getControlSampleSet(controlSampleSetName).getSamples()) {
            if (CollectionUtils.isEmpty(sample.getSampleAcquisitions())) {
                throw new ControlSamplesNotMappedException("Sample '"  + sample.getName()
                        + "' is not mapped to a patient.");
            }
            IdentifierCriterion idCriterion = new IdentifierCriterion();
            idCriterion.setStringValue(sample.getSampleAcquisitions()
                    .iterator().next().getAssignment().getIdentifier());
            idCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
            idCriterion.setEntityType(EntityTypeEnum.SUBJECT);
            idCriteria.getCriterionCollection().add(idCriterion);
        }
        return idCriteria;
    }

    /**
     * Retrieves the used subjects criterion.
     * @param usedSubjects set of used subjects.
     * @return compound criterion of used subjects.
     */
    protected CompoundCriterion retrieveUsedSubjectsCriterion(Set<StudySubjectAssignment> usedSubjects) {
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

    /**
     * Sample Group Type enumeration.
     */
    protected enum SampleGroupType {
        /**
         * Default.
         */
        DEFAULT,
        /**
         * Others Subjects.
         */
        OTHERS_GROUP,
        /**
         * Control samples group.
         */
        CONTROL_GROUP;
    }

}
