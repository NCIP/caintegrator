/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

/**
 * The purpose of this is to create a new StudyConfiguration the same way StudyManagementServiceImpl would,
 * but this is for test cases to use that aren't using the StudyManagementServiceImpl object.
 */
public class StudyConfigurationFactory {
    
    public static StudyConfiguration createNewStudyConfiguration() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(Long.valueOf(1));
        Study study = new Study();
        studyConfiguration.setStudy(study);
        
        Timepoint defaultTimepoint = new Timepoint();
        String studyTitle = "";
        if (study.getShortTitleText() != null) {
            studyTitle = study.getShortTitleText();
        } else if (study.getLongTitleText() != null) {
            studyTitle = study.getLongTitleText();
        }
        defaultTimepoint.setDescription("Default Timepoint For Study '" + studyTitle + "'");
        defaultTimepoint.setName("Default");
        study.setDefaultTimepoint(defaultTimepoint);
        return studyConfiguration;
    }

}
