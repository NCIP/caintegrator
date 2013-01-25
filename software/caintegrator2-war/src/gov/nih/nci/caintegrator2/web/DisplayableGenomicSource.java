/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

import java.util.ArrayList;
import java.util.List;

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

}
