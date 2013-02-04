/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.heatmap;

/**
 * Wrapper for the GeneChromosomalLocation intended to bridge the gap between caIntegrator and CBS2Heatmap.
 */
public class GeneLocationWrapper {
    
    private String geneSymbol;
    private String chromosome;
    private Integer startPosition;
    private Integer endPosition;
    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }
    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }
    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
    }
    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }
    /**
     * @return the startPosition
     */
    public Integer getStartPosition() {
        return startPosition;
    }
    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }
    /**
     * @return the endPosition
     */
    public Integer getEndPosition() {
        return endPosition;
    }
    /**
     * @param endPosition the endPosition to set
     */
    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }
    
    
}
