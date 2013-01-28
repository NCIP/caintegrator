/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.transfer;


/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class QueryNode {
    private Long annotationDefinitionId;
    private String value;

    /**
     * @return the annotationDefinitionId
     */
    public Long getAnnotationDefinitionId() {
        return annotationDefinitionId;
    }

    /**
     * @param annotationDefinitionId the annotationDefinitionId to set
     */
    public void setAnnotationDefinitionId(Long annotationDefinitionId) {
        this.annotationDefinitionId = annotationDefinitionId;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
