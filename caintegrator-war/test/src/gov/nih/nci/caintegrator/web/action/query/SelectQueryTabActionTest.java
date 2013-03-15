/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.query.DisplayableCopyNumberQueryResult;
import gov.nih.nci.caintegrator.web.action.query.DisplayableQueryResult;
import gov.nih.nci.caintegrator.web.action.query.SelectQueryTabAction;

import org.junit.Test;

/**
 *
 */
public class SelectQueryTabActionTest extends AbstractSessionBasedTest {
    private SelectQueryTabAction action;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action = new SelectQueryTabAction();
    }


    @Test
    public void selectQueryTabAction() throws Exception {
        action.setSelectedAction("selectSortingTab");
        assertEquals("selectSortingTab", action.getSelectedAction());
        assertEquals("selectSortingTab", action.execute());
        action.setSelectedAction("selectSortingTab,Dummy");
        assertEquals("selectSortingTab", action.getSelectedAction());
        assertEquals(20, action.getPageSize());
        action.setQueryId(1L);
        assertEquals(1, action.getQueryId().longValue());
    }

    /**
     * Tests page size functionality.
     */
    @Test
    public void pageSize() {
        action.setSelectedAction("selectSortingTab");
        assertEquals(20, action.getPageSize());

        DisplayableUserWorkspace workspace = SessionHelper.getInstance().getDisplayableUserWorkspace();
        QueryResult results = new QueryResult();
        results.setQuery(new Query());

        DisplayableQueryResult displayableResults = new DisplayableQueryResult(results);
        displayableResults.setPageSize(10);
        workspace.setQueryResult(displayableResults);
        workspace.setCopyNumberQueryResult(null);
        assertEquals(10, action.getPageSize());

        DisplayableCopyNumberQueryResult copyResults =
                new DisplayableCopyNumberQueryResult(new GenomicDataQueryResult(), ResultsOrientationEnum.SUBJECTS_AS_ROWS);
        copyResults.setPageSize(30);
        workspace.setQueryResult(null);
        workspace.setCopyNumberQueryResult(copyResults);
        assertEquals(30, action.getPageSize());
    }
}
