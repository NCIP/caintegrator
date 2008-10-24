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
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.application.query.GenomicAnnotationEnum;
import gov.nih.nci.caintegrator2.application.study.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Helper class to store and retrieve annotation definitions, and 
 * their type-based associated operators.
 */
public class AnnotationSelection {
    private Collection<AnnotationDefinition> annotationDefinitions = new HashSet<AnnotationDefinition>();
    private Collection<GenomicAnnotationEnum> genomicAnnotationDefinitions = new HashSet<GenomicAnnotationEnum>();
    private List<String> annotationSelections = new ArrayList<String>();
     private List<String> currentAnnotationOperatorSelections = new ArrayList<String>();
    private List<String> stringAnnotationDisplayOperatorList = new ArrayList<String>();
    private List<String> numericAnnotationDisplayOperatorList = new ArrayList<String>();
    // Maps string operator display to corresponding WildCardTypeEnum
    private Map<String, WildCardTypeEnum> stringOptionToEnumMap = new HashMap<String, WildCardTypeEnum>();
    // Maps numeric operator display to corresponding NumericComparisonOperatorEnum
    private Map<String, NumericComparisonOperatorEnum> numericOptionToEnumMap 
                    = new HashMap<String, NumericComparisonOperatorEnum>();

    /**
     * Default constructor. 
     */
    public AnnotationSelection() {
        initializeOperatorSets();
    }
    
    /**
     * @return List of annotation selections
     */
    public List<String> getAnnotationSelections() {
        return this.annotationSelections;
    }

    /**
     * @param annotationSelections the List to set
     */
    public void setAnnotationSelections(List<String> annotationSelections) {
        this.annotationSelections = annotationSelections;
    }
    
    /**
     * @return List of currently-selected annotation type-based operator selections
     */
    public List<String> getCurrentAnnotationOperatorSelections() {
        if (currentAnnotationOperatorSelections == null || currentAnnotationOperatorSelections.isEmpty()) {
            // Default to combined string and numeric operators
            currentAnnotationOperatorSelections = stringAnnotationDisplayOperatorList;
            currentAnnotationOperatorSelections.addAll(numericAnnotationDisplayOperatorList);
        }
        
        return this.currentAnnotationOperatorSelections;
    }
    
    /**
     * @param currentAnnotationOperatorSelections the List to set
     */
    public void setCurrentAnnotationOperatorSelections(List<String> currentAnnotationOperatorSelections) {
        this.currentAnnotationOperatorSelections = currentAnnotationOperatorSelections;
    }
    
    /**
     * @param dataType Sets and returns currentAnnotationOperatorSelections based on the type of data.
     * @return List the set of operators based on dataType
     */
    public List<String> getCurrentAnnotationOperatorSelections(String dataType) {
        if ("string".equals(dataType)) {
            this.currentAnnotationOperatorSelections = this.getStringAnnotationDisplayOperatorList();
        } else if ("numeric".equals(dataType)) {
            this.currentAnnotationOperatorSelections = this.getNumericAnnotationDisplayOperatorList();
        }
        
        return this.currentAnnotationOperatorSelections;
    }
    
    /**
     * @param dataType Sets currentAnnotationOperatorSelections based on the type of data.
     */
    public void setCurrentAnnotationOperatorSelections(String dataType) {
        if ("string".equals(dataType)) {
            this.currentAnnotationOperatorSelections = this.getStringAnnotationDisplayOperatorList();
        } else if ("numeric".equals(dataType)) {
            this.currentAnnotationOperatorSelections = this.getNumericAnnotationDisplayOperatorList();
        }
    }
    
    /**
     * @return the stringAnnotationDisplayOperatorList
     */
    public List<String> getStringAnnotationDisplayOperatorList() {
        return stringAnnotationDisplayOperatorList;
    }

    /**
     * @param stringAnnotationDisplayOperatorList the stringAnnotationDisplayOperatorList to set
     */
    public void setStringAnnotationDisplayOperatorList(List<String> stringAnnotationDisplayOperatorList) {
        this.stringAnnotationDisplayOperatorList = stringAnnotationDisplayOperatorList;
    }

    /**
     * @return the numericAnnotationDisplayOperatorList
     */
    public List<String> getNumericAnnotationDisplayOperatorList() {
        return numericAnnotationDisplayOperatorList;
    }

    /**
     * @param numericAnnotationDisplayOperatorList the numericAnnotationDisplayOperatorList to set
     */
    public void setNumericAnnotationDisplayOperatorList(List<String> numericAnnotationDisplayOperatorList) {
        this.numericAnnotationDisplayOperatorList = numericAnnotationDisplayOperatorList;
    }

    /**
     * @return the stringOptionToEnumMap
     */
    public Map<String, WildCardTypeEnum> getStringOptionToEnumMap() {
        return stringOptionToEnumMap;
    }

    /**
     * @param stringOptionToEnumMap the stringOptionToEnumMap to set
     */
    public void setStringOptionToEnumMap(Map<String, WildCardTypeEnum> stringOptionToEnumMap) {
        this.stringOptionToEnumMap = stringOptionToEnumMap;
    }

    /**
     * @return the numericOptionToEnumMap
     */
    public Map<String, NumericComparisonOperatorEnum> getNumericOptionToEnumMap() {
        return numericOptionToEnumMap;
    }

    /**
     * @param numericOptionToEnumMap the numericOptionToEnumMap to set
     */
    public void setNumericOptionToEnumMap(Map<String, NumericComparisonOperatorEnum> numericOptionToEnumMap) {
        this.numericOptionToEnumMap = numericOptionToEnumMap;
    }
    
    /**
     * @return the annotationDefinitions
     */
    public Collection<AnnotationDefinition> getAnnotationDefinitions() {
        return annotationDefinitions;
    }

    /**
     * @param annotationDefinitions the annotationDefinitions to set
     */
    public void setAnnotationDefinitions(Collection<AnnotationDefinition> annotationDefinitions) {
        this.annotationDefinitions = annotationDefinitions;
    }

    @SuppressWarnings({ "PMD.ExcessiveMethodLength" }) // Long initializer function
    private void initializeOperatorSets() {
        // Populate the string display operators to Enum definitions
        stringOptionToEnumMap = new HashMap<String, WildCardTypeEnum>();
        // From Will's email:
        // Implemented: equals, contains, does not contain, begins with, ends with 
        // Not implemented: does not equal, [matches (for regular expressions)]
        stringOptionToEnumMap.put("equals", WildCardTypeEnum.WILDCARD_OFF);
        stringOptionToEnumMap.put("begins with", WildCardTypeEnum.WILDCARD_AFTER_STRING);
        stringOptionToEnumMap.put("ends with", WildCardTypeEnum.WILDCARD_BEFORE_STRING);
        stringOptionToEnumMap.put("contains", WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING);
        
        // Populate the numeric display operators to Enum definitions
        numericOptionToEnumMap = new HashMap<String, NumericComparisonOperatorEnum>();
        // From Will's email:
        // Implemented: equals, not equals, less than, greater than, less than or equal to, greater than or equal to
        // Not implemented: [linear correlation greater than 0.8, linear correlation greater than 0.9, 
        //   spearman correlation greater than 0.8, spearman correlation greater than 0.9]
        numericOptionToEnumMap.put("=", NumericComparisonOperatorEnum.EQUAL);
        numericOptionToEnumMap.put("!=", NumericComparisonOperatorEnum.NOTEQUAL);
        numericOptionToEnumMap.put("<", NumericComparisonOperatorEnum.LESS);
        numericOptionToEnumMap.put(">", NumericComparisonOperatorEnum.GREATER);
        numericOptionToEnumMap.put("<=", NumericComparisonOperatorEnum.LESSOREQUAL);
        numericOptionToEnumMap.put(">=", NumericComparisonOperatorEnum.GREATEROREQUAL);
        
        // Populate the string display operators
        stringAnnotationDisplayOperatorList = new ArrayList<String>(10);
        stringAnnotationDisplayOperatorList.add("equals");
        stringAnnotationDisplayOperatorList.add("begins with");
        stringAnnotationDisplayOperatorList.add("ends with");
        stringAnnotationDisplayOperatorList.add("contains");
        ((ArrayList<String>) stringAnnotationDisplayOperatorList).trimToSize();
        
        // Populate the numeric display operators
        numericAnnotationDisplayOperatorList = new ArrayList<String>(10);
        numericAnnotationDisplayOperatorList.add("=");
        numericAnnotationDisplayOperatorList.add("!=");
        numericAnnotationDisplayOperatorList.add("<");
        numericAnnotationDisplayOperatorList.add(">");
        numericAnnotationDisplayOperatorList.add("<=");
        numericAnnotationDisplayOperatorList.add(">=");
        ((ArrayList<String>) numericAnnotationDisplayOperatorList).trimToSize();
        
    }

    /**
     * @return the genomicAnnotationDefinitions
     */
    public Collection<GenomicAnnotationEnum> getGenomicAnnotationDefinitions() {
        return genomicAnnotationDefinitions;
    }

    /**
     * @param genomicAnnotationDefinitions the genomicAnnotationDefinitions to set
     */
    public void setGenomicAnnotationDefinitions(Collection<GenomicAnnotationEnum> genomicAnnotationDefinitions) {
        this.genomicAnnotationDefinitions = genomicAnnotationDefinitions;
    }

}
