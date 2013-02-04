/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Gene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An object representing a GctDataset, which can only be created from GenomicDataQueryResults.
 */
public class GctDataset {
    private static final String NA_DESCRIPTION_STRING = "N/A";
    private final List<String> rowReporterNames = new ArrayList<String>();
    private final List<String> rowDescription = new ArrayList<String>();
    private final List<String> columnSampleNames = new ArrayList<String>();
    private float[][] values;
    private final Set<String> subjectsNotFoundFromQueries = new HashSet<String>();
    private final boolean addGenesToReporters;
    
    /**
     * Creates a GctDataset from GenomicDataQueryResults.
     * @param resultSet results to use.
     * @param addGenesToReporters if necessary to add genes to the description column of gct dataset 
     *                              (for performance reasons).
     */
    public GctDataset(GenomicDataQueryResult resultSet, boolean addGenesToReporters) {
        this.addGenesToReporters = addGenesToReporters;
        loadGenomicResultSet(resultSet);
    }
    
    /**
     * Creates a GctDataset from GenomicDataQueryResults.
     * @param resultSet results to use.
     */
    public GctDataset(GenomicDataQueryResult resultSet) {
        this(resultSet, true);
    }

    private void loadGenomicResultSet(GenomicDataQueryResult resultSet) {
        boolean columnNamesMapped = false;
        values = new float[resultSet.getRowCollection().size()][resultSet.getColumnCollection().size()];
        int rowNum = 0;
        for (GenomicDataResultRow row : resultSet.getRowCollection()) {
            AbstractReporter rowReporter = row.getReporter();
            addRowReporters(row, rowReporter);
            addRowValues(columnNamesMapped, rowNum, row);
            columnNamesMapped = true;
            rowNum++;
        }
    }

    private void addRowReporters(GenomicDataResultRow row, AbstractReporter rowReporter) {
        rowReporterNames.add(row.getReporter().getName());
        if (addGenesToReporters && !rowReporter.getGenes().isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Gene:");
            for (Gene gene : rowReporter.getGenes()) {
                sb.append(' ');
                sb.append(gene.getSymbol());
            }
            rowDescription.add(sb.toString());
        } else {
            rowDescription.add(NA_DESCRIPTION_STRING);
        }
    }
    
    private void addRowValues(boolean columnNamesMapped, int rowNum, GenomicDataResultRow row) {
        int colNum = 0;
        for (GenomicDataResultValue value : row.getValues()) {
            values[rowNum][colNum] = value.getValue();
            if (!columnNamesMapped) {
                columnSampleNames.add(value.getColumn().getSampleAcquisition().getSample().getName());
            }
            colNum++;
        }
    }

    
    /**
     * @return the values
     */
    @SuppressWarnings("PMD.MethodReturnsInternalArray") // Cloning the array takes a lot of time and is unnecessary
    public float[][] getValues() {
        return values;
    }

    /**
     * @return the rowReporterNames
     */
    public List<String> getRowReporterNames() {
        return rowReporterNames;
    }
    
    /**
     * @return the columnSampleNames
     */
    public List<String> getColumnSampleNames() {
        return columnSampleNames;
    }

    /**
     * @return the rowDescription
     */
    public List<String> getRowDescription() {
        return rowDescription;
    }
    
    /**
     * @return the subjectsNotFoundFromQueries
     */
    public Set<String> getSubjectsNotFoundFromQueries() {
        return subjectsNotFoundFromQueries;
    }

}
