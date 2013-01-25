/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.SampleIdentifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Records sample and array data retrieval information.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.TooManyFields", "PMD.ExcessiveClassLength" })
public class GenomicDataSourceConfiguration extends AbstractCaIntegrator2Object implements TimeStampable {
    
    private static final long serialVersionUID = 1L;
    private static final Double DEFAULT_HIGH_VARIANCE_THRESHOLD = 50.0; // 50%
    private StudyConfiguration studyConfiguration;
    private ServerConnectionProfile serverProfile = new ServerConnectionProfile();
    private String experimentIdentifier;
    private GenomicDataSourceDataTypeEnum dataType = GenomicDataSourceDataTypeEnum.EXPRESSION;
    private String platformVendor;
    private String platformName;
    private String sampleMappingFileName = NONE_CONFIGURED;
    private String sampleMappingFilePath;
    private List<SampleIdentifier> sampleIdentifiers = new ArrayList<SampleIdentifier>();
    private List<Sample> samples = new ArrayList<Sample>();
    private Set<SampleSet> controlSampleSetCollection = new HashSet<SampleSet>();
    private Boolean singleDataFile = false;
    private boolean useSupplementalFiles = false;
    private DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration;
    private CentralTendencyTypeEnum technicalReplicatesCentralTendency = CentralTendencyTypeEnum.MEAN;
    private Status status = Status.NOT_LOADED;
    private String statusDescription;
    private Date lastModifiedDate;
    private Boolean useHighVarianceCalculation = true;
    private HighVarianceCalculationTypeEnum highVarianceCalculationType = HighVarianceCalculationTypeEnum.PERCENTAGE;
    private Double highVarianceThreshold = DEFAULT_HIGH_VARIANCE_THRESHOLD;
    
    /**
     * Mapping file is not configured.
     */
    public static final String NONE_CONFIGURED = "None Configured";

    /**
     * @return the experimentIdentifier
     */
    public String getExperimentIdentifier() {
        return experimentIdentifier;
    }
    
    /**
     * @return the sampleIdentifiers
     */
    public List<SampleIdentifier> getSampleIdentifiers() {
        return sampleIdentifiers;
    }

    /**
     * @return the serverProfile
     */
    public ServerConnectionProfile getServerProfile() {
        return serverProfile;
    }

    /**
     * @param experimentIdentifier the experimentIdentifier to set
     */
    public void setExperimentIdentifier(String experimentIdentifier) {
        this.experimentIdentifier = experimentIdentifier;
    }

    @SuppressWarnings("unused")
    private void setServerProfile(ServerConnectionProfile serverProfile) {
        this.serverProfile = serverProfile;
    }

    @SuppressWarnings("unused")
    private void setSampleIdentifiers(List<SampleIdentifier> sampleIdentifiers) {
        this.sampleIdentifiers = sampleIdentifiers;
    }

    /**
     * @return the samples
     */
    public List<Sample> getSamples() {
        return samples;
    }

    /**
     * @param samples the samples to set
     */
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    /**
     * @return the mapped samples
     */
    public List<Sample> getMappedSamples() {
        List<Sample> mappedSamples = new ArrayList<Sample>();
        mappedSamples.addAll(getSamples());
        mappedSamples.retainAll(getStudyConfiguration().getSamples());
        return mappedSamples;
    }

    /**
     * @return the control samples
     */
    public List<Sample> getControlSamples() {
        if (!getStudyConfiguration().getAllControlSamples().isEmpty()) {
            List<Sample> controlSamples = new ArrayList<Sample>();
            controlSamples.addAll(getStudyConfiguration().getAllControlSamples());
            controlSamples.retainAll(getSamples());
            return controlSamples;
        } else {
            return Collections.emptyList();
        }    
    }

    /**
     * @return the unmapped samples
     */
    public List<Sample> getUnmappedSamples() {
        List<Sample> unmappedSamples = new ArrayList<Sample>();
        unmappedSamples.addAll(getSamples());
        if (!getStudyConfiguration().getAllControlSamples().isEmpty()) {
            unmappedSamples.removeAll(getStudyConfiguration().getAllControlSamples());
        }
        unmappedSamples.removeAll(getStudyConfiguration().getSamples());
        return unmappedSamples;
    }

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the platformVendor
     */
    public String getPlatformVendor() {
        return platformVendor;
    }

    /**
     * @param platformVendor the platformVendor to set
     */
    public void setPlatformVendor(String platformVendor) {
        this.platformVendor = platformVendor;
    }

    /**
     * @return the dataType
     */
    public GenomicDataSourceDataTypeEnum getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(GenomicDataSourceDataTypeEnum dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the resultType
     */
    public String getDataTypeString() {
        if (dataType == null) {
            return "";
        } else {
            return dataType.getValue();
        }
    }

    /**
     * @param dataTypeString the dataType string value to set
     */
    public void setDataTypeString(String dataTypeString) {
        if (StringUtils.isBlank(dataTypeString)) {
            this.dataType = null;
        } else {
            this.dataType = GenomicDataSourceDataTypeEnum.getByValue(dataTypeString);
        }
    }

    /**
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
    
    /**
     * @return the dnaAnalysisDataConfiguration
     */
    public DnaAnalysisDataConfiguration getDnaAnalysisDataConfiguration() {
        return dnaAnalysisDataConfiguration;
    }

    /**
     * @param dnaAnalysisDataConfiguration the dnaAnalysisDataConfiguration to set
     */
    public void setDnaAnalysisDataConfiguration(DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration) {
        this.dnaAnalysisDataConfiguration = dnaAnalysisDataConfiguration;
    }

    /**
     * @return the sampleMappingFileName
     */
    public String getSampleMappingFileName() {
        return sampleMappingFileName;
    }

    /**
     * @param sampleMappingFileName the sampleMappingFileName to set
     */
    public void setSampleMappingFileName(String sampleMappingFileName) {
        this.sampleMappingFileName = sampleMappingFileName;
    }

    /**
     * Used for the visual display of the control sample mapping file names.
     * @return list of control sample mapping file names.
     */
    public List<String> getControlSampleMappingFileNames() {
        List<String> controlSampleMappingFileNames = new ArrayList<String>();
        if (controlSampleSetCollection.isEmpty()) {
            controlSampleMappingFileNames.add(NONE_CONFIGURED);
        } else {
            for (SampleSet controlSampleSet : controlSampleSetCollection) {
                controlSampleMappingFileNames.add(controlSampleSet.getFileName());
            }
        }
        return controlSampleMappingFileNames;
    }

    /**
     * Get all control sample set names.
     * @return list of control sample set names.
     */
    public List<String> getControlSampleSetNames() {
        List<String> controlSampleSetNames = new ArrayList<String>();
            for (SampleSet controlSampleSet : controlSampleSetCollection) {
                controlSampleSetNames.add(controlSampleSet.getName());
            }
        return controlSampleSetNames;
    }
    
    /**
     * Used for the visual display of the dna analysis mapping file name.
     * @return file name.
     */
    public String getDnaAnalysisMappingFileName() {
        try {
            if (getDnaAnalysisDataConfiguration() != null 
                && getDnaAnalysisDataConfiguration().getMappingFile() != null) {
                return getDnaAnalysisDataConfiguration().getMappingFile().getName();
            }
        } catch (FileNotFoundException e) {
            return NONE_CONFIGURED;
        }
        return NONE_CONFIGURED;
    }

    /**
     * Returns a sample by name.
     * 
     * @param sampleName name to search for.
     * @return the matching sample.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")   // best way to do check
    public Sample getSample(String sampleName) {
        for (Sample sample : getSamples()) {
            if (sampleName == null && sample.getName() == null) {
                return sample;
            } else if (sampleName == null && sample.getName() != null) {
                continue;
            } else if (sampleName.equals(sample.getName())) {
                return sample;
            }
        }
        return null;
    }

    /**
     * @return the controlSampleSetCollection
     */
    public Set<SampleSet> getControlSampleSetCollection() {
        return controlSampleSetCollection;
    }

    /**
     * @param defaultControlSampleSet the defaultControlSampleSet to set
     */
    @SuppressWarnings("unused") // required by Hibernate
    private void setControlSampleSetCollection(Set<SampleSet> controlSampleSetCollection) {
        this.controlSampleSetCollection = controlSampleSetCollection;
    }

    /**
     * @param name the controlSampleSet name
     * @return the requested controlSampleSet
     */
    public SampleSet getControlSampleSet(String name) {
        for (SampleSet controlSampleSet : controlSampleSetCollection) {
            if (controlSampleSet.getName().equalsIgnoreCase(name)) {
                return controlSampleSet;
            }
        }
        return null;
    }
    
    /**
     * Get all control samples.
     * @return set of all control samples
     */
    public Set<Sample> getAllControlSamples() {
        Set<Sample> controlSamples = new HashSet<Sample>();
        for (SampleSet sampleSet : controlSampleSetCollection) {
            controlSamples.addAll(sampleSet.getSamples());
        }
        return controlSamples;
    }
    
    /**
     * Get comma separated Control sample set name and number of samples in the each set.
     * @return a string of comma separated values.
     */
    public String getControlSampleSetCommaSeparated() {
        StringBuffer resultBuffer = new StringBuffer();
        for (SampleSet controlSampleSet : controlSampleSetCollection) {
            if (resultBuffer.length() > 0) {
                resultBuffer.append(", ");
            }
            resultBuffer.append(controlSampleSet.getName());
            resultBuffer.append(": ");
            resultBuffer.append(controlSampleSet.getSamples().size());
        }
        return resultBuffer.toString();
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
    
    /**
     * Check for dataType is Expression.
     * @return boolean.
     */
    public boolean isExpressionData() {
        return GenomicDataSourceDataTypeEnum.EXPRESSION.equals(dataType);
    }
    
    /**
     * Check for data type is CopyNumber.
     * @return boolean.
     */
    public boolean isCopyNumberData() {
        return GenomicDataSourceDataTypeEnum.COPY_NUMBER.equals(dataType);
    }
    
    /**
     * Check for data type is SNP.
     * @return boolean.
     */
    public boolean isSnpData() {
        return GenomicDataSourceDataTypeEnum.SNP.equals(dataType);
    }

    /**
     * @return the sampleMappingFilePath
     */
    public String getSampleMappingFilePath() {
        return sampleMappingFilePath;
    }

    /**
     * @param sampleMappingFilePath the sampleMappingFilePath to set
     */
    public void setSampleMappingFilePath(String sampleMappingFilePath) {
        this.sampleMappingFilePath = sampleMappingFilePath;
    }

    /**
     * The file.
     * 
     * @return the file.
     * @throws FileNotFoundException when file path is null.
     */
    public File getSampleMappingFile() throws FileNotFoundException {
        if (getSampleMappingFilePath() == null) {
            throw new FileNotFoundException("Sample mapping file path is null.");
        } else {
            return new File(getSampleMappingFilePath());
        }
    }

    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * 
     */
    public void deleteSampleMappingFile() {
        setSampleMappingFileName(NONE_CONFIGURED);
        setSampleMappingFilePath(null);
        setStatus(Status.NOT_MAPPED);
    }

    /**
     * @return the useSupplementalFiles
     */
    public boolean isUseSupplementalFiles() {
        return useSupplementalFiles;
    }

    /**
     * @param useSupplementalFiles the useSupplementalFiles to set
     */
    public void setUseSupplementalFiles(boolean useSupplementalFiles) {
        this.useSupplementalFiles = useSupplementalFiles;
    }

    /**
     * @return the singleDataFile
     */
    public Boolean isSingleDataFile() {
        return singleDataFile;
    }

    /**
     * @param singleDataFile the singleDataFile to set
     */
    public void setSingleDataFile(Boolean singleDataFile) {
        this.singleDataFile = singleDataFile;
    }

    /**
     * @return the technicalReplicatesCentralTendency
     */
    public CentralTendencyTypeEnum getTechnicalReplicatesCentralTendency() {
        return technicalReplicatesCentralTendency;
    }

    /**
     * @param technicalReplicatesCentralTendency the technicalReplicatesCentralTendency to set
     */
    public void setTechnicalReplicatesCentralTendency(CentralTendencyTypeEnum technicalReplicatesCentralTendency) {
        this.technicalReplicatesCentralTendency = technicalReplicatesCentralTendency;
    }
    
    /**
     * @return the resultType
     */
    public String getTechnicalReplicatesCentralTendencyString() {
        if (technicalReplicatesCentralTendency == null) {
            return "";
        } else {
            return technicalReplicatesCentralTendency.getValue();
        }
    }

    /**
     * @param technicalReplicatesCentralTendencyString the technicalReplicatesCentralTendency string value to set
     */
    public void setTechnicalReplicatesCentralTendencyString(String technicalReplicatesCentralTendencyString) {
        if (StringUtils.isBlank(technicalReplicatesCentralTendencyString)) {
            this.technicalReplicatesCentralTendency = null;
        } else {
            this.technicalReplicatesCentralTendency =
                CentralTendencyTypeEnum.getByValue(technicalReplicatesCentralTendencyString);
        }
    }

    /**
     * @return the useHighVarianceCalculation
     */
    public Boolean isUseHighVarianceCalculation() {
        return useHighVarianceCalculation;
    }

    /**
     * @param useHighVarianceCalculation the useHighVarianceCalculation to set
     */
    public void setUseHighVarianceCalculation(Boolean useHighVarianceCalculation) {
        this.useHighVarianceCalculation = useHighVarianceCalculation;
    }

    /**
     * @return the highVarianceCalculationType
     */
    public HighVarianceCalculationTypeEnum getHighVarianceCalculationType() {
        return highVarianceCalculationType;
    }

    /**
     * @param highVarianceCalculationType the highVarianceCalculationType to set
     */
    public void setHighVarianceCalculationType(HighVarianceCalculationTypeEnum highVarianceCalculationType) {
        this.highVarianceCalculationType = highVarianceCalculationType;
    }
    
    /**
     * @return the resultType
     */
    public String getHighVarianceCalculationTypeString() {
        if (highVarianceCalculationType == null) {
            return "";
        } else {
            return highVarianceCalculationType.getValue();
        }
    }

    /**
     * @param highVarianceCalculationTypeString the highVarianceCalculationType string value to set
     */
    public void setHighVarianceCalculationTypeString(String highVarianceCalculationTypeString) {
        if (StringUtils.isBlank(highVarianceCalculationTypeString)) {
            this.highVarianceCalculationType = null;
        } else {
            this.highVarianceCalculationType =
                HighVarianceCalculationTypeEnum.getByValue(highVarianceCalculationTypeString);
        }
    }

    /**
     * @return the highVarianceThreshold
     */
    public Double getHighVarianceThreshold() {
        return highVarianceThreshold;
    }

    /**
     * @param highVarianceThreshold the highVarianceThreshold to set
     */
    public void setHighVarianceThreshold(Double highVarianceThreshold) {
        this.highVarianceThreshold = highVarianceThreshold;
    }
}
