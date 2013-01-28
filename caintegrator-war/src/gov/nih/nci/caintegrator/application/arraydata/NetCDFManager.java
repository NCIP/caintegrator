/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.file.FileManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides read and write access to the underlying NetCDF array data store.
 */
public class NetCDFManager {
    
    private final FileManager fileManager;

    NetCDFManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Persists all the values in the given values object. The values object must correspond to
     * arrays in a single experiment and values for a single <code>ReporterList</code>.
     * 
     * @param values values to store.
     */
    void storeValues(ArrayDataValues values) {
        checkIsValid(values);
        NetCDFWriter writer = new NetCDFWriter(values, fileManager);
        writer.storeValues();
    }

    /**
     * Retrieves the requested array data.
     * 
     * @param request encapsulated retrieval configuration.
     * @return the requested data.
     */
    ArrayDataValues retrieveValues(DataRetrievalRequest request) {
        if (request.hasEmptyParameter()) {
            return new ArrayDataValues(request.getReporters());
        }
        NetCDFReader reader = new NetCDFReader(fileManager, request);
        return reader.retrieveValues();
    }
    
    void deleteGisticAnalysisNetCDFFile(Study study, Long reporterListId) {
        NetCDFDeleter deleter = new NetCDFDeleter(fileManager);
        deleter.deleteGisticAnalysisNetCDFFile(study, reporterListId);
    }

    private void checkIsValid(ArrayDataValues values) {
        checkNotEmpty(values);
        checkSingleStudy(values);
        checkReporterLists(values);
        checkArrayDatas(values);
    }

    private void checkArrayDatas(ArrayDataValues values) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            if (arrayData.getId() == null) {
                throw new IllegalArgumentException("Unsaved ArrayData");
            }
            if (arrayData.getReporterLists().isEmpty()) {
                throw new IllegalArgumentException("ArrayData with id " + arrayData.getId() + " has no ReporterLists");
            }
        }
    }

    private void checkSingleStudy(ArrayDataValues values) {
        Set<Study> studies = new HashSet<Study>();
        for (ArrayData arrayData : values.getArrayDatas()) {
            arrayData.checkHasStudy();
            studies.add(arrayData.getStudy());
        }
        if (studies.size() != 1) {
            throw new IllegalArgumentException("ArrayDatas are related to multiple studies.");
        }
        if (studies.iterator().next().getId() == null) {
            throw new IllegalArgumentException("Study is unsaved.");
        }
    }

    private void checkReporterLists(ArrayDataValues values) {
        Set<ReporterTypeEnum> reporterTypes = new HashSet<ReporterTypeEnum>();
        for (ReporterList reporterList : values.getReporterLists()) {
            reporterTypes.add(reporterList.getReporterType());
            if (reporterList.getId() == null) {
                throw new IllegalArgumentException("ReporterList is unsaved.");
            }
        }
        if (reporterTypes.size() != 1) {
            throw new IllegalArgumentException("ReporterLists must be of the same type, types were: " 
                    + reporterTypes.toString());
        }
    }

    private void checkNotEmpty(ArrayDataValues values) {
        if (values.getArrayDatas().isEmpty()) {
            throw new IllegalArgumentException("No ArrayDatas in ArrayDataValues object");
        }
    }

}
