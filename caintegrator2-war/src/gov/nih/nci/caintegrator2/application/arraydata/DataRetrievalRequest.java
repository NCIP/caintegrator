/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Describes array data to be retrieved.
 */
public class DataRetrievalRequest {

    private final Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
    private final List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
    private final Set<ArrayDataValueType> types = new HashSet<ArrayDataValueType>();
    private boolean reportersChanged;

    /**
     * @return the array data
     */
    public Set<ArrayData> getArrayDatas() {
        return Collections.unmodifiableSet(arrayDatas);
    }

    /**
     * @return the reporters
     */
    public List<AbstractReporter> getReporters() {
        if (reportersChanged) {
            Collections.sort(reporters, AbstractReporter.INDEX_COMPARATOR);
            reportersChanged = false;
        }
        return Collections.unmodifiableList(reporters);
    }

    /**
     * @return the types
     */
    public Set<ArrayDataValueType> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    /**
     * Adds an <code>ArrayData</code> to the request.
     *
     * @param arrayData the array data
     */
    public void addArrayData(ArrayData arrayData) {
        arrayDatas.add(arrayData);
    }

    /**
     * Adds a collection of <code>ArrayDatas</code> to the request.
     *
     * @param arrayDataCollection the array datas
     */
    public void addArrayDatas(Collection<ArrayData> arrayDataCollection) {
        arrayDatas.addAll(arrayDataCollection);
    }

    /**
     * Adds an <code>ArrayDataValueType</code> to the request.
     *
     * @param valueType the array data
     */
    public void addType(ArrayDataValueType valueType) {
        types.add(valueType);
    }

    /**
     * Adds a collection of <code>ArrayDataTypes</code> to the request.
     *
     * @param arrayDataTypeCollection the array data types.
     */
    public void addTypes(Collection<ArrayDataValueType> arrayDataTypeCollection) {
        types.addAll(arrayDataTypeCollection);
    }

    /**
     * Adds an <code>AbstractReporter</code> to the request.
     *
     * @param reporter the array data
     */
    public void addReporter(AbstractReporter reporter) {
        reportersChanged = true;
        reporters.add(reporter);
    }

    /**
     * Adds a collection of <code>AbstractReporters</code> to the request.
     *
     * @param reporterCollection the array datas
     */
    public void addReporters(Collection<AbstractReporter> reporterCollection) {
        reportersChanged = true;
        reporters.addAll(reporterCollection);
    }

    boolean hasEmptyParameter() {
        return arrayDatas.isEmpty() || reporters.isEmpty() || types.isEmpty();
    }

    private void checkHasStudy() {
        if (arrayDatas.isEmpty()) {
            throw new IllegalStateException("There are no ArrayDatas in the request.");
        } else {
            arrayDatas.iterator().next().checkHasStudy();
        }
    }

    Study getStudy() {
        checkHasStudy();
        return arrayDatas.iterator().next().getStudy();
    }

    ReporterTypeEnum getReporterType() {
        return getFirstReporterList().getReporterType();
    }

    Platform getPlatform() {
        return getFirstReporterList().getPlatform();
    }

    private ReporterList getFirstReporterList() {
        return arrayDatas.iterator().next().getReporterLists().iterator().next();
    }

}
