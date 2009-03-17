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
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceImpl;
import gov.nih.nci.caintegrator2.application.query.ResultHandler;
import gov.nih.nci.caintegrator2.application.query.ResultHandlerImpl;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataMatrix;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

/**
 * 
 */
public class GenomicStudyHelper {
    private GeneExpressionReporter reporter;
    private QueryManagementServiceImpl queryManagementService = new QueryManagementServiceImpl();
    private GenomicDataTestDaoStub daoStub;
    
    GenomicStudyHelper() {
        daoStub = new GenomicDataTestDaoStub();
        ResultHandler resultHandler = new ResultHandlerImpl();
        daoStub.clear();
        ArrayDataServiceStub arrayDataService = new ArrayDataServiceStub();
        queryManagementService = new QueryManagementServiceImpl();
        queryManagementService.setDao(daoStub);
        queryManagementService.setArrayDataService(arrayDataService);
        queryManagementService.setResultHandler(resultHandler);
    }
    
    public Query createQuery() {
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(new StudySubscription());
        query.getSubscription().setStudy(new Study());
        Study study = query.getSubscription().getStudy();
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setSampleAcquisition(acquisition);
        Array array = new Array();
        ArrayData arrayData = new ArrayData();
        arrayData.setArray(array);
        array.setArrayDataCollection(new HashSet<ArrayData>());
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        sample.getArrayCollection().add(array);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setSample(sample);
        array.getArrayDataCollection().add(arrayData2);
        sample.getArrayDataCollection().add(arrayData2);        
        array.setSampleCollection(new HashSet<Sample>());
        array.getSampleCollection().add(sample);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        study.getAssignmentCollection().add(assignment);
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        Gene gene = new Gene();
        reporter = new GeneExpressionReporter();
        ReporterList reporterList = new ReporterList();
        reporter.setReporterList(reporterList);
        reporterList.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setGene(gene);
        geneNameCriterion.setGeneSymbol("GENE");
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        ArrayDataMatrix matrix = new ArrayDataMatrix();
        matrix.setReporterList(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        matrix.getReporterList().getReporters().add(reporter);
        reporter.setReporterList(reporterList);
        reporter.setName("Reporter1");
        matrix.getReporterList().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        arrayData.setReporterList(matrix.getReporterList());
        matrix.getReporterList().getArrayDatas().add(arrayData);
        arrayData2.setReporterList(new ReporterList());
        arrayData2.getReporterList().getArrayDatas().add(arrayData2);
        arrayData2.getReporterList().setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        daoStub.matrix = matrix;
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        query.setName("Query1");
        return query;
    }
    
    private class GenomicDataTestDaoStub extends CaIntegrator2DaoStub  {

        ArrayDataMatrix matrix;
        
        @Override
        public List<ArrayDataMatrix> getArrayDataMatrixes(Study study, ReporterTypeEnum reporterType) {
            List<ArrayDataMatrix> matrixes = new ArrayList<ArrayDataMatrix>();
            matrixes.add(matrix);
            return matrixes;
        }

        @Override
        public Set<GeneExpressionReporter> findGeneExpressionReporters(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study) {
            Set<GeneExpressionReporter> reporters = new HashSet<GeneExpressionReporter>();
            reporters.add(reporter );
            return reporters;
        }
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementServiceImpl getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementServiceImpl queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the daoStub
     */
    public GenomicDataTestDaoStub getDaoStub() {
        return daoStub;
    }

    /**
     * @param daoStub the daoStub to set
     */
    public void setDaoStub(GenomicDataTestDaoStub daoStub) {
        this.daoStub = daoStub;
    }
    
    

}
