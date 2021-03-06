/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier;

/**
 * @author caIntegrator Team
*/

public enum ImageTypes {
    JPEG("jpeg"),
    PNG("png");
    final private String value;
    ImageTypes(String s) {
         this.value = s;
    }
    public String getValue() {
        return value;
    }
}
