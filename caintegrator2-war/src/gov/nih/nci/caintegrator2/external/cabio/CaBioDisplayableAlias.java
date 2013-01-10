/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class CaBioDisplayableAlias implements Comparable<CaBioDisplayableAlias> {
    private static final int MAX_NAME_LENGTH = 19;

    private String id;
    private String name;
    private boolean checked = true;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name substring.
     */
    public String getDisplayableName() {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        return name.length() > MAX_NAME_LENGTH
            ? name.substring(0, MAX_NAME_LENGTH - 3) + "..." : name;
    }
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
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }
    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    /**
     * {@inheritDoc}
     */
    public int compareTo(CaBioDisplayableAlias o) {
        return this.getName().compareTo(o.getName());
    }

}
