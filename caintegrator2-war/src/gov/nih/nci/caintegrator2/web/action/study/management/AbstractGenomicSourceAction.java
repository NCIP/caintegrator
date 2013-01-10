/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Base class for actions that require retrieval of persistent <code>GenomicDataSourceConfigurations</code>.
 */
public abstract class AbstractGenomicSourceAction extends AbstractStudyAction {

    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;

    private GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getGenomicSource().getId() != null) {
            setGenomicSource(getStudyManagementService().getRefreshedEntity(getGenomicSource()));
            HibernateUtil.loadGenomicSource(getGenomicSource());
        }
    }

    /**
     * Cancels the creation of a genomic source to return back to study screen.
     * @return struts string.
     */
    public String cancel() {
        return SUCCESS;
    }

    /**
     * @return the genomicSource
     */
    public GenomicDataSourceConfiguration getGenomicSource() {
        return genomicSource;
    }

    /**
     * @param genomicSource the genomicSource to set
     */
    public void setGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        this.genomicSource = genomicSource;
    }

    /**
     * Delete a genomic source file.
     * @return struts string.
     */
    public String delete() {
        if (getGenomicSource() == null
           || !getStudyConfiguration().getGenomicDataSources().contains(getGenomicSource())) {
            return SUCCESS;
        }
        setStudyLastModifiedByCurrentUser(getGenomicSource(),
                LogEntry.getSystemLogDelete(getGenomicSource()));
        getStudyManagementService().delete(getStudyConfiguration(), getGenomicSource());
        return SUCCESS;
    }

    /**
     * @return a copy of the genomicDataSourceConfiguration
     */
    protected GenomicDataSourceConfiguration createNewGenomicSource() {
        GenomicDataSourceConfiguration configuration = new GenomicDataSourceConfiguration();
        ServerConnectionProfile newProfile = configuration.getServerProfile();
        ServerConnectionProfile oldProfile = getGenomicSource().getServerProfile();
        newProfile.setUrl(oldProfile.getUrl());
        newProfile.setHostname(oldProfile.getHostname());
        newProfile.setPort(oldProfile.getPort());
        newProfile.setUsername(oldProfile.getUsername());
        newProfile.setPassword(oldProfile.getPassword());
        configuration.setExperimentIdentifier(getGenomicSource().getExperimentIdentifier());
        configuration.setDataType(getGenomicSource().getDataType());
        configuration.setPlatformVendor(getGenomicSource().getPlatformVendor());
        configuration.setPlatformName(getGenomicSource().getPlatformName());
        configuration.setSampleMappingFileName(getGenomicSource().getSampleMappingFileName());
        configuration.setSampleMappingFilePath(getGenomicSource().getSampleMappingFilePath());
        configuration.setLoadingType(getGenomicSource().getLoadingType());

        if (getGenomicSource().getDnaAnalysisDataConfiguration() != null) {
            copyDnaConfiguration(configuration);
        }

        configuration.setTechnicalReplicatesCentralTendency(getGenomicSource().getTechnicalReplicatesCentralTendency());
        configuration.setUseHighVarianceCalculation(getGenomicSource().isUseHighVarianceCalculation());
        configuration.setHighVarianceCalculationType(getGenomicSource().getHighVarianceCalculationType());
        configuration.setHighVarianceThreshold(getGenomicSource().getHighVarianceThreshold());
        return configuration;
    }

    /**
     * @param configuration
     */
    private void copyDnaConfiguration(GenomicDataSourceConfiguration configuration) {
        DnaAnalysisDataConfiguration newDnaConfiguration = new DnaAnalysisDataConfiguration();
        DnaAnalysisDataConfiguration oldDnaConfiguration = getGenomicSource().getDnaAnalysisDataConfiguration();
        configuration.setDnaAnalysisDataConfiguration(newDnaConfiguration);
        newDnaConfiguration.setMappingFilePath(oldDnaConfiguration.getMappingFilePath());
        newDnaConfiguration.setChangePointSignificanceLevel(oldDnaConfiguration.getChangePointSignificanceLevel());
        newDnaConfiguration.setEarlyStoppingCriterion(oldDnaConfiguration.getEarlyStoppingCriterion());
        newDnaConfiguration.setPermutationReplicates(oldDnaConfiguration.getPermutationReplicates());
        newDnaConfiguration.setRandomNumberSeed(oldDnaConfiguration.getRandomNumberSeed());
        newDnaConfiguration.setUseCghCall(oldDnaConfiguration.getUseCghCall());

        ServerConnectionProfile newSegmentProfile = configuration.getDnaAnalysisDataConfiguration()
                .getSegmentationService();
        ServerConnectionProfile oldSegmentProfile = getGenomicSource().getDnaAnalysisDataConfiguration()
                .getSegmentationService();
        if (oldSegmentProfile != null) {
            newSegmentProfile.setUrl(oldSegmentProfile.getUrl());
            newSegmentProfile.setHostname(oldSegmentProfile.getHostname());
            newSegmentProfile.setPort(oldSegmentProfile.getPort());
            newSegmentProfile.setUsername(oldSegmentProfile.getUsername());
            newSegmentProfile.setPassword(oldSegmentProfile.getPassword());
        }
        newDnaConfiguration.setSegmentationService(newSegmentProfile);
    }

    /**
     * @return is Agilent
     */
    protected boolean isAgilent() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor());
    }

    /**
     * @return is Affymetric gene expression
     */
    protected boolean isAffyExpression() {
        return PlatformVendorEnum.AFFYMETRIX.getValue().equals(getGenomicSource().getPlatformVendor())
        && PlatformDataTypeEnum.EXPRESSION.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Affymetrix copy number
     */
    protected boolean isAffyCopyNumber() {
        return PlatformVendorEnum.AFFYMETRIX.getValue().equals(getGenomicSource().getPlatformVendor())
        && PlatformDataTypeEnum.COPY_NUMBER.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Agilent gene expression
     */
    protected boolean isAgilentExpression() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
        && PlatformDataTypeEnum.EXPRESSION.equals(getGenomicSource().getDataType());
    }

    /**
     * @return is Agilent copy number
     */
    protected boolean isAgilentCopyNumber() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
        && PlatformDataTypeEnum.COPY_NUMBER.equals(getGenomicSource().getDataType());
    }

}
