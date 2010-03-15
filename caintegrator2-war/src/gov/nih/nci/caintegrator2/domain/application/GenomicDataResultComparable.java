package gov.nih.nci.caintegrator2.domain.application;

/**
 * Interface to sort the GenomicDataResultColumn and GenomicDataResultRow. 
 */
interface GenomicDataResultComparable {

    /**
     * @return the sorted value
     */
    Float getSortedValue();

}