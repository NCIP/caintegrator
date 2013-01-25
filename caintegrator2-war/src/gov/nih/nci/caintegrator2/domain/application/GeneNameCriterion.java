/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


/**
 * 
 */
public class GeneNameCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    private GenomicCriterionTypeEnum genomicCriterionType;
    
    /**
     * Default constructor.
     */
    public GeneNameCriterion() { 
        // default.
    }
    
    /**
     * Constructor.
     * @param genomicCriterionType genomic type.
     */
    public GeneNameCriterion(GenomicCriterionTypeEnum genomicCriterionType) {
        this.genomicCriterionType = genomicCriterionType;
    }
    
    /**
     * @return the genomicCriterionType
     */
    public GenomicCriterionTypeEnum getGenomicCriterionType() {
        return genomicCriterionType;
    }
    
    /**
     * @param genomicCriterionType the genomicCriterionType to set
     */
    public void setGenomicCriterionType(GenomicCriterionTypeEnum genomicCriterionType) {
        this.genomicCriterionType = genomicCriterionType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName(GenomicCriterionTypeEnum criterionType) {
        if (genomicCriterionType.equals(criterionType)) {
            return getPlatformName();
        }
        return null;
    }
    
    
    
}
