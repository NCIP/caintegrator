/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;

import java.util.Comparator;


/**
 * A comparator class for Genomic Result Rows, currently sorts based on Gene Name and then Reporter name.
 */
public class GenomicDataResultRowComparator implements Comparator <GenomicDataResultRow> {

    /**
     * {@inheritDoc}
     */
    public int compare(GenomicDataResultRow row1, GenomicDataResultRow row2) {
        return (row1.getReporter() != null)
            ? row1.getReporter().compareTo(row2.getReporter())
            : row1.getSegmentDataResultValue().compareTo(row2.getSegmentDataResultValue());
    }

}
