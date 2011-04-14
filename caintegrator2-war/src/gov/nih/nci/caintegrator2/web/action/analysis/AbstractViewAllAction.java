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
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public abstract class AbstractViewAllAction extends AbstractDeployedStudyAction {
    
    private static final long serialVersionUID = 1L;
    
    private static final String VIEW_ALL = "viewAll";
    private static final String CANCEL_ACTION = "cancel";
    private static final String HOME_PAGE = "homePage";
    /**
     * NONE constant.
     */
    protected static final String NONE = "-- None Available --";
    private String selectedAction;
    private QueryManagementService queryManagementService;
    private ArrayDataService arrayDataService;
    private AnalysisService analysisService;
    private Set<String> copyNumberPlatformsInStudy;
    private String copyNumberPlatformName;
    private List<Platform> platforms;
    private CopyNumberCriterionTypeEnum copyNumberType = CopyNumberCriterionTypeEnum.SEGMENT_VALUE;

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        copyNumberPlatformsInStudy = getQueryManagementService().retrieveCopyNumberPlatformsForStudy(getStudy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (VIEW_ALL.equals(selectedAction)) {
            Set<GenomeBuildVersionEnum> genomeBuild = new HashSet<GenomeBuildVersionEnum>();
            platforms = new ArrayList<Platform>();
            addPlatform(genomeBuild);
            if (platforms.isEmpty()) {
                addActionError(getText("struts.messages.error.igv.no.platform"));
            }
            if (genomeBuild.size() > 1) {
                addActionError(getText("struts.messages.error.igv.mix.genome"));
            }
        }
    }

    abstract void addPlatform(Set<GenomeBuildVersionEnum> genomeBuild);

    /**
     * {@inheritDoc}
     */
    public String execute() {
        if  (VIEW_ALL.equals(selectedAction)) {
            return viewAll();
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            return HOME_PAGE;
        }
        init();
        return SUCCESS;
    }

    abstract void init();

    abstract String viewAll();

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the copyNumberPlatformsInStudy
     */
    public Set<String> getCopyNumberPlatformsInStudy() {
        return copyNumberPlatformsInStudy;
    }

    /**
     * @param copyNumberPlatformsInStudy the copyNumberPlatformsInStudy to set
     */
    public void setCopyNumberPlatformsInStudy(Set<String> copyNumberPlatformsInStudy) {
        this.copyNumberPlatformsInStudy = copyNumberPlatformsInStudy;
    }

    /**
     * @return the copyNumberPlatformName
     */
    public String getCopyNumberPlatformName() {
        return copyNumberPlatformName;
    }

    /**
     * @param copyNumberPlatformName the copyNumberPlatformName to set
     */
    public void setCopyNumberPlatformName(String copyNumberPlatformName) {
        this.copyNumberPlatformName = copyNumberPlatformName;
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
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
     * @return the option label for copy number platform selector
     */
    public String getCopyNumberPlatformOption() {
        return copyNumberPlatformsInStudy.isEmpty() ? NONE : "";
    }

    /**
     * @return the platforms
     */
    protected List<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * @param platforms the platforms to set
     */
    protected void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    /**
     * @return the copyNumberType
     */
    public CopyNumberCriterionTypeEnum getCopyNumberType() {
        return copyNumberType;
    }

    /**
     * @param copyNumberType the copyNumberType to set
     */
    public void setCopyNumberType(CopyNumberCriterionTypeEnum copyNumberType) {
        this.copyNumberType = copyNumberType;
    }
    
    /**
     * @return the displayableCopyNumberType
     */
    public String getDisplayableCopyNumberType() {
        return copyNumberType.getValue();
    }
    
    /**
     * @param copyNumberTypeString the copyNumberTypeString to set
     */
    public void setDisplayableCopyNumberType(String copyNumberTypeString) {
        this.copyNumberType = CopyNumberCriterionTypeEnum.getByValue(copyNumberTypeString);
    }
}