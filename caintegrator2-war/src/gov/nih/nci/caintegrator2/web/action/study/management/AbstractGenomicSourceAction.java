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
