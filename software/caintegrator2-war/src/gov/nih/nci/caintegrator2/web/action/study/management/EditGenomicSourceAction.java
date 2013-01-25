/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.external.ConnectionException;
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
    private GenomicDataSourceConfiguration tempGenomicSource = new GenomicDataSourceConfiguration();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        tempGenomicSource = createNewGenomicSource();
        return checkEmptyPlatformTypes();
    }

    private String checkEmptyPlatformTypes() {
        for (PlatformConfiguration platformConfiguration : getArrayDataService().getPlatformConfigurations()) {
            if (Status.LOADED.equals(platformConfiguration.getStatus())
                    && (platformConfiguration.getPlatformType() == null
                            || platformConfiguration.getPlatformChannelType() == null)) {
                addActionError(getText("struts.messages.error.caarray.platforms.invalid"));
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
        getTempGenomicSource().setPlatformName("");
        getTempGenomicSource().setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        getTempGenomicSource().setDataType(PlatformDataTypeEnum.EXPRESSION);
        getTempGenomicSource().getServerProfile().setHostname(
                getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_HOST));
        getTempGenomicSource().getServerProfile().setPort(
            Integer.valueOf(getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_PORT)));
        getTempGenomicSource().getServerProfile().setUrl(
                getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_URL));
        return checkEmptyPlatformTypes();
    }
    
    /**
     * Refresh the page.
     * @return struts string.
     */
    public String refresh() {
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    public String save() {
        if (!validateSave()) {
            getWorkspaceService().clearSession();
            return INPUT;
        }
        
        getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
        if (getGenomicSource().getId() == null) {
            runAsynchronousGenomicDataRetrieval(getTempGenomicSource());
        } else { // Need to create a new source, delete the old one, and load the new one
            delete();
            runAsynchronousGenomicDataRetrieval(tempGenomicSource);
        }
        return SUCCESS;
    }

    private void runAsynchronousGenomicDataRetrieval(GenomicDataSourceConfiguration genomicSource) {
        getDisplayableWorkspace().setCurrentStudyConfiguration(getStudyConfiguration());
        genomicSource.setStatus(Status.PROCESSING);
        getStudyManagementService().addGenomicSourceToStudy(getStudyConfiguration(), genomicSource);
        setStudyLastModifiedByCurrentUser(genomicSource, LogEntry.getSystemLogLoad(genomicSource));
        updater.runJob(genomicSource.getId());
    }

    private boolean validateSave() {
        if (StringUtils.isEmpty(getTempGenomicSource().getPlatformName())) {
            addFieldError("tempGenomicSource.platformName", 
                    getText("struts.messages.error.caarray.platform.name.required"));
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
            caArrayFacade.validateGenomicSourceConnection(getTempGenomicSource());
        } catch (ConnectionException e) {
            addFieldError("tempGenomicSource.serverProfile.hostname",
                    getText("struts.messages.error.unable.to.connect"));
            return false;
        } catch (ExperimentNotFoundException e) {
            addFieldError("tempGenomicSource.experimentIdentifier", 
                    getText("struts.messages.error.caarray.experiment.not.found"));
            return false;
        }
        return true;
    }
    
    private boolean validateHighVarianceParameters() {
        if (getTempGenomicSource().isUseHighVarianceCalculation()
                && (getTempGenomicSource().getHighVarianceThreshold() == null
                        || getTempGenomicSource().getHighVarianceThreshold() <= 0)) {
            addFieldError("tempGenomicSource.highVarianceThreshold", 
                    getText("struts.messages.error.caarray.high.variance.threshold.invalid"));
            return false;
        }
        return true;
    }
    
    /**
     * @return all platform names
     */
    public List<String> getFilterPlatformNames() {
        SortedSet<String> platformNames = new TreeSet<String>();
        for (PlatformConfiguration platformConfiguration : getArrayDataService().getPlatformConfigurations()) {
            if (Status.LOADED.equals(platformConfiguration.getStatus())
                    && platformConfiguration.getPlatform().getVendor().equals(
                            getTempGenomicSource().getPlatformVendor())
                    && platformConfiguration.getPlatformType().getDataType().equals(
                            getTempGenomicSource().getDataTypeString())) {
                platformNames.add(platformConfiguration.getPlatform().getName());
            }
        }
        return new ArrayList<String>(platformNames);
    }
    
    /**
     * @return the list of array data loading types
     */
    public List<String> getLoadingTypes() {
        return ArrayDataLoadingTypeEnum.getLoadingTypes(
                getTempGenomicSource().getPlatformVendor(), getTempGenomicSource().getDataType());
    }
    
    /**
     * 
     * @return a list of data types based on the platform vendor.
     */
    public List<String> getDataTypes() {
        if (PlatformVendorEnum.AFFYMETRIX.getValue().equals(getTempGenomicSource().getPlatformVendor())) {
            return PlatformDataTypeEnum.getStringValues();
        }
        List<String> dataTypes = PlatformDataTypeEnum.getStringValues();
        dataTypes.remove("SNP");
        return dataTypes;
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
        return getTempGenomicSource().isUseHighVarianceCalculation() 
            ? "display: block;" : "display: none;";
    }

    /**
     * @return the tempGenomicSource
     */
    public GenomicDataSourceConfiguration getTempGenomicSource() {
        return tempGenomicSource;
    }

    /**
     * @param tempGenomicSource the tempGenomicSource to set
     */
    public void setTempGenomicSource(GenomicDataSourceConfiguration tempGenomicSource) {
        this.tempGenomicSource = tempGenomicSource;
    }
       
}
