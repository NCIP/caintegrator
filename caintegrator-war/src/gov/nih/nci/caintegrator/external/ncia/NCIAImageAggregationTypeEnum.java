/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;


/**
 * Possible aggregation type values for <code>NCIAImageAggregator.imageAggregationType</code>.
 */
public enum NCIAImageAggregationTypeEnum {

    /**
     * Patient type, (currently unused).
     */
    PATIENT("patient"),
    /**
     * Image Study type.
     */
    IMAGESTUDY("imageStudy"),
    /**
     * Image Series type.
     */
    IMAGESERIES("imageSeries"),

    /**
     * Image type. (currently unused).
     */
    IMAGE("image");

    private String value;

    private NCIAImageAggregationTypeEnum(String value) {
        this.value = value;
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
