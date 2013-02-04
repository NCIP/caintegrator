/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Transports and provides access to genomic data values.
 */
public class ArrayDataValues {
    
    private final List<AbstractReporter> reporters;
    private Set<ReporterList> reporterLists;
    private final Map<String, Integer> reporterIndexMap = new HashMap<String, Integer>();
    private final Map<ArrayDataValueType, TypeValues> typeValuesMap = new HashMap<ArrayDataValueType, TypeValues>();

    /**
     * Creates a new values object.
     * 
     * @param reporters the values object will contain data for these reporters.
     */
    public ArrayDataValues(List<AbstractReporter> reporters) {
        this.reporters = reporters;
        loadReporterIndexMap();
    }
    
    /**
     * Need to clear the map to free the memory.
     */
    public void clearMaps() {
        reporterIndexMap.clear();
    }

    private void loadReporterIndexMap() {
        for (int i = 0; i < reporters.size(); i++) {
            reporterIndexMap.put(reporters.get(i).getName(), i);
        }
    }

    /**
     * @return the reporters
     */
    public List<AbstractReporter> getReporters() {
        return Collections.unmodifiableList(reporters);
    }
    
    /**
     * Gets log2 value of a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @param channelType the channel type of the data array
     * @return the value.
     */
    public double getLog2Value(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type,
            PlatformChannelTypeEnum channelType) {
        return PlatformChannelTypeEnum.TWO_COLOR.equals(channelType)
            ? getFloatValue(arrayData, getReporterIndex(reporter), type)
            : Cai2Util.log2(getFloatValue(arrayData, getReporterIndex(reporter), type));
    }
    
    /**
     * Gets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @return the value.
     */
    public float getFloatValue(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type) {
        return getFloatValue(arrayData, getReporterIndex(reporter), type);
    }
    
    /**
     * Gets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporterIndex the index of the reporter the data value is associated to.
     * @param type the type of data
     * @return the value.
     */
    public float getFloatValue(ArrayData arrayData, int reporterIndex, ArrayDataValueType type) {
        return getTypeValues(type).getFloatValue(arrayData, reporterIndex);
    }

    /**
     * Gets all data for an array / type combination.
     * 
     * @param arrayData the array data the data are associated to.
     * @param type the type of data
     * @return the values.
     */
    public float[] getFloatValues(ArrayData arrayData, ArrayDataValueType type) {
        return getTypeValues(type).getFloatValues(arrayData);
    }

    /**
     * Sets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @param value the value to set.
     */
    public void setFloatValue(ArrayData arrayData, AbstractReporter reporter, ArrayDataValueType type, float value) {
        getTypeValues(type).setFloatValue(arrayData, reporter, value);
    }
    
    /**
     * Sets a single data point for a single reporter / array / type combination, where there are more than one float
     * values to calculate a central tendency.
     * 
     * @param arrayData the array data the data value is associated to.
     * @param reporter the reporter the data value is associated to.
     * @param type the type of data
     * @param values the values to set.
     * @param centralTendencyCalculator used to calculate the central tendency of the float values.
     */
    public void setFloatValue(ArrayData arrayData, AbstractReporter reporter, 
            ArrayDataValueType type, List<Float> values, CentralTendencyCalculator centralTendencyCalculator) {
        centralTendencyCalculator.calculateCentralTendencyValue(values);
        setFloatValue(arrayData, reporter, type, centralTendencyCalculator.getCentralTendencyValue());
        if (centralTendencyCalculator.isHighVariance()) {
            arrayData.getSample().getReportersHighVariance().add(reporter);
            reporter.getSamplesHighVariance().add(arrayData.getSample());
        }
    }

    /**
     * Sets a single data point for a single reporter / array / type combination.
     * 
     * @param arrayData the array data the data values are associated to.
     * @param forReporters the reporters the data values are associated to.
     * @param type the type of data
     * @param values the values to set.
     */
    public void setFloatValues(ArrayData arrayData, List<AbstractReporter> forReporters, ArrayDataValueType type,
            float[] values) {
        getTypeValues(type).setFloatValues(arrayData, forReporters, values);
    }

    private TypeValues getTypeValues(ArrayDataValueType type) {
        if (type == null) {
            throw new IllegalArgumentException("type was null");
        }
        TypeValues typeValues = typeValuesMap.get(type);
        if (typeValues == null) {
            typeValues = new TypeValues(type, this);
            typeValuesMap.put(type, typeValues);
        }
        return typeValues;
    }

    int getReporterIndex(AbstractReporter reporter) {
        if (reporter == null) {
            throw new IllegalArgumentException("reporter was null");
        }
        return reporterIndexMap.get(reporter.getName());
    }
    
    /**
     * Returns the data types in this values object.
     * 
     * @return the data types.
     */
    public Set<ArrayDataValueType> getTypes() {
        return typeValuesMap.keySet();
    }
    
    /**
     * Returns a set of all <code>ArrayDatas</code> that have data in this values object.
     * 
     * @return the arrays.
     */
    public Set<ArrayData> getArrayDatas() {
        if (typeValuesMap.isEmpty()) {
            return Collections.emptySet();
        } else {
            return typeValuesMap.values().iterator().next().getArrayDatas();
        }
    }

    /**
     * @return the array datas in order by id.
     */
    public List<ArrayData> getOrderedArrayDatas() {
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        arrayDatas.addAll(getArrayDatas());
        Collections.sort(arrayDatas, AbstractCaIntegrator2Object.ID_COMPARATOR);
        return arrayDatas;
    }

    /**
     * Returns the <code>ReporterList</code> that data in this values object is associated with.
     * 
     * @return the reporter list.
     */
    public ReporterList getReporterList() {
        if (getReporters().isEmpty()) {
            return null;
        } else {
            return getReporters().iterator().next().getReporterList();
        }
    }

    /**
     * Returns all reporter lists for reporters in this values object.
     * 
     * @return the reporter lists.
     */
    public Set<ReporterList> getReporterLists() {
        if (reporterLists == null) {
            reporterLists = new HashSet<ReporterList>();
            for (AbstractReporter reporter : reporters) {
                reporterLists.add(reporter.getReporterList());
            }
        }
        return reporterLists;
    }

}
