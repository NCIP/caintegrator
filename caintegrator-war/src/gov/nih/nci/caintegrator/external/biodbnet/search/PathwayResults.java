/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.search;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Wrapper that contains the results of a pathway by gene search.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class PathwayResults implements Comparable<PathwayResults> {
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
    public int compareTo(PathwayResults other) {
        return this.getName().compareTo(other.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PathwayResults)) {
            return false;
        } else if (obj == this) {
            return true;
        }
        PathwayResults other = (PathwayResults) obj;
        return this.getName().equals(other.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }
}
