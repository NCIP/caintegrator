/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

public class DeploymentServiceStub implements DeploymentService {

    public StudyConfiguration refreshedStudyConfiguration;
    public boolean performDeploymentCalled;
    public boolean prepareForDeploymentCalled;

    public StudyConfiguration performDeployment(StudyConfiguration studyConfiguration, 
            HeatmapParameters heatmapParameters) {
        performDeploymentCalled = true;
        studyConfiguration.setStatus(Status.PROCESSING);
        return studyConfiguration;
    }

    public void prepareForDeployment(StudyConfiguration studyConfiguration) {
        prepareForDeploymentCalled = true;
        studyConfiguration.setStatus(Status.DEPLOYED);
    }

    public void handleDeploymentFailure(StudyConfiguration studyConfiguration, Throwable e) {
        
    }

}
