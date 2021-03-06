/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;



/**
 * Plot types.
 */
public enum GenomicCriteriaMatchTypeEnum {
    
    /**
     * Value is under.
     */
    UNDER("#0066CC"), // BLUE - negative

    /**
     * Value is over.
     */
    OVER("#CC3333"), // RED - positive
    
    /**
     * Value is between.
     */
    BETWEEN("#CC3333"), // RED
    
    /**
     * Value match somewhere, and the color is based on whether it is a positive or negative number.
     */
    MATCH_POSITIVE_OR_NEGATIVE(""),
        
    /**
     * No match.
     */
    NO_MATCH("");

    private String highlightColor;
    
    private GenomicCriteriaMatchTypeEnum(String highlightColor) {
        setHighlightColor(highlightColor);
    }

    /**
     * @return the value
     */
    public String getHighlightColor() {
        return highlightColor;
    }

    private void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    
}
