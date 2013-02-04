/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.HighVarianceCalculationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * 
 */
public class DisplayableGenomicSource {
    private final GenomicDataSourceConfiguration genomicDataSourceConfiguration;
    private final List<Platform> platforms = new ArrayList<Platform>();


    /**
     * Contructor which wraps the GenomicDataSource.
     * @param genomicDataSourceConfiguration - genomic data source.
     */
    public DisplayableGenomicSource(GenomicDataSourceConfiguration genomicDataSourceConfiguration) {
        if (genomicDataSourceConfiguration == null) {
            throw new IllegalStateException("GenomicDataSourceConfiguration cannot be null.");
        }
        this.genomicDataSourceConfiguration = genomicDataSourceConfiguration;
    }
    
    /**
     * Number of samples associated with this source.
     * @return number of samples.
     */
    public int getNumberSamples() {
        return genomicDataSourceConfiguration.getSamples().size();
    }
    
    /**
     * Number of control samples per control sample set associated with this source.
     * @return number of control samples.
     */
    public String getNumberControlSampleSetSamples() {
        return genomicDataSourceConfiguration.getControlSampleSetCommaSeparated();
    }
    
    /**
     * Number of control samples associated with this source.
     * @return number of control samples.
     */
    public int getNumberControlSamples() {
        return genomicDataSourceConfiguration.getControlSamples().size();
    }
    
    /**
     * Return T/F depending if there are control samples set for this data source.
     * @return T/F value.
     */
    public boolean isControlSamplesSet() {
        return getNumberControlSamples() > 0 ? true : false;
    }
    
    /**
     * @return T/F depending if there is caArray URL.
     */
    public boolean hasCaArrayUrl() {
        return !StringUtils.isBlank(genomicDataSourceConfiguration.getServerProfile().getUrl());
    }
    
    /**
     * @return the CaArray URL for this experiment.
     */
    public String getCaArrayUrl() {
        return genomicDataSourceConfiguration.getServerProfile().getUrl()
            + "/project/" + genomicDataSourceConfiguration.getExperimentIdentifier();
    }
    
    /**
     * Experiment name for the data source.
     * @return - Name of experiment.
     */
    public String getExperimentName() {
        return genomicDataSourceConfiguration.getExperimentIdentifier();
    }

    /**
     * @return the genomicDataSourceConfiguration
     */
    public GenomicDataSourceConfiguration getGenomicDataSourceConfiguration() {
        return genomicDataSourceConfiguration;
    }
    
    /**
     * @return the platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * @return the hostname
     */
    public String getHostName() {
        return genomicDataSourceConfiguration.getServerProfile().getHostname();
    }

    /**
     * @return the data type
     */
    public String getDataType() {
        return genomicDataSourceConfiguration.getDataTypeString();
    }

    /**
     * Check for dataType is Expression.
     * @return boolean.
     */
    public boolean isExpressionData() {
        return genomicDataSourceConfiguration.isExpressionData();
    }

    /**
     * Check for dataType is Copy Number.
     * @return boolean.
     */
    public boolean isCopyNumberData() {
        return genomicDataSourceConfiguration.isCopyNumberData();
    }

    /**
     * Check for dataType is SNP.
     * @return boolean.
     */
    public boolean isSnpData() {
        return genomicDataSourceConfiguration.isSnpData();
    }

    /**
     * 
     * @return last modified date.
     */
    public String getDisplayableLastModifiedDate() {
        return genomicDataSourceConfiguration.getDisplayableLastModifiedDate();
    }
    
    /**
     * 
     * @return central tendency.
     */
    public String getTechnicalReplicateCentralTendency() {
        return genomicDataSourceConfiguration.getTechnicalReplicatesCentralTendencyString();
    }
    
    /**
     * 
     * @return use high variance.
     */
    public boolean isUseHighVarianceCalculation() {
        return genomicDataSourceConfiguration.isUseHighVarianceCalculation();
    }
    
    /**
     * 
     * @return high variance threshold.
     */
    public String getHighVarianceThresholdString() {
        if (isUseHighVarianceCalculation()) {
            Double highVarianceThreshold = genomicDataSourceConfiguration.getHighVarianceThreshold();
            if (HighVarianceCalculationTypeEnum.PERCENTAGE.equals(
                 genomicDataSourceConfiguration.getHighVarianceCalculationType())) {
                return highVarianceThreshold + "%";
            }
            return String.valueOf(highVarianceThreshold);
        }
        return "";
    }
    
    /**
     * 
     * @return high variance label.
     */
    public String getHighVarianceThresholdLabel() {
        StringBuffer returnString = new StringBuffer("");
        if (isUseHighVarianceCalculation()) {
            if (HighVarianceCalculationTypeEnum.PERCENTAGE.equals(
                    genomicDataSourceConfiguration.getHighVarianceCalculationType())) {
                returnString.append("Relative ");
            }
            returnString.append("Standard Deviation Threshold for Technical Replicates:");
        }
        return returnString.toString();
    }
}
