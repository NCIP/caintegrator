/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

public class DeploymentServiceStub implements DeploymentService {

    public StudyConfiguration refreshedStudyConfiguration;
    public boolean performDeploymentCalled;
    public boolean prepareForDeploymentCalled;

    public Status performDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener) {
        performDeploymentCalled = true;
        return Status.PROCESSING;
    }

    public Status prepareForDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener) {
        prepareForDeploymentCalled = true;
        return Status.DEPLOYED;
    }

}
