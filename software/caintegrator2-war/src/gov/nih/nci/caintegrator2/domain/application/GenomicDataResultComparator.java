/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import java.util.Comparator;


/**
 * A comparator class for Genomic Result by sortedValue.
 */
public class GenomicDataResultComparator implements Comparator <GenomicDataResultComparable> {

    private final int order;
    
    /**
     * @param order sorting direction 1 for ascending and -1 for descending
     */
    public GenomicDataResultComparator(int order) {
        super();
        this.order = order;
    }

    /**
     * {@inheritDoc}
     */
    public int compare(GenomicDataResultComparable arg0, GenomicDataResultComparable arg1) {
        return order * arg0.getSortedValue().compareTo(arg1.getSortedValue());
    }

}
