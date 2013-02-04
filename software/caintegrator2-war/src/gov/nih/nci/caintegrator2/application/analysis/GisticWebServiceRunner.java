/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.WebServiceException;

/**
 * Helper class that invokes GISTIC via the GenePattern web service interface.
 */
class GisticWebServiceRunner {

    private final CaIntegrator2GPClient client;
    private final FileManager fileManager;

    public GisticWebServiceRunner(CaIntegrator2GPClient client, FileManager fileManager) {
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
        } catch (WebServiceException e) {
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
    throws IOException, WebServiceException {
        if ("Error".equals(jobInfo.getStatus())) {
            updateStatus(updater, job, AnalysisJobStatusEnum.ERROR_CONNECTING);
        } else {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
        }
        File resultsDir = new File(fileManager.getUserDirectory(job.getSubscription()) + File.separator
                + "GISTIC_RESULTS_" + System.currentTimeMillis());
        resultsDir.mkdirs();
        client.getResultFiles(jobInfo, resultsDir);
        return Cai2Util.zipAndDeleteDirectory(resultsDir.getAbsolutePath());
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
