/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Holds an array data matrix for a particular data type.
 */
class TypeValues {

    private final ArrayDataValueType type;
    private final ArrayDataValues arrayDataValues;
    private final Map<ArrayData, float[]> valuesMap = new HashMap<ArrayData, float[]>();

    TypeValues(ArrayDataValueType type, ArrayDataValues arrayDataValues) {
        this.type = type;
        this.arrayDataValues = arrayDataValues;
    }

   void setFloatValue(ArrayData arrayData, AbstractReporter reporter, float value) {
        float[] values = getValues(arrayData);
        values[arrayDataValues.getReporterIndex(reporter)] = value;
    }

   void setFloatValues(ArrayData arrayData, List<AbstractReporter> forReporters, float[] newValues) {
       if (newValues.length == arrayDataValues.getReporters().size()) {
           valuesMap.put(arrayData, newValues);
       } else {
           float[] values = getValues(arrayData);
           int destPosition = arrayDataValues.getReporterIndex(forReporters.get(0));
           System.arraycopy(newValues, 0, values, destPosition, newValues.length);
       }
   }

    private float[] getValues(ArrayData arrayData) {
        if (arrayData == null) {
            throw new IllegalArgumentException("arrayData was null");
        }
        float[] values = valuesMap.get(arrayData);
        if (values == null) {
            values = createArray();
            valuesMap.put(arrayData, values);
        }
        return values;
    }

    private float[] createArray() {
        if (type.getTypeClass().equals(Float.class)) {
            return new float[arrayDataValues.getReporters().size()];
        } else {
            throw new IllegalStateException("Unsupported data type: " + type.getTypeClass());
        }
    }

    float getFloatValue(ArrayData arrayData, int reporterIndex) {
        return getFloatValues(arrayData)[reporterIndex];
    }

    float[] getFloatValues(ArrayData arrayData) {
        return getValues(arrayData);
    }

    Set<ArrayData> getArrayDatas() {
        return valuesMap.keySet();
    }

}
