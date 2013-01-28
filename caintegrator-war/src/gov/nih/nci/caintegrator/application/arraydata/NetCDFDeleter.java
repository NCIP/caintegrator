/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.file.FileManager;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import ucar.nc2.NetcdfFile;

/**
 * Provides functionality to read NetCDF files.
 */
class NetCDFDeleter extends AbstractNetCdfFileHandler {
    
    private static final Logger LOGGER = Logger.getLogger(NetCDFDeleter.class);

    NetCDFDeleter(FileManager fileManager) {
        super(fileManager);
    }
    
    void deleteGisticAnalysisNetCDFFile(Study study, Long reporterListId) {
        File fileToDelete = getFile(study, reporterListId, ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        String filePath = fileToDelete.getPath();
        if (FileUtils.deleteQuietly(fileToDelete)) {
            LOGGER.info("Deleted file: " + filePath);
        }
    }

    @Override
    NetcdfFile getNetCdfFile() {
        return null;
    }

}
