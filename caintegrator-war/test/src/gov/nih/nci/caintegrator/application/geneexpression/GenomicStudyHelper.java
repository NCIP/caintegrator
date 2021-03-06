/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

import gov.nih.nci.caintegrator.application.query.QueryManagementServiceImpl;
import gov.nih.nci.caintegrator.application.query.ResultHandler;
import gov.nih.nci.caintegrator.application.query.ResultHandlerImpl;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GenomicStudyHelper {
    private static final String USER_EXISTS = "studyManager";
    private static final String EXP_ID = "caArray Experiment ID 1";
    private GeneExpressionReporter reporter;
    private QueryManagementServiceImpl queryManagementService = new QueryManagementServiceImpl();
    private GenomicDataTestDaoStub daoStub;

    GenomicStudyHelper() {
        daoStub = new GenomicDataTestDaoStub();
        ResultHandler resultHandler = new ResultHandlerImpl();
        daoStub.clear();
        queryManagementService = new QueryManagementServiceImpl();
        queryManagementService.setDao(daoStub);
        queryManagementService.setResultHandler(resultHandler);
    }

    public Query createQuery() {
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        Platform platform = daoStub.getPlatform("platformName");
        query.setGeneExpressionPlatform(platform);
        query.setSubscription(new StudySubscription());
        query.getSubscription().setStudy(new Study());
        Study study = query.getSubscription().getStudy();
        StudyConfiguration studyConfiguration = new StudyConfiguration();

        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setExperimentIdentifier(EXP_ID);
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);

        study.setStudyConfiguration(studyConfiguration);
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_EXISTS);
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setUserWorkspace(userWorkspace);
        studySubscription.setStudy(study);
        query.setSubscription(studySubscription);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        assignment.setId(Long.valueOf(1));
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setGenomicDataSource(genomicDataSourceConfiguration);
        sample.getSampleAcquisitions().add(acquisition);
        Array array = new Array();
        array.setPlatform(platform);
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        sample.getArrayCollection().add(array);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setStudy(study);
        arrayData2.setSample(sample);
        arrayData2.setArray(array);
        array.getArrayDataCollection().add(arrayData2);
        sample.getArrayDataCollection().add(arrayData2);
        array.getSampleCollection().add(sample);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        acquisition.setAssignment(assignment);
        study.getAssignmentCollection().add(assignment);
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        Gene gene = new Gene();
        gene.setSymbol("EGFR");
        reporter = new GeneExpressionReporter();
        ReporterList reporterList =
                platform.addReporterList("reporterList1", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.getGenes().add(gene);
        geneNameCriterion.setGeneSymbol("GENE");
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        reporterList.getReporters().add(reporter);
        reporter.setReporterList(reporterList);
        reporter.setName("Reporter1");
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        ReporterList reporterList2 =
                platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        arrayData2.getReporterLists().add(reporterList2);
        reporterList2.getArrayDatas().add(arrayData2);
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        query.setName("Query1");
        return query;
    }

    private class GenomicDataTestDaoStub extends CaIntegrator2DaoStub  {
        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
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
