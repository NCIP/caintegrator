/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.columbia.geworkbench.cagrid.MageBioAssayGenerator;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.mageom.domain.bioassay.BioAssay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    
    /**
     * Creates a GctDataset from GenomicDataQueryResults.
     * @param resultSet results to use.
     */
    public GctDataset(GenomicDataQueryResult resultSet) {
        loadGenomicResultSet(resultSet);
    }
    
    /**
     * Creates a GctDataset from the bioAssay object.
     * @param bioAssay contains genomic data.
     * @param mbaGenerator retrieves data from bioAssay object.
     * @param reporterGeneSymbols the reporterGeneSymbols map (since we don't get back description from bioAssays).
     */
    public GctDataset(BioAssay[] bioAssay, MageBioAssayGenerator mbaGenerator, 
            Map<String, String> reporterGeneSymbols) {
        rowReporterNames.addAll(Arrays.asList(mbaGenerator.getRowNamesFromBioAssays(bioAssay)));
        columnSampleNames.addAll(Arrays.asList(mbaGenerator.getColNamesFromBioAssays(bioAssay)));
        for (int i = 0; i < rowReporterNames.size(); i++) {
            String description = reporterGeneSymbols.get(rowReporterNames.get(i));
            rowDescription.add(description == null ? NA_DESCRIPTION_STRING : description);
        }
        values = mbaGenerator.bioAssayArrayToFloat2D(bioAssay);
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
        if (!rowReporter.getGenes().isEmpty()) {
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
    public float[][] getValues() {
        return values.clone();
    }
    
    /**
     * @param values the values to set
     */
    public void setValues(float[][] values) {
        this.values = values.clone();
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
