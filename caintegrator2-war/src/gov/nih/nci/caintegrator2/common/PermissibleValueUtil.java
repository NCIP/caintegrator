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
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DatePermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringPermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a static utility class used by the UI to update the permissibleValue collection. 
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity" }) // Checking for type and null.
public final class PermissibleValueUtil {

    private PermissibleValueUtil() {
        
    }
    
    /**
     * @param abstractPermissibleValues abstractPermissibleValues
     * @return set of permissible values
     */
    public static Set<String> getDisplayPermissibleValue(
            Collection<AbstractPermissibleValue> abstractPermissibleValues) {
        Set<String> results = new HashSet<String>();
        for (AbstractPermissibleValue abstractPermissibleValue : abstractPermissibleValues) {
            String displayString = abstractPermissibleValue.toString();
            //TODO Need to decide how to display null value, for now we just skip it
            if (displayString != null) {
                results.add(displayString);
            }
        }
        return results;
    }


    /**
     * Update the permissibleValue collection.
     * 
     * @param type the type of the abstractPermissibleValue
     * @param abstractPermissibleValues the PermissibleValue collection
     * @param newList the new list of DisplayValues
     * @throws ParseException Exception for parsing the Date string
     */
    public static void update(String type,
            Collection<AbstractPermissibleValue> abstractPermissibleValues,
            List<String> newList) throws ParseException {
        
        checkObsolete(abstractPermissibleValues, newList);
        addNewValue(type, abstractPermissibleValues, newList);
    }

    /**
     * @param abstractPermissibleValues
     * @param newList
     */
    private static void checkObsolete(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            List<String> newList) {
        List<AbstractPermissibleValue> removeList = new ArrayList<AbstractPermissibleValue>();
        for (AbstractPermissibleValue abstractPermissibleValue : abstractPermissibleValues) {
            if (newList == null || !newList.contains(abstractPermissibleValue.toString())) {
                removeList.add(abstractPermissibleValue);
            }
        }
        abstractPermissibleValues.removeAll(removeList);
    }

    /**
     * Add new values to the permissibleValue collection.
     * 
     * @param type the type of the abstractPermissibleValue
     * @param abstractPermissibleValues the PermissibleValue collection
     * @param addList the list of DisplayString to add
     * @throws ParseException Exception for parsing the Date string
     */
    public static void addNewValue(String type, Collection<AbstractPermissibleValue> abstractPermissibleValues,
            List<String> addList) throws ParseException {
        if (addList == null) {
            return;
        }
        for (String displayString : addList) {
            addNewValue(type, abstractPermissibleValues, displayString);
        }
    }

    /**
     * Remove a list of permissibleValues from the collection.
     * 
     * @param abstractPermissibleValues the PermissibleValue collection
     * @param removePermissibleDisplayValues the list PermissibleDisplayValue to be removed
     */
    public static void removeValue(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            List<String> removePermissibleDisplayValues) {
        if (removePermissibleDisplayValues == null) {
            return;
        }
        for (String displayString : removePermissibleDisplayValues) {
            removeValue(abstractPermissibleValues, displayString);
        }
    }
    
    private static void removeValue(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String removePermissibleDisplayValue) {
        AbstractPermissibleValue abstractPermissibleValue = getObject(abstractPermissibleValues,
                removePermissibleDisplayValue);
        if (abstractPermissibleValue == null) {
            return;
        }
        abstractPermissibleValues.remove(abstractPermissibleValue);
    }

    /**
     * Return the object with the same display string.
     * 
     * @param abstractPermissibleValues
     * @param removePermissibleDisplayValue
     * @return
     */
    private static AbstractPermissibleValue getObject(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) {
        for (AbstractPermissibleValue abstractPermissibleValue : abstractPermissibleValues) {
            if (displayString.equals(abstractPermissibleValue.toString())) {
                return abstractPermissibleValue;
            }
        }
        return null;
    }

    private static void addNewValue(String type, Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) throws ParseException {
        if (containsDisplayString(abstractPermissibleValues, displayString)) {
            return;
        }
        if (type.equals(AnnotationTypeEnum.STRING.getValue())) {
            addStringValue(abstractPermissibleValues, displayString);
        }
        if (type.equals(AnnotationTypeEnum.NUMERIC.getValue())) {
            addNumericValue(abstractPermissibleValues, displayString);
        }
        if (type.equals(AnnotationTypeEnum.DATE.getValue())) {
            addDateValue(abstractPermissibleValues, displayString);
        }
    }
    
    private static void addStringValue(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) {
        StringPermissibleValue newPermissibleValue = new StringPermissibleValue();
        newPermissibleValue.setStringValue(displayString);
        abstractPermissibleValues.add(newPermissibleValue);
    }
    
    private static void addNumericValue(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) {
        NumericPermissibleValue newPermissibleValue = new NumericPermissibleValue();
        newPermissibleValue.setNumericValue(new Double(displayString));
        abstractPermissibleValues.add(newPermissibleValue);
    }
    
    private static void addDateValue(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) throws ParseException {
        DatePermissibleValue newPermissibleValue = new DatePermissibleValue();
        newPermissibleValue.setDateValue(DateUtil.createDate(displayString));
        abstractPermissibleValues.add(newPermissibleValue);
    }
    
    private static boolean containsDisplayString(Collection<AbstractPermissibleValue> abstractPermissibleValues,
            String displayString) {

        for (AbstractPermissibleValue abstractPermissibleValue : abstractPermissibleValues) {
            if (displayString.equals(abstractPermissibleValue.toString())) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Retrieves all current Values for an AnnotationDefinition that don't belong to a PermissibleValue.
     * @param study is the Study the values belong to.
     * @param entityType the entity type of the Annotation definition.
     * @param annotationDefinition the annotation to validate values are permissible.
     * @param dao used to query on.
     * @return a set of string values that are not permissible for this study.
     */
    @SuppressWarnings("unchecked")
    public static Set<String> retrieveAnnotationValuesNotPermissible(Study study, 
                                                           EntityTypeEnum entityType,
                                                           AnnotationDefinition annotationDefinition, 
                                                           CaIntegrator2Dao dao) {
        AnnotationTypeEnum annotationType = AnnotationTypeEnum.getByValue(annotationDefinition.getType());
        if (annotationType == null) {
            throw new IllegalArgumentException("Data Type for the Annotation Definition is unknown.");
        }

        return retrieveValuesNotInPermissibleValues(
                    retrieveUniquePermissibleValues(annotationDefinition), 
                    dao.retrieveUniqueValuesForStudyAnnotation(study, annotationDefinition, entityType, 
                            annotationType.getClassType()));

    }
    
    /**
     * Retrieves the values in the file column which are not permissible.  This is the case when user wishes
     * to check against permissible values before the file has been loaded.
     * @param annotationDefinition to retrieve permissible values from.
     * @param fileColumn to retrieve the given values to check against the permissible values.
     * @return a set of string values that are not permissible for this study.
     * @throws ValidationException if the file column is invalid.
     */
    @SuppressWarnings("unchecked")
    public static Set<String> retrieveFileColumnValuesNotPermissible(AnnotationDefinition annotationDefinition, 
            FileColumn fileColumn) throws ValidationException {
        AnnotationTypeEnum annotationType = AnnotationTypeEnum.getByValue(annotationDefinition.getType());
        if (annotationType == null) {
            throw new IllegalArgumentException("Data Type for the Annotation Definition is unknown.");
        }
        return retrieveValuesNotInPermissibleValues(retrieveUniquePermissibleValues(annotationDefinition),
                fileColumn.getUniqueDataValues(annotationType.getClassType()));
    }
    
    private static <T> Set<String> retrieveValuesNotInPermissibleValues(Set<T> permissibleValues, 
            Collection<T> uniqueValues) {
        Set<String> valuesNotInPemissibleList = new HashSet<String>();
        if (uniqueValues != null && !uniqueValues.isEmpty()) {
            for (T uniqueValue : uniqueValues) {
                if (!permissibleValues.contains(uniqueValue)) {
                    valuesNotInPemissibleList.add(uniqueValue.toString());
                }
            }
        }
        return valuesNotInPemissibleList;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Set <T> retrieveUniquePermissibleValues(AnnotationDefinition annotationDefinition) {
        Set<T> permissibleValues = new HashSet<T>();
        for (AbstractPermissibleValue permissibleValue : annotationDefinition.getPermissibleValueCollection()) {
            permissibleValues.add((T) retrievePermissibleValueAsPrimitiveType(permissibleValue));
        }
        return permissibleValues;
    }

    private static Object retrievePermissibleValueAsPrimitiveType(AbstractPermissibleValue permissibleValue) {
        if (permissibleValue instanceof StringPermissibleValue) {
            StringPermissibleValue stringPermissibleValue = (StringPermissibleValue) permissibleValue;
            return stringPermissibleValue.getStringValue();    
        } else if (permissibleValue instanceof NumericPermissibleValue) {
            NumericPermissibleValue numericPermissibleValue = (NumericPermissibleValue) permissibleValue;
            return numericPermissibleValue.getNumericValue();  
        } else if (permissibleValue instanceof DatePermissibleValue) {
            DatePermissibleValue datePermissibleValue = (DatePermissibleValue) permissibleValue;
            return datePermissibleValue.getDateValue();
        } 
        throw new IllegalArgumentException("Unknown permissibleValue Type");
    }

}
