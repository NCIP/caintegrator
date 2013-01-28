/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import java.util.Comparator;

/**
 * Comparator used for DisplayTag columns that are more than likely numeric.  First tries to do
 * a comparison casting both values to Double, if an exception is thrown it falls back to doing a
 * basic String comparison on the two values.  The Objects are both expected to be non-null.
 */
public class NumericColumnDisplayTagComparator implements Comparator<Object> {

    /**
     * {@inheritDoc}
     */
    public int compare(Object o1, Object o2) {
        String string1 = createStringFromObject(o1);
        String string2 = createStringFromObject(o2);
        try {
            return Double.valueOf(string1).compareTo(Double.valueOf(string2)); 
        } catch (Exception e) {
            return string1.compareTo(string2);
        }      
    }

    private String createStringFromObject(Object o) {
        String string = o.toString();
        string = string.replace("Cell[staticValue=", "");
        string = string.replaceAll("\r", "");
        string = string.replaceAll("\n", "");
        string = string.replace("]", "");
        string = string.trim();
        return string;
    }

}
