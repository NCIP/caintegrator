/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;

/**
 * Interface to sort the GenomicDataResultColumn and GenomicDataResultRow. 
 */
interface GenomicDataResultComparable {

    /**
     * @return the sorted value
     */
    Float getSortedValue();

}
