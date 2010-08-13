package gov.nih.nci.caintegrator2.domain.application;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
public class CopyNumberAlterationCriterion extends AbstractGenomicCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private Float upperLimit;
    private Float lowerLimit;
    private SegmentBoundaryTypeEnum segmentBoundaryType = SegmentBoundaryTypeEnum.ONE_OR_MORE;
    private GenomicIntervalTypeEnum genomicIntervalType = GenomicIntervalTypeEnum.GENE_NAME;
    private Float chromosomeCoordinateHigh;
    private Float chromosomeCoordinateLow;
    private Integer chromosomeNumber = 1;

    /**
     * @return the upperLimit
     */
    public Float getUpperLimit() {
        return upperLimit;
    }

    /**
     * @return the upperLimit to display
     */
    public String getDisplayUpperLimit() {
        return (upperLimit == null) ? "" : upperLimit.toString();
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(Float upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * @return the lowerLimit
     */
    public Float getLowerLimit() {
        return lowerLimit;
    }

    /**
     * @return the lowerLimit to display
     */
    public String getDisplayLowerLimit() {
        return (lowerLimit == null) ? "" : lowerLimit.toString();
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(Float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * @return the segmentBoundaryType
     */
    public SegmentBoundaryTypeEnum getSegmentBoundaryType() {
        return segmentBoundaryType;
    }

    /**
     * @param segmentBoundaryType the segmentBoundaryType to set
     */
    public void setSegmentBoundaryType(SegmentBoundaryTypeEnum segmentBoundaryType) {
        this.segmentBoundaryType = segmentBoundaryType;
    }

    /**
     * @return the genomicIntervalType
     */
    public GenomicIntervalTypeEnum getGenomicIntervalType() {
        return genomicIntervalType;
    }

    /**
     * @param genomicIntervalType the genomicIntervalType to set
     */
    public void setGenomicIntervalType(GenomicIntervalTypeEnum genomicIntervalType) {
        this.genomicIntervalType = genomicIntervalType;
    }


    /**
     * @return the chromosomeCoordinateHigh
     */
    public Float getChromosomeCoordinateHigh() {
        return chromosomeCoordinateHigh;
    }

    /**
     * @return the chromosomeCoordinateHigh to display
     */
    public String getDisplayChromosomeCoordinateHigh() {
        return (chromosomeCoordinateHigh == null) ? "" : chromosomeCoordinateHigh.toString();
    }

    /**
     * @param chromosomeCoordinateHigh the chromosomeCoordinateHigh to set
     */
    public void setChromosomeCoordinateHigh(Float chromosomeCoordinateHigh) {
        this.chromosomeCoordinateHigh = chromosomeCoordinateHigh;
    }

    /**
     * @return the chromosomeCoordinateLow
     */
    public Float getChromosomeCoordinateLow() {
        return chromosomeCoordinateLow;
    }

    /**
     * @return the chromosomeCoordinateLow to display
     */
    public String getDisplayChromosomeCoordinateLow() {
        return (chromosomeCoordinateLow == null) ? "" : chromosomeCoordinateLow.toString();
    }

    /**
     * @param chromosomeCoordinateLow the chromosomeCoordinateLow to set
     */
    public void setChromosomeCoordinateLow(Float chromosomeCoordinateLow) {
        this.chromosomeCoordinateLow = chromosomeCoordinateLow;
    }

    /**
     * @return the chromosomeNumber
     */
    public Integer getChromosomeNumber() {
        return chromosomeNumber;
    }

    /**
     * @param chromosomeNumber the chromosomeNumber to set
     */
    public void setChromosomeNumber(Integer chromosomeNumber) {
        this.chromosomeNumber = chromosomeNumber;
    }

    /**
     * {@inheritDoc}
     */
    protected CopyNumberAlterationCriterion clone() throws CloneNotSupportedException {
        return (CopyNumberAlterationCriterion) super.clone();
    }
    
    /**
     * Determines if it is an outside boundary type of query, meaning the upperLimit and lowerLimit
     * values are both non-null and upperLimit < lowerLimit.
     * @return true if the upperLimit < lowerLimit.
     */
    public boolean isOutsideBoundaryType() {
        return upperLimit != null && lowerLimit != null && lowerLimit > upperLimit; 
    }
    
    /**
     * Determines if it is an outside boundary type of query, meaning the upperLimit and lowerLimit
     * values are both non-null and upperLimit >= lowerLimit.
     * @return true if the upperLimit >= lowerLimit.
     */
    public boolean isInsideBoundaryType() {
        return upperLimit != null && lowerLimit != null && lowerLimit <= upperLimit; 
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> getGeneSymbolsInCriterion() {
        return GenomicIntervalTypeEnum.GENE_NAME.equals(genomicIntervalType) 
            ? super.getGeneSymbolsInCriterion() : new ArrayList<String>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName(GenomicCriterionTypeEnum genomicCriterionType) {
        if (GenomicCriterionTypeEnum.COPY_NUMBER.equals(genomicCriterionType)) {
            return getPlatformName();
        }
        return null;
    }
}