/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.StudyTestDataGenerator;

@SuppressWarnings("PMD")
public final class StudyConfigurationGenerator extends AbstractTestDataGenerator<StudyConfiguration> {
    
    public static final AbstractTestDataGenerator<StudyConfiguration> INSTANCE = new StudyConfigurationGenerator();

    private StudyConfigurationGenerator() {
        super();
    }
    
    @Override
    public void compareFields(StudyConfiguration original, StudyConfiguration retrieved) {
        assertEquals(original.getStatus(), retrieved.getStatus());
        assertEquals(original.getVisibility(), retrieved.getVisibility());
        StudyTestDataGenerator.INSTANCE.compare(original.getStudy(), retrieved.getStudy());
        assertEquals(original.getClinicalConfigurationCollection().size(), retrieved.getClinicalConfigurationCollection().size());
        for (int i = 0; i < original.getClinicalConfigurationCollection().size(); i++) {
            DelimitedTextClinicalSourceConfiguration config1 = (DelimitedTextClinicalSourceConfiguration) original.getClinicalConfigurationCollection().get(i);
            DelimitedTextClinicalSourceConfiguration config2 = (DelimitedTextClinicalSourceConfiguration) retrieved.getClinicalConfigurationCollection().get(i);
            DelimitedTextClinicalSourceConfigurationGenerator.INSTANCE.compare(config1, config2);
        }
        assertEquals(original.getGenomicDataSources().size(), retrieved.getGenomicDataSources().size());
        for (int i = 0; i < original.getGenomicDataSources().size(); i++) {
            GenomicDataSourceConfiguration config1 = (GenomicDataSourceConfiguration) original.getGenomicDataSources().get(i);
            GenomicDataSourceConfiguration config2 = (GenomicDataSourceConfiguration) retrieved.getGenomicDataSources().get(i);
            GenomicDataSourceConfigurationGenerator.INSTANCE.compare(config1, config2);
        }
        assertEquals(original.getImageDataSources().size(), retrieved.getImageDataSources().size());
        for (int i = 0; i < original.getImageDataSources().size(); i++) {
            ImageDataSourceConfiguration config1 = original.getImageDataSources().get(i);
            ImageDataSourceConfiguration config2 = retrieved.getImageDataSources().get(i);
            ImageDataSourceConfigurationGenerator.INSTANCE.compare(config1, config2);
        }
    }

    @Override
    public StudyConfiguration createPersistentObject() {
        return new StudyConfiguration();
    }

    @Override
    public void setValues(StudyConfiguration studyConfiguration, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        studyConfiguration.setStatus(getNewEnumValue(studyConfiguration.getStatus(), Status.values()));
        studyConfiguration.setVisibility(getNewEnumValue(studyConfiguration.getVisibility(), Visibility.values()));
        if (studyConfiguration.getStudy() == null) {
            studyConfiguration.setStudy(StudyTestDataGenerator.INSTANCE.createPersistentObject());
        }
        studyConfiguration.getStudy().setStudyConfiguration(studyConfiguration);
        StudyTestDataGenerator.INSTANCE.setValues(studyConfiguration.getStudy(), nonCascadedObjects);
        studyConfiguration.getClinicalConfigurationCollection().clear();
        for (int i = 0; i < 3; i++) {
            DelimitedTextClinicalSourceConfiguration config = new DelimitedTextClinicalSourceConfiguration(null, studyConfiguration);
            DelimitedTextClinicalSourceConfigurationGenerator.INSTANCE.setValues(config, nonCascadedObjects);
        }
        studyConfiguration.getGenomicDataSources().clear();
        for (int i = 0; i < 3; i++) {
            GenomicDataSourceConfiguration config = new GenomicDataSourceConfiguration();
            studyConfiguration.getGenomicDataSources().add(config);
            config.setStudyConfiguration(studyConfiguration);
            config.setDataType(PlatformDataTypeEnum.EXPRESSION);
            GenomicDataSourceConfigurationGenerator.INSTANCE.setValues(config, nonCascadedObjects);
        }
        for (int i = 0; i < 3; i++) {
            GenomicDataSourceConfiguration config = new GenomicDataSourceConfiguration();
            studyConfiguration.getGenomicDataSources().add(config);
            config.setStudyConfiguration(studyConfiguration);
            config.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
            GenomicDataSourceConfigurationGenerator.INSTANCE.setValues(config, nonCascadedObjects);
        }
        for (int i = 0; i < 3; i++) {
            ImageDataSourceConfiguration config = new ImageDataSourceConfiguration();
            studyConfiguration.getImageDataSources().add(config);
            config.setStudyConfiguration(studyConfiguration);
            ImageDataSourceConfigurationGenerator.INSTANCE.setValues(config, nonCascadedObjects);
        }
    }

}
