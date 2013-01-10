/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Provides persistent access to a file that maps subjects to samples to copy number data files in caArray.
 * 
 * <p>The format of the file should be subject_id,sample_name,cnchp_filename on each line.
 */
public class DnaAnalysisDataConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Double DEFAULT_CHANGE_POINT_SIGNIFICANCE_LEVEL = 0.01;
    private static final Double DEFAULT_EARLY_STOPPING_CRITERION = 0.05;
    private static final Integer DEFAULT_PERMUTATION_REPLICATES = 10000;
    private static final Integer DEFAULT_RANDOM_NUMBER_SEED = 25;
    
    private String mappingFilePath;
    private Double changePointSignificanceLevel = DEFAULT_CHANGE_POINT_SIGNIFICANCE_LEVEL;
    private Double earlyStoppingCriterion = DEFAULT_EARLY_STOPPING_CRITERION;
    private Integer permutationReplicates = DEFAULT_PERMUTATION_REPLICATES;
    private Integer randomNumberSeed = DEFAULT_RANDOM_NUMBER_SEED;
    private ServerConnectionProfile segmentationService = new ServerConnectionProfile();
    private Boolean useCghCall = false;
    private Integer numberLevelCall = 3;

    /**
     * @return the mappingFilePath
     */
    public String getMappingFilePath() {
        return mappingFilePath;
    }

    /**
     * @param mappingFilePath the mappingFilePath to set
     */
    public void setMappingFilePath(String mappingFilePath) {
        this.mappingFilePath = mappingFilePath;
    }

    /**
     * The file.
     * 
     * @return the file.
     * @throws FileNotFoundException when file path is null.
     */
    public File getMappingFile() throws FileNotFoundException {
        if (getMappingFilePath() == null) {
            throw new FileNotFoundException("File path is null.");
        } else {
            return new File(getMappingFilePath());
        }
    }

    /**
     * @return the changePointSignificanceLevel
     */
    public Double getChangePointSignificanceLevel() {
        return changePointSignificanceLevel;
    }

    /**
     * @param changePointSignificanceLevel the changePointSignificanceLevel to set
     */
    public void setChangePointSignificanceLevel(Double changePointSignificanceLevel) {
        this.changePointSignificanceLevel = changePointSignificanceLevel;
    }

    /**
     * @return the earlyStoppingCriterion
     */
    public Double getEarlyStoppingCriterion() {
        return earlyStoppingCriterion;
    }

    /**
     * @param earlyStoppingCriterion the earlyStoppingCriterion to set
     */
    public void setEarlyStoppingCriterion(Double earlyStoppingCriterion) {
        this.earlyStoppingCriterion = earlyStoppingCriterion;
    }

    /**
     * @return the permutationReplicates
     */
    public Integer getPermutationReplicates() {
        return permutationReplicates;
    }

    /**
     * @param permutationReplicates the permutationReplicates to set
     */
    public void setPermutationReplicates(Integer permutationReplicates) {
        this.permutationReplicates = permutationReplicates;
    }

    /**
     * @return the randomNumberSeed
     */
    public Integer getRandomNumberSeed() {
        return randomNumberSeed;
    }

    /**
     * @param randomNumberSeed the randomNumberSeed to set
     */
    public void setRandomNumberSeed(Integer randomNumberSeed) {
        this.randomNumberSeed = randomNumberSeed;
    }

    /**
     * @return the segmentationService
     */
    public ServerConnectionProfile getSegmentationService() {
        return segmentationService;
    }

    /**
     * @param segmentationService the segmentationService to set
     */
    public void setSegmentationService(ServerConnectionProfile segmentationService) {
        this.segmentationService = segmentationService;
    }

    /**
     * @return true if configured to use CaDNACopy.
     */
    public boolean isCaDNACopyConfiguration() {
        return getSegmentationService().getUrl() != null && getSegmentationService().getUrl().endsWith("/CaDNAcopy");
    }

    /**
     * @return true if configured to use CaDNACopy.
     */
    public boolean isCaCGHCallConfiguration() {
        return getSegmentationService().getUrl() != null && getSegmentationService().getUrl().endsWith("/CaCGHcall");
    }

    /**
     * @return the useCghCall
     */
    public Boolean isUseCghCall() {
        return useCghCall;
    }

    /**
     * @return the useCghCall
     */
    public Boolean getUseCghCall() {
        return useCghCall;
    }

    /**
     * @param useCghCall the useCghCall to set
     */
    public void setUseCghCall(Boolean useCghCall) {
        this.useCghCall = useCghCall;
    }

    /**
     * @return the numberLevelCall
     */
    public Integer getNumberLevelCall() {
        return numberLevelCall;
    }

    /**
     * @param numberLevelCall the numberLevelCall to set
     */
    public void setNumberLevelCall(Integer numberLevelCall) {
        this.numberLevelCall = numberLevelCall;
    }
}
