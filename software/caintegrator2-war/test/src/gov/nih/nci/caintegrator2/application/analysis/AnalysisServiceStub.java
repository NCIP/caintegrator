/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.genepattern.webservice.JobInfo;

public class AnalysisServiceStub implements AnalysisService {

    public boolean createKMPlotCalled;
    public boolean createGEPlotCalled;
    public boolean executeGenePatternJobCalled;
    public boolean executeComparativeMarkerSelectionJobCalled;
    public boolean executePcaJobCalled;
    public boolean executeGisticJobCalled;
    public boolean deleteAnalysisJobCalled;
    public boolean deleteViewerDirectoryCalled;
    public boolean deleteGisticAnalysisCalled;
    public boolean isValidGenePatternConnection = true;
    public boolean executeIGVCalled;
    public boolean executeHeatmapCalled;
    public boolean executeIGVGlobalCalled;
    public boolean createViewerFilesCalled;
    public boolean createHeatmapFileCalled;

    public void clear() {
        createKMPlotCalled = false;
        createGEPlotCalled = false;
        executeGenePatternJobCalled = false;
        executeComparativeMarkerSelectionJobCalled = false;
        executePcaJobCalled = false;
        executeGisticJobCalled = false;
        deleteAnalysisJobCalled = false;
        deleteViewerDirectoryCalled = false;
        deleteGisticAnalysisCalled = false;
        executeIGVCalled = false;
        executeHeatmapCalled = false;
        createViewerFilesCalled = false;
        createHeatmapFileCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server) {
        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        AnalysisMethod method = new AnalysisMethod();
        method.setName("method");
        methods.add(method);
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setName("parameter");
        parameter.setType(AnalysisParameterType.STRING);
        StringParameterValue defaultValue = new StringParameterValue();
        defaultValue.setParameter(parameter);
        defaultValue.setValue("default");
        parameter.setDefaultValue(defaultValue);
        parameter.setRequired(true);
        method.getParameters().add(parameter);
        return methods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) {
        JobInfoWrapper jobInfo = new JobInfoWrapper();
        executeGenePatternJobCalled = true;
        try {
            jobInfo.setUrl(new URL("http://localhost/resultUrl"));
            jobInfo.setJobInfo(new JobInfo());
            return jobInfo;
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KMPlot createKMPlot(StudySubscription studySubscription, AbstractKMParameters parameters) {
        createKMPlotCalled = true;
        KMPlotServiceStub kmPlotService = new KMPlotServiceStub();
        return kmPlotService.generatePlot(new KMPlotConfiguration());
    }


    @Override
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) {
        createGEPlotCalled = true;
        return null;
    }

    @Override
    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job)
            throws ConnectionException {
        executeComparativeMarkerSelectionJobCalled = true;
        return null;
    }

    @Override
    public File executeGridPCA(StatusUpdateListener updater, PrincipalComponentAnalysisJob job)
            throws ConnectionException {
        executePcaJobCalled = true;
        return null;
    }

    @Override
    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job)
            throws ConnectionException, InvalidCriterionException {
        executeGisticJobCalled = true;
        return null;
    }

    @Override
    public void deleteAnalysisJob(Long jobId) {
        deleteAnalysisJobCalled = true;
    }

    @Override
    public void deleteViewerDirectory(Study study) {
        deleteViewerDirectoryCalled = true;
    }

    @Override
    public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
        return entity;
    }

    @Override
    public StudySubscription getRefreshedStudySubscription(StudySubscription studySubscription) {
        return studySubscription;
    }

    @Override
    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        return geneSymbols;
    }

    @Override
    public void deleteGisticAnalysis(GisticAnalysis gisticAnalysis) {
        deleteGisticAnalysisCalled = true;
    }

    @Override
    public boolean validateGenePatternConnection(ServerConnectionProfile server) {
        return isValidGenePatternConnection;
    }

    @Override
    public String executeIGV(IGVParameters igvParameters)
            throws InvalidCriterionException {
        executeIGVCalled = true;
        return null;
    }

    @Override
    public String executeHeatmap(HeatmapParameters heatmapParameters)
            throws InvalidCriterionException {
        executeHeatmapCalled = true;
        return null;
    }

    public void createHeatmapFile(StudySubscription studySubscription, Platform platform)
            throws InvalidCriterionException {
        createHeatmapFileCalled = true;
        return;
    }

    @Override
    public void createViewerFiles(StudySubscription studySubscription, HeatmapParameters heatmapParameters,
            Platform platform) throws InvalidCriterionException {
        createViewerFilesCalled = true;
        return;
    }
}
