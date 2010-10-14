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
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Parameters used for creating a Gene Expression Based KaplanMeier plot. 
 */
public class KMGeneExpressionBasedParameters extends AbstractKMParameters {

    private Float underValue;
    private Float overValue;
    private String controlSampleSetName;
    private String geneSymbol;
    private final List<String> genesNotFound = new ArrayList<String>();
    private final List<String> genesFoundInStudy = new ArrayList<String>();
    private boolean multiplePlatformsInStudy = false;
    private String platformName;
    private ExpressionTypeEnum expressionType = ExpressionTypeEnum.FOLD_CHANGE;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        isValid &= validateGeneName();
        isValid &= validateUnderexpressed();
        isValid &= validateOverexpressed();
        isValid &= validateExpressionLevelValues();
        isValid &= validatePlatformSelected();
        isValid &= validateControlSetSelected();
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
    }
    
    private boolean validatePlatformSelected() {
        if (multiplePlatformsInStudy && StringUtils.isBlank(platformName)) {
            getErrorMessages().add("There are multiple platforms in this study, select a platform first.");
            return false;
        }
        return true;
    }
    
    private boolean validateControlSetSelected() {
        if (isFoldChangeType() && StringUtils.isBlank(controlSampleSetName)) {
            getErrorMessages().add("Must select a control sample set to create a Fold Change based plot.");
            return false;
        }
        return true;
    }

    private boolean validateOverexpressed() {
        String valueStringPrefix = isFoldChangeType() ? "Over Expressed Fold Change" : "Above Expression Level";
        if (overValue == null) {
            getErrorMessages().add(valueStringPrefix + " value cannot be null");
            return false;
        } 
        if (isFoldChangeType() && overValue <= 0) {
            getErrorMessages().add(valueStringPrefix + " value is not a valid number, must be > 0.");
            return false;
        } 
        
        return true;
    }

    private boolean validateExpressionLevelValues() {
        if (!isFoldChangeType() && overValue != null && underValue != null && overValue <= underValue) {
            getErrorMessages().add("Above Expression Level value must be greater than Below Expression Level value.");
            return false;
        }
        return true;
    }


    private boolean validateUnderexpressed() {
        String valueStringPrefix = isFoldChangeType() ? "Under Expressed Fold Change" : "Below Expression Level";
        if (underValue == null) {
            getErrorMessages().add(valueStringPrefix + " value cannot be null");
            return false;
        }
        if (isFoldChangeType() && underValue <= 0) {
            getErrorMessages().add(valueStringPrefix + " value is not a valid number, must be > 0.");
            return false;
        }
        return true;
    }

    private boolean validateGeneName() {
        if (StringUtils.isBlank(geneSymbol)) {
            getErrorMessages().add("Gene Symbol is blank, please enter a valid Gene.");
            return false;
        }
        return true;
    }


    /**
     * @return the underValue
     */
    public Float getUnderValue() {
        return underValue;
    }

    /**
     * @param underValue the underValue to set
     */
    public void setUnderValue(Float underValue) {
        this.underValue = underValue;
    }

    /**
     * @return the overValue
     */
    public Float getOverValue() {
        return overValue;
    }

    /**
     * @param overValue the overValue to set
     */
    public void setOverValue(Float overValue) {
        this.overValue = overValue;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        underValue = null;
        overValue = null;
        geneSymbol = null;
        
    }


    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }


    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }


    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }


    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }
    
    /**
     * @return the genesNotFound
     */
    public List<String> getGenesNotFound() {
        return genesNotFound;
    }

    /**
     * @return the genesFoundInStudy
     */
    public List<String> getGenesFoundInStudy() {
        return genesFoundInStudy;
    }


    /**
     * @return the multiplePlatformsInStudy
     */
    public boolean isMultiplePlatformsInStudy() {
        return multiplePlatformsInStudy;
    }


    /**
     * @param multiplePlatformsInStudy the multiplePlatformsInStudy to set
     */
    public void setMultiplePlatformsInStudy(boolean multiplePlatformsInStudy) {
        this.multiplePlatformsInStudy = multiplePlatformsInStudy;
    }
    
    /**
     * @return the expressionType
     */
    public ExpressionTypeEnum getExpressionType() {
        return expressionType;
    }

    /**
     * @param expressionType the expressionType to set
     */
    public void setExpressionType(ExpressionTypeEnum expressionType) {
        this.expressionType = expressionType;
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
     * Used in the JSP's to retrieve the displayable string version of the Enum values. 
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static List<String> getExpressionTypeValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (ExpressionTypeEnum type : ExpressionTypeEnum.values()) {
            list.add(type.getValue());
        }
        return list;
    }
    
    private boolean isFoldChangeType() {
        return ExpressionTypeEnum.FOLD_CHANGE.equals(expressionType);
    }
}
