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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * 
 */
public class PermissibleValueUtilTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#getDisplayString(gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue)}.
     * @throws ParseException 
     */
    @Test
    public void testGetDisplayString() throws ParseException {
        PermissibleValue val1 = new PermissibleValue();
        val1.setValue("ABC");
        assertTrue("ABC".equalsIgnoreCase(val1.toString()));
        
        PermissibleValue val2 = new PermissibleValue();
        val2.setValue("123");
        assertEquals("123",val2.toString());
        
        PermissibleValue val3 = new PermissibleValue();
        Date date = DateUtil.createDate("10-11-2008");  
        val3.setValue(DateUtil.toString(date));
        assertEquals("10/11/2008", val3.toString());
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#getDisplayPermissibleValue(java.util.Collection)}.
     */
    @Test
    public void testGetDisplayPermissibleValue() {
        Collection <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        PermissibleValue val1 = new PermissibleValue();
        val1.setValue("123");
        permissibleValueCollection.add(val1);
        val1 = new PermissibleValue();
        val1.setValue("123");
        permissibleValueCollection.add(val1);
        assertTrue(permissibleValueCollection.size() == 2);
        assertTrue(PermissibleValueUtil.getDisplayPermissibleValue(permissibleValueCollection).size() == 1);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#addNewValues(java.util.Collection, java.util.List)}.
     * @throws ParseException 
     */
    @Test
    public void testAddNewValues() throws ParseException {
        // Test StringPermissibleValue
        Collection <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("ABC");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 1);
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 1);
        stringValues.add("DEF");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues2 = new ArrayList<String>();
        stringValues2.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues2);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test DatePermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add(getDisplayDate("11-10-2008"));
        stringValues.add(getDisplayDate("05/28/2008"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues4 = new ArrayList<String>();
        stringValues4.add(getDisplayDate("11-10-2008"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues4);
        assertTrue(permissibleValueCollection.size() == 2);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#removeValues(java.util.Collection, java.util.List)}.
     * @throws ParseException 
     */
    @Test
    public void testRemoveValues() throws ParseException {
        // Test StringPermissibleValue
        Set <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> annotationValues = new ArrayList<String>();
        annotationValues.add("ABC");
        annotationValues.add("DEF");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("ABC");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add("123.1");
        annotationValues.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("456.1");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add(getDisplayDate("01-15-1995"));
        annotationValues.add(getDisplayDate("12-01-2007"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add(DateUtil.toString(DateUtil.createDate("12-01-2007")));
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
    }

    @Test
    public void testUpdate() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection;
        List<String> newStringValues;
        
        permissibleValueCollection = createStringPermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add("ABC");
        newStringValues.add("XYZ");
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = createNumericPermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add("123.0");
        newStringValues.add("89.95");
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test DatePermissibleValue
        permissibleValueCollection = createDatePermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add(getDisplayDate("01-15-1995"));
        newStringValues.add(getDisplayDate("11-02-2008"));
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
    }
    
    @Test
    public void testRetrieveValuesNotPermissible() throws ParseException {
        AnnotationDefinition annotationDefinition1 = new AnnotationDefinition();
        annotationDefinition1.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition1.getPermissibleValueCollection().addAll(createNumericPermissible());

        NumericAnnotationValue validValue = new NumericAnnotationValue();
        validValue.setNumericValue(123.0);
        validValue.setAnnotationDefinition(annotationDefinition1);
        annotationDefinition1.getAnnotationValueCollection().add(validValue);
        
        Set<String> invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        assertTrue(invalidValues.isEmpty());

        annotationDefinition1.setDataType(null);
        try {
            invalidValues = 
                PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        } catch (Exception e) {
            assertEquals("Data Type for the Annotation Definition is unknown.", e.getMessage());
        }

        annotationDefinition1.setDataType(AnnotationTypeEnum.NUMERIC);
        NumericAnnotationValue invalidValue = new NumericAnnotationValue();
        invalidValue.setNumericValue(1234.0);
        invalidValue.setAnnotationDefinition(annotationDefinition1);
        annotationDefinition1.getAnnotationValueCollection().add(invalidValue);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        assertTrue(invalidValues.size() == 1);
        assertTrue(invalidValues.iterator().next().equals("1234.0"));
        
        AnnotationDefinition annotationDefinition2 = new AnnotationDefinition();
        annotationDefinition2.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition2.getPermissibleValueCollection().addAll(createStringPermissible());
        
        StringAnnotationValue validValue2 = new StringAnnotationValue();
        validValue2.setStringValue("ABC");
        validValue2.setAnnotationDefinition(annotationDefinition2);
        annotationDefinition2.getAnnotationValueCollection().add(validValue2);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition2), annotationDefinition2);
        assertTrue(invalidValues.isEmpty());
        
        StringAnnotationValue invalidValue2 = new StringAnnotationValue();
        invalidValue2.setStringValue("ABCDEF");
        invalidValue2.setAnnotationDefinition(annotationDefinition2);
        annotationDefinition2.getAnnotationValueCollection().add(invalidValue2);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition2), annotationDefinition2);
        assertTrue(invalidValues.size() == 1);
        assertTrue(invalidValues.iterator().next().equals("ABCDEF"));

        AnnotationDefinition annotationDefinition3 = new AnnotationDefinition();
        annotationDefinition3.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition3.getPermissibleValueCollection().addAll(createDatePermissible());
        DateAnnotationValue validValue3 = new DateAnnotationValue();
        validValue3.setDateValue(new Date());
        validValue3.setAnnotationDefinition(annotationDefinition3);
        annotationDefinition3.getAnnotationValueCollection().add(validValue3);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition3), annotationDefinition3);
        assertTrue(invalidValues.size() == 1);
    }
    
    private Set<Object> retrieveValues(AnnotationDefinition annotationDefinition) {
        Set<Object> objectValues = new HashSet<Object>();
        for (AbstractAnnotationValue value : annotationDefinition.getAnnotationValueCollection()) {
            if (value instanceof StringAnnotationValue) {
                objectValues.add(((StringAnnotationValue) value).getStringValue());
            } else if (value instanceof NumericAnnotationValue) {
                objectValues.add(((NumericAnnotationValue) value).getNumericValue());
            } else if (value instanceof DateAnnotationValue) {
                objectValues.add(((DateAnnotationValue) value).getDateValue());
            } 
        }
        return objectValues;
    }

    private Collection<PermissibleValue> createStringPermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("ABC");
        stringValues.add("DEF");
        stringValues.add("GHI");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<PermissibleValue> createNumericPermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.7");
        stringValues.add("789.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<PermissibleValue> createDatePermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add(getDisplayDate("01-15-1995"));
        stringValues.add(getDisplayDate("02-01-1987"));
        stringValues.add(getDisplayDate("12-15-2007"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private String getDisplayDate(String date) throws ParseException {
        return DateUtil.toString(DateUtil.createDate(date));
    }
}
