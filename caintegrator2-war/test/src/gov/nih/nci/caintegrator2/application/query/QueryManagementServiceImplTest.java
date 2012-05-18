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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AuthorizedAnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AuthorizedQuery;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregationTypeEnum;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableQueryResultTest;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.exceptions.CSException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for QueryManagementServiceImpl
 */
public class QueryManagementServiceImplTest {


    private static final String USER_EXISTS = "studyManager";
    private static final String AFD_NAME = "Gender";
    private static final String EXP_ID = "caArray Experiment ID 1";
    private QueryManagementServiceImpl queryManagementService;
    private CaIntegrator2DaoStub dao;
    private Query query;
    private GeneExpressionReporter reporter;
    private ArrayDataServiceStub arrayDataService;
    private SegmentData segmentData;
    private SegmentData segmentData2;
    private UserWorkspace userWorkspace;

    @Before
    public void setup() {
        dao = new GenomicDataTestDaoStub();
        ResultHandler resultHandler = new ResultHandlerImpl();
        dao.clear();
        arrayDataService = new ArrayDataServiceStub();
        queryManagementService = new QueryManagementServiceImpl();
        queryManagementService.setDao(dao);
        queryManagementService.setArrayDataService(arrayDataService);
        queryManagementService.setResultHandler(resultHandler);
        query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setUserWorkspace(userWorkspace);
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        study = studyConfiguration.getStudy();
        study.setShortTitleText("Test study by QueryManagementServiceImpl");
        study.setStudyConfiguration(studyConfiguration);
        // setup AFD and AnnotationGroup
        AnnotationDefinition ad = new AnnotationDefinition();
        ad.setDisplayName(AFD_NAME);
        ad.setDataType(AnnotationTypeEnum.STRING);
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setName(AFD_NAME);
        afd.setType(AnnotationFieldType.ANNOTATION);
        afd.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        afd.setDefinition(ad);
        StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
        studySubjectAssignment.setId(Long.valueOf(1));
        studySubjectAssignment.setIdentifier("SubjectID1");
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        StringAnnotationValue stringAnnotationValue = new StringAnnotationValue();
        stringAnnotationValue.setStringValue("M");
        stringAnnotationValue.setAnnotationDefinition(ad);
        subjectAnnotation.setAnnotationValue(stringAnnotationValue);
        studySubjectAssignment.getSubjectAnnotationCollection().add(subjectAnnotation);
        study.getAssignmentCollection().add(studySubjectAssignment);
        AnnotationGroup group = new AnnotationGroup();
        group.setName("group");
        group.getAnnotationFieldDescriptors().add(afd);
        study.getAnnotationGroups().add(group);
        studySubscription.setStudy(study);
        query.setSubscription(studySubscription);
        query.getSubscription().setStudy(study);
        query.getSubscription().setUserWorkspace(userWorkspace);
        segmentData = new SegmentData();
        ChromosomalLocation location = new ChromosomalLocation();
        location.setStartPosition(1);
        location.setEndPosition(2);
        segmentData.setLocation(location);
        segmentData2 = new SegmentData();
        segmentData2.setLocation(location);
    }


    @Test
    public void testExecute() throws InvalidCriterionException {
        QueryResult queryResult = queryManagementService.execute(query);
        assertNotNull(queryResult.getRowCollection());
        assertFalse(query.isHasMaskedValues());
        NumericComparisonCriterion numericCrit = new NumericComparisonCriterion();
        numericCrit.setEntityType(EntityTypeEnum.SUBJECT);
        numericCrit.setNumericValue(12d);
        numericCrit.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);

        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        afd.setMaxNumber(10d);
        afd.setNumericRange(4);
        afd.setDefinition(new AnnotationDefinition());
        numericCrit.setAnnotationFieldDescriptor(afd);
        ResultColumn column = new ResultColumn();
        column.setAnnotationFieldDescriptor(afd);
        query.getColumnCollection().add(column);
        query.getCompoundCriterion().getCriterionCollection().add(numericCrit);
        queryManagementService.execute(query);
        assertTrue(query.isHasMaskedValues());

        // Test SubjectList with all matches
        SubjectList sl1 = new SubjectList();
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier("real");
        sl1.getSubjectIdentifiers().add(subjectIdentifier);

        StudySubjectAssignment subject1 = new StudySubjectAssignment();
        subject1.setIdentifier("real");
        query.getSubscription().getStudy().getAssignmentCollection().add(subject1);
        query.getCompoundCriterion().getCriterionCollection().clear();
        SubjectListCriterion subjectListCriterion = new SubjectListCriterion();
        subjectListCriterion.getSubjectListCollection().add(sl1);
        query.getCompoundCriterion().getCriterionCollection().add(subjectListCriterion);
        queryResult = queryManagementService.execute(query);
        assertTrue(queryResult.getQuery().getSubjectIdsNotFound().isEmpty());

        // Test 2 subject lists, one matches, and one doesn't.
        SubjectList sl2 = new SubjectList();
        SubjectIdentifier subjectIdentifier2 = new SubjectIdentifier();
        subjectIdentifier2.setIdentifier("fake");
        sl2.getSubjectIdentifiers().add(subjectIdentifier2);
        subjectListCriterion.getSubjectListCollection().add(sl2);
        queryResult = queryManagementService.execute(query);
        assertTrue(queryResult.getQuery().getSubjectIdsNotFound().contains("fake"));


        // Test 1 subject list, where neither matches.
        subjectListCriterion.getSubjectListCollection().clear();
        subjectListCriterion.getSubjectListCollection().add(sl2);
        try {
            queryResult = queryManagementService.execute(query);
            fail("Should catch invalid criterion exception because there are no valid subject IDs in query.");
        } catch (InvalidCriterionException e) {
            // noop - expect it to come here.
        }

    }

    @Test
    @SuppressWarnings("PMD")
    public void testExecuteGenomicDataQuery() throws InvalidCriterionException {
        Platform platform = dao.getPlatform("platformName");
        Study study = query.getSubscription().getStudy();
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        study.getStudyConfiguration().setUserWorkspace(userWorkspace);
        query.getSubscription().setUserWorkspace(userWorkspace);
        StudySubjectAssignment assignment = query.getSubscription().getStudy().getAssignmentCollection().iterator().next();
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        acquisition.setAssignment(assignment);
        sample.setSampleAcquisition(acquisition);
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setExperimentIdentifier(EXP_ID);
        study.getStudyConfiguration().getGenomicDataSources().add(genomicDataSourceConfiguration);
        sample.setGenomicDataSource(genomicDataSourceConfiguration);
        Array array = new Array();
        array.setPlatform(platform);
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        segmentData.setSegmentValue(.1f);
        arrayData.getSegmentDatas().add(segmentData);
        segmentData.setArrayData(arrayData);
        sample.getArrayCollection().add(array);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setStudy(study);
        arrayData2.setSample(sample);
        arrayData2.setArray(array);
        segmentData2.setSegmentValue(.4f);
        arrayData2.getSegmentDatas().add(segmentData2);
        segmentData2.setArrayData(arrayData2);
        array.getArrayDataCollection().add(arrayData2);
        sample.getArrayDataCollection().add(arrayData2);
        array.getSampleCollection().add(sample);
        acquisition.setSample(sample);
        acquisition.setAssignment(assignment);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        study.getAssignmentCollection().add(assignment);
        try {
            authorizeStudyElements(study.getStudyConfiguration());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //////////////////////////////////
        // Gene Name Criterion Testing. //
        //////////////////////////////////
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        Gene gene = new Gene();
        gene.setSymbol("GENE");
        reporter = new GeneExpressionReporter();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setReporterList(reporterList);
        reporter.getGenes().add(gene);
        geneNameCriterion.setGeneSymbol("GENE");
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        reporterList.getReporters().add(reporter);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        ReporterList reporterList2 = platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        arrayData2.getReporterLists().add(reporterList2);
        reporterList2.getArrayDatas().add(arrayData2);

        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        try {
            arrayDataService.numberPlatformsInStudy = 2;
            queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because no platforms selected.");
        } catch (InvalidCriterionException e) {
        }
        arrayDataService.numberPlatformsInStudy = 1;
        geneNameCriterion.setPlatformName("platformName");
        query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);

        assertEquals(1, result.getFilteredRowCollection().size());
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getFilteredRowCollection().iterator().next().getValues().size());
        GenomicDataResultColumn column = result.getColumnCollection().iterator().next();
        assertNotNull(column.getSampleAcquisition());
        assertNotNull(column.getSampleAcquisition().getSample());

        /////////////////////////////////////////
        // Expression Level Criterion Testing. //
        /////////////////////////////////////////
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setPlatformName("platformName");
        expressionLevelCriterion.setGeneSymbol("GENE");
        expressionLevelCriterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
        expressionLevelCriterion.setLowerLimit(1f);
        query.getCompoundCriterion().getCriterionCollection().clear();
        query.getCompoundCriterion().getCriterionCollection().add(expressionLevelCriterion);
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getFilteredRowCollection().iterator().next().getValues().size());
        column = result.getColumnCollection().iterator().next();
        assertNotNull(column.getSampleAcquisition());
        assertNotNull(column.getSampleAcquisition().getSample());

        expressionLevelCriterion.setRangeType(RangeTypeEnum.LESS_OR_EQUAL); // No upper limit = always true.
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getFilteredRowCollection().iterator().next().getValues().size());

        expressionLevelCriterion.setUpperLimit(1.1f);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getFilteredRowCollection().size());
        assertEquals(0, result.getColumnCollection().size());


        ////////////////////////////////////
        // Fold Change Criterion Testing. //
        ////////////////////////////////////
        FoldChangeCriterion foldChangeCriterion = new FoldChangeCriterion();
        foldChangeCriterion.setFoldsUp(1.0f);
        foldChangeCriterion.setGeneSymbol("GENE");
        foldChangeCriterion.setControlSampleSetName("controlSampleSet1");
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UP);
        query.getCompoundCriterion().getCriterionCollection().clear();
        query.getCompoundCriterion().getCriterionCollection().add(foldChangeCriterion);
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        try {
            result = queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because no control samples in study");
        } catch (InvalidCriterionException e) {
        }
        SampleSet sampleSet1 = new SampleSet();
        sampleSet1.setName("controlSampleSet1");
        sampleSet1.getSamples().add(new Sample());
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet1);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        foldChangeCriterion.setFoldsDown(1.0f);
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getFilteredRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UP_OR_DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UNCHANGED);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getFilteredRowCollection().size());
        try {
            foldChangeCriterion.setGeneSymbol("EGFR");
            result = queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because genes are not found.");
        } catch (InvalidCriterionException e) {
        }

        ReporterList reporterList3 = platform.addReporterList("reporterList3", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        arrayData.getReporterLists().add(reporterList3);
        reporterList3.getArrayDatas().add(arrayData);
        arrayData2.getReporterLists().add(reporterList3);
        reporterList3.getArrayDatas().add(arrayData2);
        CopyNumberAlterationCriterion copyNumberCriterion = new CopyNumberAlterationCriterion();
        query.setResultType(ResultTypeEnum.COPY_NUMBER);
        query.getCompoundCriterion().getCriterionCollection().clear();
        query.getCompoundCriterion().getCriterionCollection().add(copyNumberCriterion);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        ChromosomalLocation location = new ChromosomalLocation();
        location.setStartPosition(1);
        location.setEndPosition(4);
        segmentData2.setLocation(location);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(2, result.getFilteredRowCollection().size());
        // test copy number - calls value
        query.getCompoundCriterion().getCriterionCollection().clear();
        Set<Integer> callsValues = new HashSet<Integer>();
        callsValues.add(Integer.decode("1"));
        copyNumberCriterion.setCallsValues(callsValues);
        query.getCompoundCriterion().getCriterionCollection().add(copyNumberCriterion);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(2, result.getFilteredRowCollection().size());

    }


    @Test
    public void testSave() {
       queryManagementService.save(query);
       assertTrue(dao.saveCalled);
       query.setId(Long.valueOf(1));
       queryManagementService.save(query);
       assertTrue(dao.mergeCalled);
    }

    @Test
    public void testDelete() {
       queryManagementService.delete(query);
       assertTrue(dao.deleteCalled);
    }

    @Test
    public void testCreateDicomJob() {
        queryManagementService.setDao(new ImageStudyTestDaoStub());
        NCIADicomJob dicomJob = queryManagementService.createDicomJob(DisplayableQueryResultTest.
                                                                      getImagingSeriesResult().
                                                                      getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESERIES, dicomJob.getImageAggregationType());
        assertTrue(!dicomJob.getImageSeriesIDs().isEmpty());
        assertTrue(dicomJob.getImageStudyIDs().isEmpty());

        dicomJob = queryManagementService.createDicomJob(DisplayableQueryResultTest.
                                                        getImageStudyResult().
                                                        getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESTUDY, dicomJob.getImageAggregationType());
        assertTrue(dicomJob.getImageSeriesIDs().isEmpty());
        assertTrue(!dicomJob.getImageStudyIDs().isEmpty());
    }

    @Test
    public void testCreateNciaBasket() {
        queryManagementService.setDao(new ImageStudyTestDaoStub());
        NCIABasket nciaBasket = queryManagementService.createNciaBasket(DisplayableQueryResultTest.
                                                                      getImagingSeriesResult().
                                                                      getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESERIES, nciaBasket.getImageAggregationType());
        assertTrue(!nciaBasket.getImageSeriesIDs().isEmpty());
        assertTrue(nciaBasket.getImageStudyIDs().isEmpty());

        nciaBasket = queryManagementService.createNciaBasket(DisplayableQueryResultTest.
                                                        getImageStudyResult().
                                                        getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESTUDY, nciaBasket.getImageAggregationType());
        assertTrue(nciaBasket.getImageSeriesIDs().isEmpty());
        assertTrue(!nciaBasket.getImageStudyIDs().isEmpty());
    }


    private class GenomicDataTestDaoStub extends CaIntegrator2DaoStub  {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter );
            return reporters;
        }

        @Override
        public List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion,
                Study study, Platform platform) {
            List<SegmentData> segmentDatas = new ArrayList<SegmentData>();
            segmentDatas.add(segmentData);
            segmentDatas.add(segmentData2);
            return segmentDatas;
        }

        @Override
        public List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch,
                Study study, Platform platform) {
            return findMatchingSegmentDatas(null, study, platform);
        }
    }

    @SuppressWarnings("unchecked")
    private static class ImageStudyTestDaoStub extends CaIntegrator2DaoStub {
        @Override
        public <T> T get(Long id, Class<T> objectClass) {
            return (T) DisplayableQueryResultTest.retrieveStudySubjectAssignment(id);
        }

    }

    private void authorizeStudyElements(StudyConfiguration studyConfiguration)
    throws ConnectionException, DataRetrievalException, ValidationException, IOException, InvalidCriterionException, CSException {

            AuthorizedStudyElementsGroup authorizedStudyElementsGroup1 = new AuthorizedStudyElementsGroup();
            authorizedStudyElementsGroup1 = createAuthorizedStudyElementsGroup(studyConfiguration,"IntegrationTestAuthorizedStudyElementsGroup1","Gender", "F");
            List<AuthorizedStudyElementsGroup> list = new ArrayList<AuthorizedStudyElementsGroup>();
            list.add(authorizedStudyElementsGroup1);
            studyConfiguration.setAuthorizedStudyElementsGroups(list);

            AuthorizedStudyElementsGroup authorizedStudyElementsGroup2 = new AuthorizedStudyElementsGroup();
            authorizedStudyElementsGroup2 = createAuthorizedStudyElementsGroup(studyConfiguration,"IntegrationTestAuthorizedStudyElementsGroup2","Age", StringUtils.EMPTY);
            list.add(authorizedStudyElementsGroup2);
            studyConfiguration.setAuthorizedStudyElementsGroups(list);
    }

    /**
     * This method creates and returns an AuthorizedStudyElementsGroup that
     * consists of elements from the current studyConfiguration.
     * @param studyConfiguration
     * @param authorizedStudyElementsGroupName
     * @param fieldDescriptorName
     * @param annotationValue
     * @return authorizedStudyElementsGroup
     */
    protected AuthorizedStudyElementsGroup createAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                                                                String authorizedStudyElementsGroupName,
                                                                                String fieldDescriptorName,
                                                                                String annotationValue) {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfiguration);
        String desc = "Created by integration test for study named: " + studyConfiguration.getStudy().getShortTitleText();
        Group group = new Group();
        group.setGroupName(authorizedStudyElementsGroupName);
        group.setGroupDesc(desc);

        authorizedStudyElementsGroup.setAuthorizedGroup(group);
        // add AuthorizedAnnotationFieldDescriptor
        AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor = studyConfiguration.getExistingFieldDescriptorInStudy(fieldDescriptorName);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor = new AuthorizedAnnotationFieldDescriptor();
        authorizedAnnotationFieldDescriptor.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedAnnotationFieldDescriptor.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        authorizedStudyElementsGroup.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor);
        // add AuthorizedGenomicDataSourceConfigurations
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration = new AuthorizedGenomicDataSourceConfiguration();
        authorizedGenomicDataSourceConfiguration.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedGenomicDataSourceConfiguration.setGenomicDataSourceConfiguration(studyConfiguration.getGenomicDataSources().get(0));
        authorizedStudyElementsGroup.getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration);
        // add AuthorizedQuery
        Query query = new Query();
        query.setName("TestAuthorizationQuery");
        query.setDescription(desc);

        for (StudySubscription studySubscription : userWorkspace.getSubscriptionCollection()) {
            if (studySubscription.getStudy().getId().equals(studyConfiguration.getStudy().getId())) {
                query.setSubscription(studySubscription);
            }
        }

        query.setLastModifiedDate(new Date());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        StringComparisonCriterion stringComparisonCriterion = new StringComparisonCriterion();
        stringComparisonCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        stringComparisonCriterion.setStringValue(annotationValue);
        stringComparisonCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        AbstractCriterion abstractCriterion = new AbstractAnnotationCriterion();
        abstractCriterion = stringComparisonCriterion;
        HashSet<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
        abstractCriterionCollection.add(abstractCriterion);
        query.getCompoundCriterion().setCriterionCollection(abstractCriterionCollection);
        AuthorizedQuery authorizedQuery = new AuthorizedQuery();
        authorizedQuery.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedQuery.setQuery(query);
        authorizedStudyElementsGroup.getAuthorizedQuerys().add(authorizedQuery);
        return authorizedStudyElementsGroup;
    }

}
