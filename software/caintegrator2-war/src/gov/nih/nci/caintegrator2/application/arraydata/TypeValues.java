/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;


/**
 * Holds an array data matrix for a particular data type.
 */
class TypeValues {

    private final ArrayDataValueType type;
    private final ArrayDataValues arrayDataValues;
    private final Map<ArrayData, Object> valuesMap = new HashMap<ArrayData, Object>();

    TypeValues(ArrayDataValueType type, ArrayDataValues arrayDataValues) {
        this.type = type;
        this.arrayDataValues = arrayDataValues;
    }

   void setFloatValue(ArrayData arrayData, AbstractReporter reporter, float value) {
        float[] values = (float[]) getValues(arrayData);
        values[arrayDataValues.getReporterIndex(reporter)] = value;
    }

   void setFloatValues(ArrayData arrayData, List<AbstractReporter> forReporters, float[] newValues) {
       if (newValues.length == arrayDataValues.getReporters().size()) {
           valuesMap.put(arrayData, newValues);
       } else {
           float[] values = (float[]) getValues(arrayData);
           int destPosition = arrayDataValues.getReporterIndex(forReporters.get(0));
           System.arraycopy(newValues, 0, values, destPosition, newValues.length);
       }
   }

    private Object getValues(ArrayData arrayData) {
        if (arrayData == null) {
            throw new IllegalArgumentException("arrayData was null");
        }
        Object values = valuesMap.get(arrayData);
        if (values == null) {
            values = createArray();
            valuesMap.put(arrayData, values);
        }
        return values;
    }

    private Object createArray() {
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
        return (float[]) getValues(arrayData);
    }

    Set<ArrayData> getArrayDatas() {
        return valuesMap.keySet();
    }

}
