/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.search;

/**
 * Wrapper that contains the gene pathway information from bioDbNet.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenePathwayResult implements Comparable<GenePathwayResult> {
    private String name;
    private String title;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(GenePathwayResult other) {
        return this.getName().compareTo(other.getName());
    }
}