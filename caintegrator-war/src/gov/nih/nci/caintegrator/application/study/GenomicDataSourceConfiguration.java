/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleRefreshTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.caarray.SampleIdentifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Records sample and array data retrieval information.
 */
public class GenomicDataSourceConfiguration extends AbstractCaIntegrator2Object implements TimeStampable {

    private static final long serialVersionUID = 1L;
    private static final Double DEFAULT_HIGH_VARIANCE_THRESHOLD = 50.0; // 50%
    private StudyConfiguration studyConfiguration;
    private ServerConnectionProfile serverProfile = new ServerConnectionProfile();
    private String experimentIdentifier;
    private PlatformDataTypeEnum dataType = PlatformDataTypeEnum.EXPRESSION;
    private PlatformVendorEnum platformVendor = PlatformVendorEnum.AFFYMETRIX;
    private String platformName;
    private String sampleMappingFileName = NONE_CONFIGURED;
    private String sampleMappingFilePath;
    private List<SampleIdentifier> sampleIdentifiers = new ArrayList<SampleIdentifier>();
    private List<Sample> samples = new ArrayList<Sample>();
    private Set<SampleSet> controlSampleSetCollection = new HashSet<SampleSet>();
    private ArrayDataLoadingTypeEnum loadingType = ArrayDataLoadingTypeEnum.PARSED_DATA;
    private DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration;
    private CentralTendencyTypeEnum technicalReplicatesCentralTendency = CentralTendencyTypeEnum.MEAN;
    private Status status = Status.NOT_LOADED;
    private String statusDescription;
    private Date lastModifiedDate;
    private Boolean useHighVarianceCalculation = true;
    private HighVarianceCalculationTypeEnum highVarianceCalculationType = HighVarianceCalculationTypeEnum.PERCENTAGE;
    private Double highVarianceThreshold = DEFAULT_HIGH_VARIANCE_THRESHOLD;
    private Map<String, Date> refreshSampleNames = new HashMap<String, Date>();
    private boolean dataRefreshed = false;

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
    @SuppressWarnings("unchecked")
    public List<Sample> getMappedSamples() {
        List<Sample> mappedSamples = new ArrayList<Sample>();
        mappedSamples.addAll(CollectionUtils.intersection(getSamples(), getStudyConfiguration().getSamples()));
        addRefreshFlagToMappedSamples(mappedSamples, getRefreshSampleNames());
        return mappedSamples;
    }

    /**
     * @return the control samples
     */
    @SuppressWarnings("unchecked")
    public List<Sample> getControlSamples() {
        List<Sample> controlSamples = new ArrayList<Sample>();
        if (CollectionUtils.isNotEmpty(getStudyConfiguration().getAllControlSamples())) {
            controlSamples.addAll(CollectionUtils.intersection(getSamples(),
                    getStudyConfiguration().getAllControlSamples()));
        }
        return controlSamples;
    }

    /**
     * @return the unmapped samples
     */
    public List<Sample> getUnmappedSamples() {
        List<Sample> unmappedSamples = new ArrayList<Sample>(getSamples());
        if (CollectionUtils.isNotEmpty(getStudyConfiguration().getAllControlSamples())) {
            unmappedSamples.removeAll(getStudyConfiguration().getAllControlSamples());
        }
        unmappedSamples.removeAll(getStudyConfiguration().getSamples());
        addRefreshFlagToUnMappedSamples(unmappedSamples, getRefreshSampleNames());
        return unmappedSamples;
    }

    private void addRefreshFlagToMappedSamples(Collection<Sample> mappedSamples, Map<String, Date> refreshMap) {
        for (Sample sample : mappedSamples) {
            if (!refreshMap.containsKey(sample.getName())) {
                sample.setRefreshType(SampleRefreshTypeEnum.DELETE_ON_REFRESH);
            } else if (studyConfiguration.getDeploymentFinishDate() != null
                    && sample.getCreationDate().after(studyConfiguration.getDeploymentFinishDate())) {
                sample.setRefreshType(SampleRefreshTypeEnum.ADD_ON_REFRESH);
            } else if (refreshMap.containsKey(sample.getName()) && refreshMap.get(sample.getName()) != null
                    && studyConfiguration.getDeploymentFinishDate() != null
                    && studyConfiguration.getDeploymentFinishDate().before(refreshMap.get(sample.getName()))) {
                sample.setRefreshType(SampleRefreshTypeEnum.UPDATE_ON_REFRESH);
            } else {
                sample.setRefreshType(SampleRefreshTypeEnum.UNCHANGED);
            }
        }
    }

    private void addRefreshFlagToUnMappedSamples(Collection<Sample> unmappedsamples, Map<String, Date> refreshMap) {
        List<Sample> samplesToAdd = new ArrayList<Sample>();
        for (Sample sample : unmappedsamples) {
            if (!refreshMap.containsKey(sample.getName())) {
                sample.setRefreshType(SampleRefreshTypeEnum.DELETE_ON_REFRESH);
            } else {
                sample.setRefreshType(SampleRefreshTypeEnum.UNCHANGED);
            }
        }
        for (String sampleName : refreshMap.keySet()) {
            if (!isSampleInList(sampleName)) {
                Sample newSample = new Sample();
                newSample.setName(sampleName);
                newSample.setRefreshType(SampleRefreshTypeEnum.ADD_ON_REFRESH);
                samplesToAdd.add(newSample);
            }
        }
        unmappedsamples.addAll(samplesToAdd);
    }

    private boolean isSampleInList(String nameToLookFor) {
        for (Sample sample : samples) {
            if (StringUtils.equals(sample.getName(), nameToLookFor)) {
                return true;
            }
        }
        return false;
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
    public PlatformVendorEnum getPlatformVendor() {
        return platformVendor;
    }

    /**
     * @param platformVendor the platformVendor to set
     */
    public void setPlatformVendor(PlatformVendorEnum platformVendor) {
        this.platformVendor = platformVendor;
    }

    /**
     * @return the dataType
     */
    public PlatformDataTypeEnum getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(PlatformDataTypeEnum dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the loadingType
     */
    public ArrayDataLoadingTypeEnum getLoadingType() {
        return loadingType;
    }

    /**
     * @param loadingType the loadingType to set
     */
    public void setLoadingType(ArrayDataLoadingTypeEnum loadingType) {
        this.loadingType = loadingType;
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
        for (SampleSet ss : controlSampleSetCollection) {
            addRefreshFlagToMappedSamples(ss.getSamples(), getRefreshSampleNames());
        }
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
        this.statusDescription = Cai2Util.trimDescription(statusDescription);
    }

    /**
     * Check for dataType is Expression.
     * @return boolean.
     */
    public boolean isExpressionData() {
        return PlatformDataTypeEnum.EXPRESSION.equals(dataType);
    }

    /**
     * Check for data type is CopyNumber.
     * @return boolean.
     */
    public boolean isCopyNumberData() {
        return PlatformDataTypeEnum.COPY_NUMBER.equals(dataType);
    }

    /**
     * Check for data type is SNP.
     * @return boolean.
     */
    public boolean isSnpData() {
        return PlatformDataTypeEnum.SNP.equals(dataType);
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
    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * @return isUseCghCall
     */
    public boolean isUseCghCall() {
        return isCopyNumberData() && dnaAnalysisDataConfiguration.isUseCghCall();
    }

    /**
     * @param refreshSampleNames the refreshSampleNames to set
     */
    public void setRefreshSampleNames(Map<String, Date> refreshSampleNames) {
        this.refreshSampleNames = refreshSampleNames;
    }

    /**
     * @return the refreshSampleNames
     */
    public Map<String, Date> getRefreshSampleNames() {
        return refreshSampleNames;
    }

    /**
     * @return the dataRefreshed
     */
    public boolean isDataRefreshed() {
        return dataRefreshed;
    }

    /**
     * @param dataRefreshed the dataRefreshed to set
     */
    public void setDataRefreshed(boolean dataRefreshed) {
        this.dataRefreshed = dataRefreshed;
    }
}
