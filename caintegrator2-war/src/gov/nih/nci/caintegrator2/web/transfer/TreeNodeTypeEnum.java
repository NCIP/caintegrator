/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.transfer;

/**
 * Enum for categorizing tree nodes.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public enum TreeNodeTypeEnum {

    /**
     * Data type.
     */
    DATA_SOURCE,

    /**
     * Field descriptor.
     */
    FIELD_DESCRIPTOR,

    /**
     * Parent.
     */
    PARENT;
}
