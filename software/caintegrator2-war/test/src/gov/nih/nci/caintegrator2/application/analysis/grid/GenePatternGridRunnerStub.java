/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.caintegrator2.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;

import java.io.File;
import java.io.IOException;

/**
 * 
 */
public class GenePatternGridRunnerStub implements GenePatternGridRunner {
    
    public boolean runGisticCalled;
    public boolean runPCACalled;
    public boolean runPreprocessComparativeMarkerSelectionCalled;
    public boolean runPreprocessDatasetCalled;
    
    public void clear() {
        runGisticCalled = false;
        runPCACalled = false;
        runPreprocessComparativeMarkerSelectionCalled = false;
        runPreprocessDatasetCalled = false;
    }

    public File runGistic(StatusUpdateListener updater, GisticAnalysisJob job, File segmentFile, File markersFile,
            File cnvFile) throws ConnectionException, InvalidCriterionException, ParameterException, IOException {
        runGisticCalled = true;
        return null;
    }


    public File runPCA(StatusUpdateListener updater, PrincipalComponentAnalysisJob job, File gctFile)
            throws ConnectionException, InvalidCriterionException {
        runPCACalled = true;
        return null;
    }

    public File runPreprocessComparativeMarkerSelection(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job, File gctFile, File clsFile) throws ConnectionException,
            InvalidCriterionException {
        runPreprocessComparativeMarkerSelectionCalled = true;
        return null;
    }

    public void runPreprocessDataset(StatusUpdateListener updater, AbstractPersistedAnalysisJob job,
            PreprocessDatasetParameters parameters, File gctFile) throws ConnectionException, InvalidCriterionException {
        runPreprocessDatasetCalled = true;
    }

}
