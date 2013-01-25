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
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.web.ajax.IGenomicDataSourceAjaxUpdater;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (getGenomicSource().getId() == null) {
            getGenomicSource().getServerProfile().setHostname(
                    getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_HOST));
            getGenomicSource().getServerProfile().setPort(
                Integer.valueOf(getConfigurationHelper().getString(ConfigurationParameter.CAARRAY_PORT)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
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
        setLastModifiedByCurrentUser();
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
        getStudyConfiguration().setStatus(Status.NOT_DEPLOYED);
        if (getGenomicSource().getId() == null) {
            runAsynchronousGenomicDataRetrieval(getGenomicSource());
        } else { // Need to create a new source, delete the old one, and load the new one
            GenomicDataSourceConfiguration newGenomicSource = createNewGenomicSource();
            delete();
            runAsynchronousGenomicDataRetrieval(newGenomicSource);
        }
        return SUCCESS;
    }

    private GenomicDataSourceConfiguration createNewGenomicSource() {
        GenomicDataSourceConfiguration configuration = new GenomicDataSourceConfiguration();
        ServerConnectionProfile newProfile = configuration.getServerProfile();
        ServerConnectionProfile oldProfile = getGenomicSource().getServerProfile();
        newProfile.setHostname(oldProfile.getHostname());
        newProfile.setPort(oldProfile.getPort());
        newProfile.setUsername(oldProfile.getUsername());
        newProfile.setPassword(oldProfile.getPassword());
        configuration.setExperimentIdentifier(getGenomicSource().getExperimentIdentifier());
        configuration.setPlatformVendor(getGenomicSource().getPlatformVendor());
        configuration.setPlatformName(getGenomicSource().getPlatformName());
        configuration.setDataType(getGenomicSource().getDataType());
        return configuration;
    }

    private void runAsynchronousGenomicDataRetrieval(GenomicDataSourceConfiguration genomicSource) {
        getDisplayableWorkspace().setCurrentStudyConfiguration(getStudyConfiguration());
        genomicSource.setStatus(Status.PROCESSING);
        setLastModifiedByCurrentUser();
        getStudyManagementService().addGenomicSourceToStudy(getStudyConfiguration(), genomicSource);
        updater.runJob(genomicSource.getId());
    }

    private boolean validateSave() {
        if (PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
                && StringUtils.isEmpty(getGenomicSource().getPlatformName())) {
            addFieldError("genomicSource.platformName", "Platform name is required for Agilent");
            return false;
        }
        try {
            caArrayFacade.validateGenomicSourceConnection(getGenomicSource());
        } catch (ConnectionException e) {
            addFieldError("genomicSource.serverProfile.hostname", "Unable to connect to server.");
            return false;
        } catch (ExperimentNotFoundException e) {
            addFieldError("genomicSource.experimentIdentifier", "Experiment identifier not found on server.");
            return false;
        }
        return true;
    }
    
    
    
    /**
     * @return all platform names
     */
    public List<String> getAgilentPlatformNames() {
        List<String> platformNames = new ArrayList<String>();
        platformNames.add("");
        for (Platform platform : getArrayDataService().getPlatforms()) {
            if (platform.getVendor().equals(PlatformVendorEnum.AGILENT)) {
                platformNames.add(platform.getName());
            }
        }
        return platformNames;
    }
    
    /**
     * Disabled status for the platform name selection.
     * @return whether to disable the platform names.
     */
    public String getPlatformNameDisable() {
        return PlatformVendorEnum.AGILENT.getValue().equals(getGenomicSource().getPlatformVendor())
            ? "false" : "true";
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
       
}
