/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import java.util.Collections;
import java.util.List;

/**
 * This is a static utility class used for doing math functions.
 */
public final class MathUtil {

    private MathUtil() {
    }

    /**
     * Calculates the standard deviation.
     * @param values to calculate std dev for.
     * @param mean of the values.
     * @return standard deviation.
     */
    public static Double standardDeviation(List<Double> values, Double mean) {
        Double totalSquaredDeviations = 0.0;
        for (Double value : values) {
            totalSquaredDeviations += Math.pow(value - mean, 2);
        }
        return Math.sqrt(mean(totalSquaredDeviations, values.size()));
    }

    /**
     * Calculates the median for the list of numbers.
     * 
     * @param list
     *            of numbers to calculate median for.
     * @return median value.
     */
    public static Double medianDouble(List<Double> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        Collections.sort(list);
        int middle = list.size() / 2;
        if (list.size() % 2 == 1) {
            return list.get(middle);
        } else {
            return (list.get(middle - 1) + list.get(middle)) / 2.0;
        }
    }


    /**
     * Retrieves mean value of the list of floats.
     * 
     * @param totalValue total value of the number.
     * @param numValues number of values.
     * @return mean value.
     */
    public static Double mean(Double totalValue, int numValues) {
        return numValues != 0 ? totalValue / numValues : 0;
    }

    /**
     * Calculates the standard deviation.
     * @param values to calculate std dev for.
     * @param mean of the values.
     * @return standard deviation.
     */
    public static Double standardDeviation(List<Float> values, Float mean) {
        Double totalSquaredDeviations = 0.0;
        for (Float value : values) {
            totalSquaredDeviations += Math.pow(value - mean, 2);
        }
        return Math.sqrt(mean(totalSquaredDeviations, values.size()));
    }

    /**
     * Calculates the median for the list of numbers.
     * 
     * @param list
     *            of numbers to calculate median for.
     * @return median value.
     */
    public static Float median(List<Float> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        Collections.sort(list);
        int middle = list.size() / 2;
        if (list.size() % 2 == 1) {
            return list.get(middle);
        } else {
            return (list.get(middle - 1) + list.get(middle)) / 2.0f;
        }
    }

    /**
     * Retrieves mean value of the list of floats.
     * 
     * @param list
     *            of values.
     * @return mean value.
     */
    public static Float mean(List<Float> list) {
        Float totalNumber = 0f;
        for (Float value : list) {
            totalNumber += value;
        }
        return list.isEmpty() ? 0 : totalNumber / list.size();
    }

}
