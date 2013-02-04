/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD")
public class QueryManagementServiceStub implements QueryManagementService {

    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean executeCalled;
    public QueryResult QR = new QueryResult();
    public boolean executeGenomicDataQueryCalled;
    public boolean createCsvFileFromGenomicResultCalled;
    private GenomicDataQueryResult expectedGenomicResult = new GenomicDataQueryResult();

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
        createCsvFileFromGenomicResultCalled = false;
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
    
}
