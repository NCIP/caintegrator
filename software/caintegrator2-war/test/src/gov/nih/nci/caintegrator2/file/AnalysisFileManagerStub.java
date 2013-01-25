/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.file;

import gov.nih.nci.caintegrator2.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator2.application.analysis.GctDataset;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class AnalysisFileManagerStub implements AnalysisFileManager {

    public boolean deleteAllViewerDirectoryCalled;
    public boolean deleteViewerDirectoryCalled;
    public boolean getIGVDirectoryCalled;
    public boolean createIGVGctFileCalled;
    public boolean createIGVSegFileCalled;
    public boolean createIGVSessionFileCalled;
    public boolean createIGVSampleClassificationFileCalled;
    public boolean createHeatmapGenomicFileCalled;
    public boolean createHeatmapJnlpFileCalled;
    public boolean createHeatmapSampleClassificationFileCalled;
    
    public void clear() {
        deleteAllViewerDirectoryCalled = false;
        deleteViewerDirectoryCalled = false;
        getIGVDirectoryCalled = false;
        createIGVGctFileCalled = false;
        createIGVSegFileCalled = false;
        createIGVSessionFileCalled = false;
        createIGVSampleClassificationFileCalled = false;
        createHeatmapGenomicFileCalled = false;
        createHeatmapJnlpFileCalled = false;
        createHeatmapSampleClassificationFileCalled = false;
    }




    public File getNewTemporaryDirectory(String dirName) {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public File getUserDirectory(StudySubscription studySubscription) {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public File retrieveTmpFile() {
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "tmpFile");
        tmpFile.deleteOnExit();
        return tmpFile;
    }

    public File getIGVDirectory(String sessionId) {
        getIGVDirectoryCalled = true;
        return retrieveTmpFile();
    }

    public void deleteAllTempAnalysisDirectories() {
        deleteAllViewerDirectoryCalled = true;
        
    }

    public void deleteViewerDirectory(Study study) {
        deleteViewerDirectoryCalled = true;
    }

    public void deleteSessionDirectories(String sessionId) {
        deleteViewerDirectoryCalled = true;
    }

    public File createIGVGctFile(GctDataset gctDataset, String sessionId) {
        createIGVGctFileCalled = true;
        return retrieveTmpFile();
    }

    public File createIGVSegFile(Collection<SegmentData> segmentDatas, String sessionId, boolean isUseCGHCall) {
        createIGVSegFileCalled = true;
        return retrieveTmpFile();
    }

    public void createIGVSessionFile(IGVParameters igvParams, IGVResult igvResult) {
        createIGVSessionFileCalled = true;
    }

    public File createIGVSampleClassificationFile(QueryResult queryResult, String sessionId,
            Collection<ResultColumn> columns, CopyNumberCriterionTypeEnum copyNumberSubType) {
        createIGVSampleClassificationFileCalled = true;
        return retrieveTmpFile();
    }

    public File createIGVGctFile(GctDataset gctDataset, Study study, String platformName) {
        createIGVGctFileCalled = true;
        return retrieveTmpFile();
    }

    public File createIGVSegFile(Collection<SegmentData> segmentDatas, Study study, String platformName, boolean isUseCGHCall) {
        createIGVSegFileCalled = true;
        return retrieveTmpFile();
    }

    public File retrieveIGVFile(Study study, IGVFileTypeEnum fileType, String platformName) {
        return retrieveTmpFile();
    }

    public void createHeatmapJnlpFile(HeatmapParameters heatmapParameters, HeatmapResult heatmapResult) {
        createHeatmapJnlpFileCalled = true;
    }

    public File createHeatmapSampleClassificationFile(QueryResult queryResult, String sessionId,
            Collection<ResultColumn> columns) {
        createHeatmapSampleClassificationFileCalled = true;
        return retrieveTmpFile();
    }

    public File retrieveHeatmapFile(Study study, HeatmapFileTypeEnum fileType, String platformName) {
        return retrieveTmpFile();
    }

    public void createHeatmapGenomicFile(HeatmapParameters parameters, HeatmapResult result,
            Collection<SegmentData> segmentDatas, GeneLocationConfiguration geneLocationConfiguration,
            CBSToHeatmapFactory cbsToHeatmapFactory) throws IOException {
        result.setGenomicDataFile(new File("Dummy"));
        createHeatmapGenomicFileCalled = true;
    }

}
