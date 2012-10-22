/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import org.apache.commons.lang3.StringUtils;

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
