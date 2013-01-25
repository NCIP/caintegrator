/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import edu.mit.broad.genepattern.gp.services.FileWrapper;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import edu.mit.broad.genepattern.gp.services.JobInfo;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

/**
 * Helper class that invokes GISTIC via the GenePattern web service interface.
 */
class GisticWebServiceRunner {

    private final GenePatternClient client;
    private final FileManager fileManager;

    public GisticWebServiceRunner(GenePatternClient client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;
    }

    File runGistic(StatusUpdateListener updater, GisticAnalysisJob job, File segmentFile, File markersFile) 
    throws ConnectionException, InvalidCriterionException, IOException {
        try {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
            List<ParameterInfo> parameters = createParameters(job, segmentFile, markersFile);
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_REMOTELY);
            JobInfo jobInfo = client.runAnalysis("GISTIC", parameters);
            jobInfo = GenePatternUtil.waitToComplete(jobInfo, client);
            return handleResult(updater, job, jobInfo);
        } catch (GenePatternServiceException e) {
            updateStatus(updater, job, AnalysisJobStatusEnum.ERROR_CONNECTING);
            throw new ConnectionException("Couldn't connect to GISTIC service: " + e.getMessage(), e);
        } finally {
            segmentFile.delete();
            markersFile.delete();
        }
    }

    private void updateStatus(StatusUpdateListener updater,
            AbstractPersistedAnalysisJob job, AnalysisJobStatusEnum status) {
        job.setStatus(status);
        updater.updateStatus(job);
    }

    private File handleResult(StatusUpdateListener updater, GisticAnalysisJob job, JobInfo jobInfo) 
    throws GenePatternServiceException, IOException {
        if ("Error".equals(jobInfo.getStatus())) {
            updateStatus(updater, job, AnalysisJobStatusEnum.ERROR_CONNECTING);
        } else {
            updateStatus(updater, job, AnalysisJobStatusEnum.COMPLETED);
        }
        File resultsDir = new File(fileManager.getUserDirectory(job.getSubscription()) + File.separator
                + "GISTIC_RESULTS_" + System.currentTimeMillis());
        downloadResults(resultsDir, jobInfo);
        return Cai2Util.zipAndDeleteDirectory(resultsDir.getAbsolutePath());
    }

    private void downloadResults(File resultsDir, JobInfo jobInfo) throws GenePatternServiceException, IOException {
        resultsDir.mkdirs();
        FileWrapper[] fileWrappers = client.getResultFiles(jobInfo);
        for (FileWrapper fileWrapper : fileWrappers) {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(resultsDir, fileWrapper.getFilename()));
            IOUtils.copy(fileWrapper.getDataHandler().getInputStream(), fileOutputStream);
            fileOutputStream.close();
        }
    }

    private List<ParameterInfo> createParameters(GisticAnalysisJob job, File segmentFile, File markersFile) 
    throws InvalidCriterionException, IOException {
        List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();
        GisticParameters gisticParams = job.getGisticAnalysisForm().getGisticParameters();
        parameters.add(createParameter("refgene.file", gisticParams.getRefgeneFile().getParameterValue()));
        parameters.add(createParameter("amplifications.threshold", gisticParams.getAmplificationsThreshold()));
        parameters.add(createParameter("deletions.threshold", gisticParams.getAmplificationsThreshold()));
        parameters.add(createParameter("join.segment.size", gisticParams.getJoinSegmentSize()));
        parameters.add(createParameter("qv.thresh", gisticParams.getQvThresh()));
        parameters.add(createParameter("extension", gisticParams.getExtension()));
        parameters.add(createParameter("remove.X", gisticParams.getRemoveX()));
        parameters.add(createParameter("seg.file", segmentFile.getAbsolutePath()));
        parameters.add(createParameter("markers.file", markersFile.getAbsolutePath()));
        if (gisticParams.getCnvSegmentsToIgnoreFile() != null) {
            parameters.add(createParameter("cnv.file", gisticParams.getCnvSegmentsToIgnoreFile().getAbsolutePath()));
        }
        return parameters;
    }

    private ParameterInfo createParameter(String name, Object value) {
        ParameterInfo parameter = new ParameterInfo();
        parameter.setName(name);
        parameter.setValue(value.toString());
        return parameter;
    }

}
