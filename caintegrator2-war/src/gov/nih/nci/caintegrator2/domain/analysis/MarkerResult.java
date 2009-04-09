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
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
@SuppressWarnings("PMD.TooManyFields") // This is a wrapped object from genepattern.
public class MarkerResult extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    
    private Integer rank;
    private String feature;
    private String description;
    private Double score;
    private Double featureP;
    private Double featurePLow;
    private Double featurePHigh;
    private Double fdr;
    private Double qvalue;
    private Double bonferroni;
    private Double maxT;
    private Double fwer;
    private Double foldChange;
    private Double class0Mean;
    private Double class0Std;
    private Double class1Mean;
    private Double class1Std;
    private Double k;
    /**
     * @return the rank
     */
    public Integer getRank() {
        return rank;
    }
    /**
     * @param rank the rank to set
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }
    /**
     * @param feature the feature to set
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the score
     */
    public Double getScore() {
        return score;
    }
    /**
     * @param score the score to set
     */
    public void setScore(Double score) {
        this.score = score;
    }
    /**
     * @return the featureP
     */
    public Double getFeatureP() {
        return featureP;
    }
    /**
     * @param featureP the featureP to set
     */
    public void setFeatureP(Double featureP) {
        this.featureP = featureP;
    }
    /**
     * @return the featurePLow
     */
    public Double getFeaturePLow() {
        return featurePLow;
    }
    /**
     * @param featurePLow the featurePLow to set
     */
    public void setFeaturePLow(Double featurePLow) {
        this.featurePLow = featurePLow;
    }
    /**
     * @return the featurePHigh
     */
    public Double getFeaturePHigh() {
        return featurePHigh;
    }
    /**
     * @param featurePHigh the featurePHigh to set
     */
    public void setFeaturePHigh(Double featurePHigh) {
        this.featurePHigh = featurePHigh;
    }
    /**
     * @return the fdr
     */
    public Double getFdr() {
        return fdr;
    }
    /**
     * @param fdr the fdr to set
     */
    public void setFdr(Double fdr) {
        this.fdr = fdr;
    }
    /**
     * @return the qvalue
     */
    public Double getQvalue() {
        return qvalue;
    }
    /**
     * @param qvalue the qvalue to set
     */
    public void setQvalue(Double qvalue) {
        this.qvalue = qvalue;
    }
    /**
     * @return the bonferroni
     */
    public Double getBonferroni() {
        return bonferroni;
    }
    /**
     * @param bonferroni the bonferroni to set
     */
    public void setBonferroni(Double bonferroni) {
        this.bonferroni = bonferroni;
    }
    /**
     * @return the maxT
     */
    public Double getMaxT() {
        return maxT;
    }
    /**
     * @param maxT the maxT to set
     */
    public void setMaxT(Double maxT) {
        this.maxT = maxT;
    }
    /**
     * @return the fwer
     */
    public Double getFwer() {
        return fwer;
    }
    /**
     * @param fwer the fwer to set
     */
    public void setFwer(Double fwer) {
        this.fwer = fwer;
    }
    /**
     * @return the foldChange
     */
    public Double getFoldChange() {
        return foldChange;
    }
    /**
     * @param foldChange the foldChange to set
     */
    public void setFoldChange(Double foldChange) {
        this.foldChange = foldChange;
    }
    /**
     * @return the class0Mean
     */
    public Double getClass0Mean() {
        return class0Mean;
    }
    /**
     * @param class0Mean the class0Mean to set
     */
    public void setClass0Mean(Double class0Mean) {
        this.class0Mean = class0Mean;
    }
    /**
     * @return the class0Std
     */
    public Double getClass0Std() {
        return class0Std;
    }
    /**
     * @param class0Std the class0Std to set
     */
    public void setClass0Std(Double class0Std) {
        this.class0Std = class0Std;
    }
    /**
     * @return the class1Mean
     */
    public Double getClass1Mean() {
        return class1Mean;
    }
    /**
     * @param class1Mean the class1Mean to set
     */
    public void setClass1Mean(Double class1Mean) {
        this.class1Mean = class1Mean;
    }
    /**
     * @return the class1Std
     */
    public Double getClass1Std() {
        return class1Std;
    }
    /**
     * @param class1Std the class1Std to set
     */
    public void setClass1Std(Double class1Std) {
        this.class1Std = class1Std;
    }
    /**
     * @return the k
     */
    public Double getK() {
        return k;
    }
    /**
     * @param k the k to set
     */
    public void setK(Double k) {
        this.k = k;
    }
    
}
