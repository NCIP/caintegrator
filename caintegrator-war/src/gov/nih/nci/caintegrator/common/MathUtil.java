/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.common;

import java.util.Arrays;
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
    public static Double standardDeviation(float[] values, float mean) {
        double totalSquaredDeviations = 0.0;
        for (float value : values) {
            totalSquaredDeviations += Math.pow(value - mean, 2);
        }
        return Math.sqrt(mean(totalSquaredDeviations, values.length));
    }

    /**
     * Calculates the median for the list of numbers.
     *
     * @param values of numbers to calculate median for.
     * @return median value.
     */
    public static float median(float[] values) {
        if (values.length == 1) {
            return values[0];
        }
        Arrays.sort(values);
        int middle = values.length / 2;
        if (values.length % 2 == 1) {
            return values[middle];
        } else {
            return (values[middle - 1] + values[middle]) / 2.0f;
        }
    }

    /**
     * Retrieves mean value of the list of floats.
     *
     * @param values of values.
     * @return mean value.
     */
    public static float mean(float[] values) {
        float totalNumber = 0.0f;
        for (float value : values) {
            totalNumber += value;
        }
        return values.length == 0 ? 0 : totalNumber / values.length;
    }
}
