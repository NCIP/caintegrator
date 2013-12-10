/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.file;

import gov.nih.nci.caintegrator.application.analysis.AnalysisViewerTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.GctDatasetFileWriter;
import gov.nih.nci.caintegrator.application.analysis.SegmentDatasetFileWriter;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapGenomicDataFileWriter;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapJnlpFileWriter;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapSampleAnnotationsFileWriter;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVSampleInfoFileWriter;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVSessionFileWriter;
import gov.nih.nci.caintegrator.common.QueryUtil;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * The analysis file manager.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Service("analysisFileManager")
public class AnalysisFileManagerImpl implements AnalysisFileManager {
    private FileManager fileManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public File createIGVGctFile(GctDataset gctDataset, String sessionId) {
        return gctDataset == null ? null : GctDatasetFileWriter.writeAsGct(gctDataset,
                new File(getIGVDirectory(sessionId).getAbsolutePath()
                       + File.separator + IGVFileTypeEnum.GENE_EXPRESSION.getFilename()).getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File createIGVGctFile(GctDataset gctDataset, Study study, String platformName) {
        return gctDataset == null ? null : GctDatasetFileWriter.writeAsGct(gctDataset,
                retrieveIGVFile(study, IGVFileTypeEnum.GENE_EXPRESSION, platformName).getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File createIGVSampleClassificationFile(QueryResult queryResult, String sessionId,
            Collection<ResultColumn> columns, CopyNumberCriterionTypeEnum copyNumberSubType) {
        return new IGVSampleInfoFileWriter().writeSampleInfoFile(QueryUtil.retrieveSampleValuesMap(queryResult),
                columns, new File(getIGVDirectory(sessionId).getAbsolutePath()
                        + File.separator + IGVFileTypeEnum.SAMPLE_CLASSIFICATION.getFilename()).getAbsolutePath(),
                        copyNumberSubType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File createHeatmapSampleClassificationFile(QueryResult queryResult, String sessionId,
            Collection<ResultColumn> columns) {
        return new HeatmapSampleAnnotationsFileWriter().writeSampleInfoFile(QueryUtil.
            retrieveSampleValuesMap(queryResult), columns, new File(getHeatmapDirectory(sessionId).getAbsolutePath()
                + File.separator + HeatmapFileTypeEnum.ANNOTATIONS.getFilename()).getAbsolutePath(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File createIGVSegFile(Collection<SegmentData> segmentDatas, String sessionId, boolean isUseCGHCall) {
        return segmentDatas.isEmpty() ? null : SegmentDatasetFileWriter.writeAsSegFile(segmentDatas,
                new File(getIGVDirectory(sessionId).getAbsolutePath() + File.separator
                        + (isUseCGHCall ? IGVFileTypeEnum.SEGMENTATION_CALLS.getFilename()
                                : IGVFileTypeEnum.SEGMENTATION.getFilename())).getAbsolutePath(), isUseCGHCall);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File createIGVSegFile(Collection<SegmentData> segmentDatas, Study study, String platformName,
            boolean isUseCGHCall) {
        return segmentDatas.isEmpty() ? null : SegmentDatasetFileWriter.writeAsSegFile(segmentDatas,
                retrieveIGVFile(study,
                        isUseCGHCall ? IGVFileTypeEnum.SEGMENTATION_CALLS
                                : IGVFileTypeEnum.SEGMENTATION, platformName).getAbsolutePath(), isUseCGHCall);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createHeatmapGenomicFile(HeatmapParameters parameters, HeatmapResult result,
            Collection<SegmentData> segmentDatas, GeneLocationConfiguration geneLocationConfiguration,
            CBSToHeatmapFactory cbsToHeatmapFactory) throws IOException {
        File genomicDataFile = null;
        File layoutFile = null;
        if (parameters.isViewAllData()) {
            genomicDataFile = retrieveHeatmapFile(parameters.getStudySubscription().getStudy(),
                    parameters.isUseCGHCall() ? HeatmapFileTypeEnum.CALLS_DATA
                    : HeatmapFileTypeEnum.GENOMIC_DATA, parameters.getPlatform().getName());
            layoutFile = retrieveHeatmapFile(parameters.getStudySubscription().getStudy(),
                    HeatmapFileTypeEnum.LAYOUT, parameters.getPlatform().getName());
        } else {
            File sessionDirectory = new File(getHeatmapDirectory(parameters.getSessionId()) + File.separator);
            genomicDataFile = new File(sessionDirectory,
                    parameters.isUseCGHCall() ? HeatmapFileTypeEnum.CALLS_DATA.getFilename()
                    : HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
            layoutFile = new File(sessionDirectory, HeatmapFileTypeEnum.LAYOUT.getFilename());
        }
        if (!segmentDatas.isEmpty()) {
            HeatmapGenomicDataFileWriter fileWriter = new HeatmapGenomicDataFileWriter(segmentDatas,
                    geneLocationConfiguration, parameters, cbsToHeatmapFactory);
            fileWriter.writeGenomicDataFiles(
                genomicDataFile.getAbsolutePath(), layoutFile.getAbsolutePath());
        }
        result.setGenomicDataFile(genomicDataFile);
        result.setLayoutFile(layoutFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createHeatmapJnlpFile(HeatmapParameters heatmapParameters, HeatmapResult heatmapResult) {
        HeatmapJnlpFileWriter.writeJnlpFile(getHeatmapDirectory(heatmapParameters.getSessionId()),
                heatmapParameters.getUrlPrefix(), heatmapParameters.getHeatmapJarUrlPrefix(), heatmapResult);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createIGVSessionFile(IGVParameters igvParameters, IGVResult igvResult) {
        IGVSessionFileWriter.writeSessionFile(getIGVDirectory(igvParameters.getSessionId()),
                igvParameters.getUrlPrefix(), igvResult, igvParameters.getPlatforms());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteViewerDirectory(Study study) {
        try {
            FileUtils.deleteDirectory(getStudyIGVDirectory(study));
            FileUtils.deleteDirectory(getStudyHeatmapDirectory(study));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSessionDirectories(String sessionId) {
        try {
            FileUtils.deleteDirectory(getIGVDirectory(sessionId));
            FileUtils.deleteDirectory(getHeatmapDirectory(sessionId));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllTempAnalysisDirectories() {
        FileUtils.deleteQuietly(new File(fileManager.getTempDirectory(), AnalysisViewerTypeEnum.IGV.getValue()));
        FileUtils.deleteQuietly(new File(fileManager.getTempDirectory(), AnalysisViewerTypeEnum.HEATMAP.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getIGVDirectory(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            throw new IllegalArgumentException("Session ID is null or blank, unable to get the IGV directory.");
        }
        return fileManager.getNewTemporaryDirectory(AnalysisViewerTypeEnum.IGV.getValue() + File.separator + sessionId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File retrieveIGVFile(Study study, IGVFileTypeEnum fileType, String platformName) {
        return new File(getStudyIGVDirectory(study) + File.separator + platformName + "_" + fileType.getFilename());
    }

    private File getStudyIGVDirectory(Study study) {
        File file = new File(fileManager.getStudyDirectory(study) + File.separator
                + AnalysisViewerTypeEnum.IGV.getValue());
        file.mkdir();
        return file;
    }

    /**
     * {@inheritDoc}
     */
    public File getHeatmapDirectory(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            throw new IllegalArgumentException("Session ID is null or blank, unable to get the heatmap directory.");
        }
        return fileManager.getNewTemporaryDirectory(AnalysisViewerTypeEnum.HEATMAP.getValue() + File.separator
                + sessionId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File retrieveHeatmapFile(Study study, HeatmapFileTypeEnum fileType, String platformName) {
        return new File(getStudyHeatmapDirectory(study) + File.separator
                + platformName + "_" + fileType.getFilename());
    }

    private File getStudyHeatmapDirectory(Study study) {
        File file = new File(fileManager.getStudyDirectory(study) + File.separator
                + AnalysisViewerTypeEnum.HEATMAP.getValue());
        file.mkdir();
        return file;
    }

    /**
     * @return the fileManager
     */
    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    @Autowired
    @Override
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
