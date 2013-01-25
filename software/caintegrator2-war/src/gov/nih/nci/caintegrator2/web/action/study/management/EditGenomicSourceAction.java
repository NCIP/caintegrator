/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.web.ajax.IGenomicDataSourceAjaxUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * Action called to create or edit a <code>GenomicDataSourceConfiguration</code>.
 */
public class EditGenomicSourceAction extends AbstractGenomicSourceAction {

    private static final long serialVersionUID = 1L;
    
    private IGenomicDataSourceAjaxUpdater updater;
    private ArrayDataService arrayDataService;
    private CaArrayFacade caArrayFacade;
    private ConfigurationHelper configurationHelper;
    
    private boolean mappingData = true;

    /**
     * @return the mappingData
     */
    public boolean isMappingData() {
        return mappingData;
    }

    /**
     * @param mappingData the mappingData to set
     */
    public void setMappingData(boolean mappingData) {
        this.mappingData = mappingData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return checkEmptyPlatformTypes();
    }
    
    private String checkEmptyPlatformTypes() {
        for (PlatformConfiguration platformConfiguration : getArrayDataService().getPlatformConfigurations()) {
            if (Status.LOADED.equals(platformConfiguration.getStatus())
                    && (platformConfiguration.getPlatformType() == null
                            || platformConfiguration.getPlatformChannelType() == null)) {
                addActionError("Some Platforms are missing 'Platform Type/Platform Channel Type',"
                    + " please go to the Manage Platforms page to set them.");
                return ERROR;
            }
        }
        return SUCCESS;
    }
    
    /**
     * Set default platform vendor and type.
     * @return struts string.
     */
    public String addNew() {
        getGenomicSource().setPlatformName("");
        getGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX.getValue());
        getGenomicSource().setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
        getGenomicSource().getServerProfile().setHostname(
                getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_HOST));
        getGenomicSource().getServerProfile().setPort(
            Integer.valueOf(getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_PORT)));
        getGenomicSource().getServerProfile().setUrl(
                getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_URL));
        return checkEmptyPlatformTypes();
    }
    
    /**
     * Refresh the page.
     * @return struts string.
     */
    public String refresh() {
        if (!isAffyExpression()) {
            getGenomicSource().setUseSupplementalFiles(true);
        }
        return SUCCESS;
    }
    
    /**
     * Cancels the creation of a genomic source to return back to study screen.
     * @return struts string.
     */
    public String cancel() {
        return SUCCESS;
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
     * {@inheritDoc}
     */
    public String save() {
        if (!validateSave()) {
            return INPUT;
        }
        if (isMappingData()) {
            getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
            if (getGenomicSource().getId() == null) {
                runAsynchronousGenomicDataRetrieval(getGenomicSource());
            } else { // Need to create a new source, delete the old one, and load the new one
                GenomicDataSourceConfiguration newGenomicSource = createNewGenomicSource();
                delete();
                runAsynchronousGenomicDataRetrieval(newGenomicSource);
            }
        } else {
            setStudyLastModifiedByCurrentUser(getGenomicSource(), 
                    LogEntry.getSystemLogSave(getGenomicSource()));
            getStudyManagementService().save(getStudyConfiguration());
        }
        return SUCCESS;
    }

    private GenomicDataSourceConfiguration createNewGenomicSource() {
        GenomicDataSourceConfiguration configuration = new GenomicDataSourceConfiguration();
        ServerConnectionProfile newProfile = configuration.getServerProfile();
        ServerConnectionProfile oldProfile = getGenomicSource().getServerProfile();
        newProfile.setUrl(oldProfile.getUrl());
        newProfile.setHostname(oldProfile.getHostname());
        newProfile.setPort(oldProfile.getPort());
        newProfile.setUsername(oldProfile.getUsername());
        newProfile.setPassword(oldProfile.getPassword());
        configuration.setExperimentIdentifier(getGenomicSource().getExperimentIdentifier());
        configuration.setPlatformVendor(getGenomicSource().getPlatformVendor());
        configuration.setPlatformName(getGenomicSource().getPlatformName());
        configuration.setDataType(getGenomicSource().getDataType());
        configuration.setUseSupplementalFiles(getGenomicSource().isUseSupplementalFiles());
        configuration.setSingleDataFile(getGenomicSource().isSingleDataFile());
        configuration.setTechnicalReplicatesCentralTendency(getGenomicSource().getTechnicalReplicatesCentralTendency());
        configuration.setUseHighVarianceCalculation(getGenomicSource().isUseHighVarianceCalculation());
        configuration.setHighVarianceThreshold(getGenomicSource().getHighVarianceThreshold());
        configuration.setHighVarianceCalculationType(getGenomicSource().getHighVarianceCalculationType());
        return configuration;
    }

    private void runAsynchronousGenomicDataRetrieval(GenomicDataSourceConfiguration genomicSource) {
        getDisplayableWorkspace().setCurrentStudyConfiguration(getStudyConfiguration());
        genomicSource.setStatus(Status.PROCESSING);
        getStudyManagementService().addGenomicSourceToStudy(getStudyConfiguration(), genomicSource);
        setStudyLastModifiedByCurrentUser(genomicSource, LogEntry.getSystemLogLoad(getGenomicSource()));
        updater.runJob(genomicSource.getId());
    }

    private boolean validateSave() {
        if (isPlatformNameRequired()
                && StringUtils.isEmpty(getGenomicSource().getPlatformName())) {
            addFieldError("genomicSource.platformName", "Platform name is required for using supplemental files.");
            return false;
        }
        if (!validateGenomicSourceConnection()) {
            return false;
        }
        if (!validateHighVarianceParameters()) {
            return false;
        }
        return true;
    }

    private boolean validateGenomicSourceConnection() {
        try {
            caArrayFacade.validateGenomicSourceConnection(getGenomicSource());
        } catch (ConnectionException e) {
            addFieldError("genomicSource.serverProfile.hostname", "Unable to connect to server.");
            return false;
        } catch (ExperimentNotFoundException e) {
            addFieldError("genomicSource.experimentIdentifier", "Experiment identifier not found on server.");
            return false;
        }
        return checkUseSupplementalFiles();
    }
    
    private boolean checkUseSupplementalFiles() {
        if (!isAffyExpression()) {
            getGenomicSource().setUseSupplementalFiles(true);
        }
        return true;
    }

    
    private boolean validateHighVarianceParameters() {
        if (getGenomicSource().isUseHighVarianceCalculation()
                && (getGenomicSource().getHighVarianceThreshold() == null || getGenomicSource()
                        .getHighVarianceThreshold() <= 0)) {
            addFieldError("genomicSource.highVarianceThreshold", 
                    "High Variance Threshold must be a number greater than 0.");
            return false;
        }
        return true;
    }
    
    private boolean isPlatformNameRequired() {
        return getGenomicSource().isUseSupplementalFiles();
    }
    
    /**
     * @return all platform names
     */
    public List<String> getFilterPlatformNames() {
        SortedSet<String> platformNames = new TreeSet<String>();
        for (PlatformConfiguration platformConfiguration : getArrayDataService().getPlatformConfigurations()) {
            if (Status.LOADED.equals(platformConfiguration.getStatus())
                    && platformConfiguration.getPlatform().getVendor().getValue().equals(
                        getGenomicSource().getPlatformVendor())
                    && platformConfiguration.getPlatformType().getDataType().equals(
                        getGenomicSource().getDataTypeString())) {
                platformNames.add(platformConfiguration.getPlatform().getName());
            }
        }
        return new ArrayList<String>(platformNames);
    }
    
    /**
     * 
     * @return a list of data types based on the platform vendor.
     */
    public List<String> getDataTypes() {
        if (PlatformVendorEnum.AFFYMETRIX.getValue().equals(getGenomicSource().getPlatformVendor())) {
            return GenomicDataSourceDataTypeEnum.getStringValues();
        }
        List<String> dataTypes = GenomicDataSourceDataTypeEnum.getStringValues();
        dataTypes.remove("SNP");
        return dataTypes;
    }
    
    /**
     * Disabled status for the useSupplementalFiles checkbox.
     * @return whether to disable the checkbox.
     */
    public String getUseSupplementalFilesDisable() {
        return isAffyExpression() ? "false" : "true";
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the updater
     */
    public IGenomicDataSourceAjaxUpdater getUpdater() {
        return updater;
    }

    /**
     * @param updater the updater to set
     */
    public void setUpdater(IGenomicDataSourceAjaxUpdater updater) {
        this.updater = updater;
    }

    /**
     * @return the caArrayFacade
     */
    public CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    /**
     * @param caArrayFacade the caArrayFacade to set
     */
    public void setCaArrayFacade(CaArrayFacade caArrayFacade) {
        this.caArrayFacade = caArrayFacade;
    }

    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }
    
    /**
     * 
     * @return css style value.
     */
    public String getVarianceInputCssStyle() {
        return getGenomicSource().isUseHighVarianceCalculation() 
            ? "display: block;" : "display: none;";
    }
       
}
