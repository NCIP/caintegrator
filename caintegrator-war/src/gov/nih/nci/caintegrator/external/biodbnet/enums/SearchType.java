/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.enums;

/**
 * Enum for choosing what type of search is being performed.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public enum SearchType {
    /**
     * Search by Gene Symbol.
     */
    GENE_ID("Gene Ids"),

    /**
     * Search by Gene Symbol.
     */
    GENE_SYMBOL("Gene Symbols"),

    /**
     * Search by Gene Synonyms.
     */
    GENE_ALIAS("Gene Aliases"),

    /**
     * Search by Gene Pathway.
     */
    PATHWAY("Gene Pathways"),

    /**
     * Search for gene pathways by gene symbol.
     */
    PATHWAY_BY_GENE("Gene Pathways by Gene Symbol");

    private String name;

    /**
     * Default constructor.
     * @param name the name of this type of search
     */
    private SearchType(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
