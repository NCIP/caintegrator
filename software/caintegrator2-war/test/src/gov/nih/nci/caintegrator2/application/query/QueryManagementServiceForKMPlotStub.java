/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.analysis.KMPlotStudyCreator;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("PMD")
public class QueryManagementServiceForKMPlotStub extends QueryManagementServiceStub implements QueryManagementService {

    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean executeCalled;
    public QueryResult QR;
    public boolean executeGenomicDataQueryCalled;
    public PlotTypeEnum kmPlotType;
    private KMPlotStudyCreator creator = new KMPlotStudyCreator();
    private CaIntegrator2DaoStub dao;

    public void save(Query query) {
        saveCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Query query) {
        deleteCalled = true;
    }
    
    public QueryResult execute(Query query) {
        executeCalled = true;
        if (kmPlotType == null) {
            QR = creator.retrieveFakeQueryResults(query); 
        } else {
            switch (kmPlotType) {
            case ANNOTATION_BASED:
                QR = creator.retrieveQueryResultForAnnotationBased(query);
                break;
            case GENE_EXPRESSION:
                QR = creator.retrieveFakeQueryResults(query);
                break;
            case QUERY_BASED:
                QR = creator.retrieveFakeQueryResults(query);
                break;
            default:
                return null;
            }
        }
        QR.setQuery(query);
        return QR;
    }

    public GenomicDataQueryResult executeGenomicDataQuery(Query query) {
        executeGenomicDataQueryCalled = true;
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        query.setGeneExpressionPlatform(dao.getPlatform("platformName"));
        result.setQuery(query);
        GenomicDataResultRow row = new GenomicDataResultRow();
        GenomicDataResultValue value = new GenomicDataResultValue();
        GenomicDataResultColumn column = result.addColumn();
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        assignment.setId(Long.valueOf(1));
        sampleAcquisition.setAssignment(assignment);
        column.setSampleAcquisition(sampleAcquisition);
        Sample sample = new Sample();
        sample.setName("sample");
        sampleAcquisition.setSample(sample);
        value.setColumn(column);
        value.setValue(1f);
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        Gene gene = new Gene();
        gene.setSymbol("EGFR");
        reporter.getGenes().add(gene);
        row.setReporter(reporter);
        row.getValues().add(value);
        result.getRowCollection().add(row);
        return result;
    }
    
    @Override
    public Collection<SegmentData> retrieveSegmentDataQuery(Query query) throws InvalidCriterionException {
        Set<SegmentData> segmentDatas = new HashSet<SegmentData>();
        SegmentData segmentData = new SegmentData();
        segmentData.setArrayData(new ArrayData());
        segmentDatas.add(segmentData);
        return segmentDatas;
    }

    public void clear() {
        super.clear();
        saveCalled = false;
        executeCalled = false;
        executeGenomicDataQueryCalled = false;
    }

    public NCIADicomJob createDicomJob(List<DisplayableResultRow> checkedRows) {

        return null;
    }

    public NCIABasket createNciaBasket(List<DisplayableResultRow> checkedRows) {

        return null;
    }

    public File createCsvFileFromGenomicResults(GenomicDataQueryResult result) {
        return null;
    }

    public List<Query> createQueriesFromSubjectLists(Collection<SubjectList> subjectLists) {
        return null;
    }

    /**
     * @return the dao
     */
    public CaIntegrator2DaoStub getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2DaoStub dao) {
        this.dao = dao;
    }
    
}
