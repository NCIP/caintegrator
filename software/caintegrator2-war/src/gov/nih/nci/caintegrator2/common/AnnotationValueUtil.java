/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;


/**
 * This is a static utility class used by the UI to display the annotation value. 
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity" }) // Checking for type and null.
public final class AnnotationValueUtil {

    private AnnotationValueUtil() {
        
    }
    
    /**
     * @param abstractAnnotationValues abstractAnnotationValues
     * @param dataValues the values from the current upload file
     * @param filterList the list of display string to filter
     * @return set of distinct available values
     */
    public static Set<String> getAdditionalValue(Collection<AbstractAnnotationValue> abstractAnnotationValues,
            List<String>dataValues, Set<String> filterList) {
        Set<String> results = new HashSet<String>();
        for (String dataValue : dataValues) {
            if (dataValue != null && !filterList.contains(dataValue)) {
                if (NumberUtils.isNumber(dataValue)) {
                    dataValue = new DecimalFormat(NumericAnnotationValue.DECIMAL_FORMAT).format(
                            Double.valueOf(dataValue));
                }
                results.add(dataValue);
            }
        }
        for (AbstractAnnotationValue abstractAnnotationValue : abstractAnnotationValues) {
            String displayString = abstractAnnotationValue.toString();
            if (displayString != null && !filterList.contains(displayString)) {
                results.add(displayString);
            }
        }
        return results;
    }
    
}
