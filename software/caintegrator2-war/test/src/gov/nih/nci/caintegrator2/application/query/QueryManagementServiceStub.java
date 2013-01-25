/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("PMD")
public class QueryManagementServiceStub implements QueryManagementService {
    public final Set<String> platformsForStudy = new HashSet<String>();
    
    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean executeCalled;
    public QueryResult QR = new QueryResult();
    public boolean executeGenomicDataQueryCalled;
    public boolean retrieveSegmentDataQueryCalled;
    public boolean createCsvFileFromGenomicResultCalled;
    private GenomicDataQueryResult expectedGenomicResult = new GenomicDataQueryResult();
    public boolean getRefreshedEntityCalled;
    public boolean retrieveCopyNumberPlatformsForStudyCalled;
    public boolean retrieveGeneExpressionPlatformsForStudyCalled;
    public boolean annotationForAllSamplesCalled;
    
    public boolean throwGenesNotFoundException = false;

    public void save(Query query) {
        query.setId(1L);
        saveCalled = true;
    }

    public void delete(Query query) {
        deleteCalled = true;
    }
    
    @SuppressWarnings("unchecked")
    public QueryResult execute(Query query) {
        executeCalled = true;
        QR.setQuery(query);
        if (QR.getRowCollection() == null) {
            QR.setRowCollection(Collections.EMPTY_SET);
        }
        return QR;
    }

    @SuppressWarnings("unchecked")
    public QueryResult getAnnotationForAllSamples(Query query) throws InvalidCriterionException {
        annotationForAllSamplesCalled = true;
        QR.setQuery(query);
        if (QR.getRowCollection() == null) {
            QR.setRowCollection(Collections.EMPTY_SET);
        }
        return QR;
    }

    @SuppressWarnings("unchecked")
    public Collection<SegmentData> retrieveSegmentDataQuery(Query query) throws InvalidCriterionException {
        retrieveSegmentDataQueryCalled = true;
        return Collections.EMPTY_SET;
    }

    /**
     * {@inheritDoc}
     */
    public GenomicDataQueryResult executeGenomicDataQuery(Query query) {
        executeGenomicDataQueryCalled = true;
        return expectedGenomicResult;
    }

    public void clear() {
        saveCalled = false;
        executeCalled = false;
        executeGenomicDataQueryCalled = false;
        retrieveSegmentDataQueryCalled = false;
        createCsvFileFromGenomicResultCalled = false;
        getRefreshedEntityCalled = false;
        throwGenesNotFoundException = false;
        retrieveCopyNumberPlatformsForStudyCalled = false;
        retrieveGeneExpressionPlatformsForStudyCalled = false;
        annotationForAllSamplesCalled = false;
        platformsForStudy.clear();
    }


    public NCIADicomJob createDicomJob(List<DisplayableResultRow> checkedRows) {
        return new NCIADicomJob();
    }


    public NCIABasket createNciaBasket(List<DisplayableResultRow> checkedRows) {
        return new NCIABasket();
    }

    /**
     * @return the expectedGenomicResult
     */
    public GenomicDataQueryResult getExpectedGenomicResult() {
        return expectedGenomicResult;
    }

    /**
     * @param expectedGenomicResult the expectedGenomicResult to set
     */
    public void setExpectedGenomicResult(GenomicDataQueryResult expectedGenomicResult) {
        this.expectedGenomicResult = expectedGenomicResult;
    }

    public File createCsvFileFromGenomicResults(GenomicDataQueryResult result) {
        createCsvFileFromGenomicResultCalled = true;
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public List<Query> createQueriesFromSubjectLists(StudySubscription subscription) {
        List<Query> queryList = new ArrayList<Query>();
        for (SubjectList subjectList : subscription.getStudy().getStudyConfiguration().getSubjectLists()) {
            queryList.add(createQueryFromSubjectList(subscription, subjectList));
        }
        for (SubjectList subjectList : subscription.getSubjectLists()) {
            queryList.add(createQueryFromSubjectList(subscription, subjectList));
        }
        return queryList;
    }

    public Query createQueryFromSubjectList(StudySubscription subscription, SubjectList subjectList) {
        Query query = new Query();
        query.setName(subjectList.getName());
        query.setSubjectListQuery(true);
        query.setSubjectListVisibility(subjectList.getVisibility());
        return query;
    }

    
    public <T> T getRefreshedEntity(T entity) {
        getRefreshedEntityCalled = true;
        return entity;
    }

    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        if (throwGenesNotFoundException) {
            throw new GenesNotFoundInStudyException("");
        }
        return new ArrayList<String>();
    }

    public Set<String> retrieveGeneExpressionPlatformsForStudy(Study study) {
        retrieveGeneExpressionPlatformsForStudyCalled = true;
        return platformsForStudy;
    }

    public Set<String> retrieveCopyNumberPlatformsForStudy(Study study) {
        retrieveCopyNumberPlatformsForStudyCalled = true;
        return new HashSet<String>();
    }


    public Set<String> getAllSubjectsNotFoundInCriteria(Query query) throws InvalidCriterionException {
        return new HashSet<String>();
    }

    public Query retrieveQueryToExecute(Query query) throws InvalidCriterionException {
        return query;
    }

    public Set<String> retrieveCopyNumberPlatformsWithCghCallForStudy(Study study) {
        retrieveCopyNumberPlatformsForStudyCalled = true;
        return new HashSet<String>();
    }
    
}
