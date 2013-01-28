/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.file;

import gov.nih.nci.caintegrator.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 
 */
public interface AnalysisFileManager {

    /**
     * Creates an igv directory for the session.
     * @param sessionId to create directory for.
     * @return igv directory.
     */
    File getIGVDirectory(String sessionId);
    
    /**
     * Delete the igv directory.
     */
    void deleteAllTempAnalysisDirectories();
    
    /**
     * Delete the igv directory for the session.
     * @param sessionId to delete directory for.
     */
    void deleteSessionDirectories(String sessionId);
    
    /**
     * Delete the viewer directory for the study.
     * @param study to delete directory for.
     */
    void deleteViewerDirectory(Study study);
    
    /**
     * Retrieves the IGV File for the study based on the file type and platform name.
     * @param study the study.
     * @param fileType the file type.
     * @param platformName the platform name.
     * @return the file or null if not found.
     */
    File retrieveIGVFile(Study study, IGVFileTypeEnum fileType, String platformName);
    
    /**
     * Retrieves the Heatmap File for the study based on the file type and platform name.
     * @param study the study.
     * @param fileType the file type.
     * @param platformName the platform name.
     * @return the file or null if not found.
     */
    File retrieveHeatmapFile(Study study, HeatmapFileTypeEnum fileType, String platformName);
    
    /**
     * Creates the IGV GCT File for the given gctDataset.
     * @param gctDataset gct data.
     * @param sessionId directory will be based on this.
     * @return the file.
     */
    File createIGVGctFile(GctDataset gctDataset, String sessionId);
    
    /**
     * Stores the IGV GCTFile for the study with platform name.
     * @param gctDataset gct data.
     * @param study the study.
     * @param platformName the platform name.
     * @return the file.
     */
    File createIGVGctFile(GctDataset gctDataset, Study study, String platformName);
    
    /**
     * Creates the IGV Segment Data file for the given segment datas.
     * @param segmentDatas segment data.
     * @param sessionId directory will be based on this.
     * @param isUseCGHCall whether or not to use CGH call for the segmentation file.
     * @return the file.
     */
    File createIGVSegFile(Collection<SegmentData> segmentDatas, String sessionId, boolean isUseCGHCall);
    
    /**
     * Stores the IGV Segment Data File for the study with platform name.
     * @param segmentDatas segment data.
     * @param study the study.
     * @param platformName the platform name.
     * @param isUseCGHCall whether or not to use CGH call for the segmentation file.
     * @return the file.
     */
    File createIGVSegFile(Collection<SegmentData> segmentDatas, Study study, String platformName, boolean isUseCGHCall);
    
    /**
     * Creates the IGV Sample Classification File for the given query result.
     * @param queryResult query result data to turn into sample classification file.
     * @param sessionId directory will be based on this.
     * @param columns the columns that are in the query results.
     * @param copyNumberSubType subtype for copynumber data (null if no copy number exists).
     * @return the file.
     */
    File createIGVSampleClassificationFile(QueryResult queryResult, String sessionId, 
            Collection<ResultColumn> columns, CopyNumberCriterionTypeEnum copyNumberSubType);
    
    /**
     * Creates the Heatmap JNLP file.
     * @param heatmapParameters parameters used for running heatmap.
     * @param heatmapResult results of the run.
     */
    void createHeatmapJnlpFile(HeatmapParameters heatmapParameters, HeatmapResult heatmapResult);
    
    /**
     * Creates the IGV Session file.
     * @param igvParameters parameters used for running IGV.
     * @param igvResult results of the run.
     */
    void createIGVSessionFile(IGVParameters igvParameters, IGVResult igvResult);
    
    /**
     * Creates the Heatmap genomic file.
     * @param parameters heatmap params.
     * @param result heatmap result.
     * @param segmentDatas for genomic data to be calculated.
     * @param geneLocationConfiguration for gene chromosomal locations.
     * @param cbsToHeatmapFactory factory that creates CBSToHeatmap object, which runs cbsToHeatmap algorithm.
     * @throws IOException 
     */
    void createHeatmapGenomicFile(HeatmapParameters parameters, HeatmapResult result, 
            Collection<SegmentData> segmentDatas, GeneLocationConfiguration geneLocationConfiguration, 
            CBSToHeatmapFactory cbsToHeatmapFactory) throws IOException;
    
    /**
     * Creates the Heatmap Sample Classification File for the given query result.
     * @param queryResult query result data to turn into sample classification file.
     * @param sessionId directory will be based on this.
     * @param columns the columns that are in the query results.
     * @return the file.
     */
    File createHeatmapSampleClassificationFile(QueryResult queryResult, String sessionId, 
            Collection<ResultColumn> columns);

    /**
     * @return the fileManager
     */
    FileManager getFileManager();

    /**
     * @param fileManager the fileManager to set
     */
    void setFileManager(FileManager fileManager);
    
}
