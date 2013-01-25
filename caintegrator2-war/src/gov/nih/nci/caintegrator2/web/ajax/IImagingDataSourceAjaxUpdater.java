/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum;

import java.io.File;


/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IImagingDataSourceAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the GenomicDataSource Job.
     * @param imagingSourceId to retrieve imaging data for (asynchronously).
     * @param imageClinicalMappingFile file to map.
     * @param mappingType type of mapping for the image source. 
     * @param mapOnly to determine if it's only re-mapping the source.
     * @param loadAimAnnotation to load AIM annotation.
     */
    void runJob(Long imagingSourceId, 
            File imageClinicalMappingFile, 
            ImageDataSourceMappingTypeEnum mappingType,
            boolean mapOnly, boolean loadAimAnnotation);

}
