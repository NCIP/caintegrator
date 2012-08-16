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
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class ExpressionLevelCriterionHandlerTest extends AbstractMockitoTest {
    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private Query query;
    private Study study;
    private Gene gene;
    GeneExpressionReporter reporter = new GeneExpressionReporter();

    @Before
    public void setUp() {
        Platform platform = daoStub.getPlatform("platformName");
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        gene = new Gene();
        reporter.getGenes().add(gene);

        daoStub.clear();
        study = new Study();
        query = new Query();
        query.setGeneExpressionPlatform(platform);
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        study.getAssignmentCollection().add(assignment);
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        Array array = new Array();
        array.setPlatform(platform);
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.addReporter(new GeneExpressionReporter());
        arrayData.setSample(sample);
        sample.getSampleAcquisitions().add(acquisition);
        sample.getArrayDataCollection().add(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        study.setStudyConfiguration(studyConfiguration);
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
        criterion.setLowerLimit(1.0f);
        criterion.setGeneSymbol("Tester");
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        Set<ResultRow> rows = new HashSet<ResultRow>();
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
        criterion.setRangeType(RangeTypeEnum.LESS_OR_EQUAL);
        criterion.setUpperLimit(1.0f);
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(0, rows.size());
        verify(arrayDataService, atLeastOnce()).getData(any(DataRetrievalRequest.class));
    }

    @Test
    public void testIsGenomicValueMatchCriterion() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.INSIDE_RANGE);
        criterion.setLowerLimit(1.0f);
        criterion.setUpperLimit(2.0f);
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        Set<Gene> genes = new HashSet<Gene>();
        Gene gene = new Gene();
        gene.setSymbol("Gene");
        genes.add(gene);
        assertEquals(GenomicCriteriaMatchTypeEnum.BETWEEN, handler.getGenomicValueMatchCriterionType(genes, 1.4f));

        criterion.setGeneSymbol("invalid");
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 1.4f));
        criterion.setGeneSymbol("Gene");
        criterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);
        assertEquals(GenomicCriteriaMatchTypeEnum.UNDER, handler.getGenomicValueMatchCriterionType(genes, .9f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 1f));

        assertTrue(handler.hasReporterCriterion());
        assertTrue(handler.isReporterMatchHandler());
    }

    @Test
    public void testGetReporterMatches() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setGeneSymbol("tester");
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        assertEquals(1, handler.getReporterMatches(daoStub, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null).size());

        criterion.setGeneSymbol("");
        assertEquals(1, handler.getReporterMatches(daoStub, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null).size());

    }

    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
            return reporters;
        }

    }

}
