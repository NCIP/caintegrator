/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.JobInfo;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotConfiguration;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnalysisServiceStub implements AnalysisService {
    
    public boolean createKMPlotCalled;
    public boolean createGEPlotCalled;
    public boolean executeGenePatternJobCalled;
    public boolean executeComparativeMarkerSelectionJobCalled;
    public boolean executePcaJobCalled;
    public boolean executeGisticJobCalled;
    public boolean deleteAnalysisJobCalled;
    
    public void clear() {
        createKMPlotCalled = false;
        createGEPlotCalled = false;
        executeGenePatternJobCalled = false;
        executeComparativeMarkerSelectionJobCalled = false;
        executePcaJobCalled = false;
        executeGisticJobCalled = false;
        deleteAnalysisJobCalled = false;
    }

    /**
     * {@inheritDoc}
     */
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
    public KMPlot createKMPlot(StudySubscription studySubscription, AbstractKMParameters parameters) {
        createKMPlotCalled = true;
        KMPlotServiceStub kmPlotService = new KMPlotServiceStub();
        return kmPlotService.generatePlot(new KMPlotConfiguration());
    }

    
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) {
        createGEPlotCalled = true;
        return null;
    }

    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job)
            throws ConnectionException {
        executeComparativeMarkerSelectionJobCalled = true;
        return null;
    }

    public File executeGridPCA(StatusUpdateListener updater, PrincipalComponentAnalysisJob job)
            throws ConnectionException {
        executePcaJobCalled = true;
        return null;
    }

    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job)
            throws ConnectionException, InvalidCriterionException {
        executeGisticJobCalled = true;
        return null;
    }

    public void deleteAnalysisJob(Long jobId) {
        deleteAnalysisJobCalled = true;
    }
}
