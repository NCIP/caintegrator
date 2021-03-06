/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.common;

import java.text.DecimalFormat;


/**
 * This is a static utility class to handle the display of Numeric.
 */
public final class NumericUtil {

    private static final DecimalFormat DECIMAL_FORMAT =
        new DecimalFormat("0.####################"); // Up to 20 dec places.
    
    private NumericUtil() {
        
    }
    
    /**
     * 
     * @param displayString to display in our format
     * @return the double display format string
     */
    public static String formatDisplay(String displayString) {
        return DECIMAL_FORMAT.format(Double.valueOf(displayString));
    }
    
    /**
     * 
     * @param value to display in our format
     * @return the double display format string
     */
    public static String formatDisplay(Double value) {
        return DECIMAL_FORMAT.format(value);
    }
}
