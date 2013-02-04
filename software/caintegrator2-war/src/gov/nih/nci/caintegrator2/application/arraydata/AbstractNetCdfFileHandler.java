/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.nc2.NetcdfFile;

/**
 * Provides shared functionality for the reader and writer subclasses.
 */
abstract class AbstractNetCdfFileHandler {
    
    static final String REPORTER_DIMENSION_NAME = "reporter_dimension";
    static final String ARRAY_DATA_DIMENSION_NAME = "array_data_dimension";
    static final String ARRAY_DATA_IDS_VARIABLE = "array_data_ids";
    private final FileManager fileManager;
    private Map<Long, Integer> arrayDataOffsets;

    AbstractNetCdfFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    DataType getDataType(ArrayDataValueType valueType) {
        if (Float.class.equals(valueType.getTypeClass())) {
            return DataType.FLOAT;
        } else {
            throw new ArrayDataStorageException("Unsupported data type: " + valueType.getTypeClass().getName());
        }
    }

    File getFile(ArrayDataValues values) {
        return getFile(getStudy(values), getPlatform(values), getReporterType(values));
    }

    File getFile(Study study, Platform platform, ReporterTypeEnum reporterType) {
        File studyDirectory = fileManager.getStudyDirectory(study);
        return new File(studyDirectory, getFileName(platform, reporterType));
    }

    private String getFileName(Platform platform, ReporterTypeEnum reporterType) {
        return "data" + platform.getId() + "_" + reporterType.getValue() + ".nc";
    }

    private ReporterTypeEnum getReporterType(ArrayDataValues values) {
        return values.getArrayDatas().iterator().next().getReporterLists().iterator().next().getReporterType();
    }
    
    private Platform getPlatform(ArrayDataValues values) {
        return values.getArrayDatas().iterator().next().getReporterLists().iterator().next().getPlatform();
    }

    private Study getStudy(ArrayDataValues values) {
        return values.getArrayDatas().iterator().next().getStudy();
    }

    private void loadArrayDataOffsets() throws IOException {
        arrayDataOffsets = new HashMap<Long, Integer>();
        Array ids = getNetCdfFile().findVariable(ARRAY_DATA_IDS_VARIABLE).read();
        for (int i = 0; i < ids.getSize(); i++) {
            arrayDataOffsets.put(ids.getLong(i), i);
        }
    }

    abstract NetcdfFile getNetCdfFile();

    Map<Long, Integer> getArrayDataOffsets() throws IOException {
        if (arrayDataOffsets == null) {
            loadArrayDataOffsets();
        }
        return arrayDataOffsets;
    }
}
