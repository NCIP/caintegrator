/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Global singleton object that manages the results based on sessionId.
 */
public class SessionAnalysisResultsManager {

    private final Map<String, IGVResult> igvResultMap = new HashMap<String, IGVResult>();
    private final Map<String, HeatmapResult> heatmapResultMap = new HashMap<String, HeatmapResult>();

    /**
     * Stores job based on session.
     * @param sessionId to map result to.
     * @param result to store.
     */
    public void storeJobResult(String sessionId, IGVResult result) {
        igvResultMap.put(sessionId, result);
    }
    /**
     * Stores job based on session.
     * @param sessionId to map result to.
     * @param result to store.
     */
    public void storeJobResult(String sessionId, HeatmapResult result) {
        heatmapResultMap.put(sessionId, result);
    }

    /**
     * Given a sessionId and fileType, it retrieves the appropriate igv file.
     * @param sessionId id of session to get file for.
     * @param fileType type of file.
     * @return IGV file.
     */
    public File getJobResultFile(String sessionId, IGVFileTypeEnum fileType) {
        IGVResult result = igvResultMap.get(sessionId);
        if (result == null) {
            return null;
        }
        switch (fileType) {
        case SESSION:
            return result.getSessionFile();
        case GENE_EXPRESSION:
            return result.getGeneExpressionFile();
        case SEGMENTATION:
            return result.getSegmentationFile();
        case SAMPLE_CLASSIFICATION:
            return result.getSampleInfoFile();
            default:
                return null;
        }
    }

    /**
     * Given a sessionId and fileType, it retrieves the appropriate igv file.
     * @param sessionId id of session to get file for.
     * @param fileType type of file.
     * @return IGV file.
     */
    public File getJobResultFile(String sessionId, HeatmapFileTypeEnum fileType) {
        HeatmapResult result = heatmapResultMap.get(sessionId);
        if (result == null) {
            return null;
        }
        switch (fileType) {
        case LAUNCH_FILE:
            return result.getJnlpFile();
        case GENOMIC_DATA:
            return result.getGenomicDataFile();
        case LAYOUT:
            return result.getLayoutFile();
        case ANNOTATIONS:
            return result.getSampleAnnotationFile();
            default:
                return null;
        }
    }

    /**
     * Removes the session from the results.
     * @param session to remove.
     */
    public void removeSession(String session) {
        igvResultMap.remove(session);
        heatmapResultMap.remove(session);
    }

}
