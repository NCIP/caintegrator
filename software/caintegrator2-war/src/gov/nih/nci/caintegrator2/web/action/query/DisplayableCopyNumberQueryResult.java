/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wraps access to a <code>QueryResult</code> object for easy use in display JSPs.
 */
public final class DisplayableCopyNumberQueryResult {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final GenomicDataQueryResult result;
    private final List<String> sampleHeaders = new ArrayList<String>();
    private final List<DisplayableCopyNumberGeneBasedRow> rows = new ArrayList<DisplayableCopyNumberGeneBasedRow>();
    private final List<DisplayableCopyNumberSampleBasedRow> sampleRows =
        new ArrayList<DisplayableCopyNumberSampleBasedRow>();
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    DisplayableCopyNumberQueryResult(GenomicDataQueryResult result, ResultsOrientationEnum orientation) {
        this.result = result;
        if (ResultsOrientationEnum.SUBJECTS_AS_COLUMNS.equals(orientation)) {
            loadGeneBasedRow();
        } else {
            loadSampleBasedRow();
        }
    }

    private void loadSampleBasedRow() {
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            for (GenomicDataResultRow row : result.getRowCollection()) {
                GenomicDataResultValue value = getValue(row, column);
                if (value != null && value.isMeetsCriterion()) {
                    DisplayableCopyNumberSampleBasedRow displayableRow = new DisplayableCopyNumberSampleBasedRow();
                    displayableRow.setSubject(
                            column.getSampleAcquisition().getAssignment().getIdentifier());
                    displayableRow.setSample(
                            column.getSampleAcquisition().getSample().getName());
                    displayableRow.setChromosome(
                            row.getSegmentDataResultValue().getChromosomalLocation().getChromosome().toString());
                    displayableRow.setStartPosition(
                            row.getSegmentDataResultValue().getChromosomalLocation().getStartPosition().toString());
                    displayableRow.setEndPosition(
                            row.getSegmentDataResultValue().getChromosomalLocation().getEndPosition().toString());
                    displayableRow.setGenes(row.getSegmentDataResultValue().getDisplayGenes());
                    displayableRow.setValue(value);
                    sampleRows.add(displayableRow);
                }
            }
        }
        Collections.sort(sampleRows);
    }
    
    private void loadGeneBasedRow() {
        loadHeaders();
        loadRows();
    }

    private void loadRows() {
        for (GenomicDataResultRow row : result.getRowCollection()) {
            DisplayableCopyNumberGeneBasedRow displayableRow = new DisplayableCopyNumberGeneBasedRow();
            displayableRow.setChromosome(
                    row.getSegmentDataResultValue().getChromosomalLocation().getChromosome().toString());
            displayableRow.setStartPosition(
                    row.getSegmentDataResultValue().getChromosomalLocation().getStartPosition().toString());
            displayableRow.setEndPosition(
                    row.getSegmentDataResultValue().getChromosomalLocation().getEndPosition().toString());
            displayableRow.setGenes(row.getSegmentDataResultValue().getDisplayGenes());
            for (GenomicDataResultColumn column : result.getColumnCollection()) {
                displayableRow.getValues().add(getValue(row, column));
            }
            rows.add(displayableRow);
        }
    }

    private GenomicDataResultValue getValue(GenomicDataResultRow row, GenomicDataResultColumn column) {
        for (GenomicDataResultValue value : row.getValues()) {
            if (value.getColumn().equals(column)) {
                return (value.isMeetsCriterion()) ? value : null;
            }
        }
        return null;
    }

    private void loadHeaders() {
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            sampleHeaders.add(column.getSampleAcquisition().getAssignment().getIdentifier() + "/"
                    + column.getSampleAcquisition().getSample().getName());
        }
    }

    /**
     * Gets the query.
     * 
     * @return the query.
     */
    public Query getQuery() {
        return result.getQuery();
    }
    
    /**
     * Returns the list of sampleHeaders to display.
     * 
     * @return the sampleHeaders.
     */
    public List<String> getSampleHeaders() {
        return sampleHeaders;
    }

    /**
     * @return the rows
     */
    public List<DisplayableCopyNumberGeneBasedRow> getRows() {
        return rows;
    }

    /**
     * @return the number of rows.
     */
    public int getNumberOfRows() {
        return getRows().size();
    }


    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the sampleRows
     */
    public List<DisplayableCopyNumberSampleBasedRow> getSampleRows() {
        return sampleRows;
    }
}
