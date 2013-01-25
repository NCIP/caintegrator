/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.SampleGenerator;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfileGenerator;

import java.util.Set;

public class GenomicDataSourceConfigurationGenerator extends AbstractTestDataGenerator<GenomicDataSourceConfiguration> {

    public static final AbstractTestDataGenerator<GenomicDataSourceConfiguration> INSTANCE = new GenomicDataSourceConfigurationGenerator();

    @Override
    public void compareFields(GenomicDataSourceConfiguration original, GenomicDataSourceConfiguration retrieved) {
        assertEquals(original.getStudyConfiguration(), retrieved.getStudyConfiguration());
        assertEquals(original.getExperimentIdentifier(), retrieved.getExperimentIdentifier());
        ServerConnectionProfileGenerator.INSTANCE.compare(original.getServerProfile(), retrieved.getServerProfile());
        assertEquals(original.getSamples().size(), retrieved.getSamples().size());
        for (int i = 0; i < original.getSamples().size(); i++) {
            SampleGenerator.INSTANCE.compare(original.getSamples().get(i), retrieved.getSamples().get(i));
        }
        if (original.getDnaAnalysisDataConfiguration() == null) {
            assertNull(retrieved.getDnaAnalysisDataConfiguration());
        } else {
            DnaAnalysisDataConfiguration originalConfig = original.getDnaAnalysisDataConfiguration();
            DnaAnalysisDataConfiguration retrievedConfig = retrieved.getDnaAnalysisDataConfiguration();
            assertEquals(originalConfig.getMappingFilePath(), retrievedConfig.getMappingFilePath());
            assertEquals(originalConfig.getChangePointSignificanceLevel(), retrievedConfig.getChangePointSignificanceLevel());
            assertEquals(originalConfig.getEarlyStoppingCriterion(), retrievedConfig.getEarlyStoppingCriterion());
            assertEquals(originalConfig.getRandomNumberSeed(), retrievedConfig.getRandomNumberSeed());
            if (originalConfig.getSegmentationService() == null) {
                assertNull(retrievedConfig.getSegmentationService());
            } else {
                ServerConnectionProfileGenerator.INSTANCE.compare(originalConfig.getSegmentationService(), retrievedConfig.getSegmentationService());                
            }
        }
        if (original.getControlSampleSetCollection() == null) {
            assertNull(retrieved.getControlSampleSetCollection());
        } else {
            assertEquals(original.getControlSampleSetCollection().size(), retrieved.getControlSampleSetCollection().size());
        }
    }

    @Override
    public GenomicDataSourceConfiguration createPersistentObject() {
        return new GenomicDataSourceConfiguration();
    }

    @Override
    public void setValues(GenomicDataSourceConfiguration config, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        config.setExperimentIdentifier(getUniqueString());
        ServerConnectionProfileGenerator.INSTANCE.setValues(config.getServerProfile(), nonCascadedObjects);
        config.setDnaAnalysisDataConfiguration(null);
        config.getSamples().clear();
        if (config.getDataType().equals(GenomicDataSourceDataTypeEnum.COPY_NUMBER)) {
            DnaAnalysisDataConfiguration cnDataConfig = new DnaAnalysisDataConfiguration();
            config.setDnaAnalysisDataConfiguration(cnDataConfig);
            cnDataConfig.setMappingFilePath(getUniqueString());
            cnDataConfig.setChangePointSignificanceLevel(getUniqueDouble());
            cnDataConfig.setEarlyStoppingCriterion(getUniqueDouble());
            cnDataConfig.setPermutationReplicates(getUniqueInt());
            cnDataConfig.setRandomNumberSeed(getUniqueInt());
            ServerConnectionProfileGenerator.INSTANCE.setValues(cnDataConfig.getSegmentationService(), nonCascadedObjects);
        } else if (config.getDataType().equals(GenomicDataSourceDataTypeEnum.EXPRESSION)) {
            for (int i = 0; i < 3; i++) {
                config.getSamples().add(SampleGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));
            }
            config.getControlSampleSetCollection().add(new SampleSet());
        }
    }


}
