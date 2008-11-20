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

import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.DatePermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringPermissibleValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

/**
 * 
 */
public class PermissibleValueUtilTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#getDisplayString(gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue)}.
     * @throws ParseException 
     */
    @Test
    public void testGetDisplayString() throws ParseException {
        StringPermissibleValue val1 = new StringPermissibleValue();
        val1.setStringValue("ABC");
        assertTrue("ABC".equalsIgnoreCase(PermissibleValueUtil.getDisplayString(val1)));
        
        NumericPermissibleValue val2 = new NumericPermissibleValue();
        val2.setNumericValue(Double.valueOf(123.0));
        assertTrue("123.0".equalsIgnoreCase(PermissibleValueUtil.getDisplayString(val2)));
        
        DatePermissibleValue val3 = new DatePermissibleValue();
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Date date = (Date)formatter.parse("10-11-2008");  
        val3.setDateValue(date);
        assertTrue(PermissibleValueUtil.getDisplayString(val3).equalsIgnoreCase("10-11-2008"));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.PermissibleValueUtil#getDisplayPermissibleValue(java.util.Collection)}.
     */
    @Test
    public void testGetDisplayPermissibleValue() {
        Collection <AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        NumericPermissibleValue val1 = new NumericPermissibleValue();
        val1.setNumericValue(Double.valueOf(123));
        permissibleValueCollection.add(val1);
        val1 = new NumericPermissibleValue();
        val1.setNumericValue(Double.valueOf(123));
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
        Collection <AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
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
        permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues2 = new ArrayList<String>();
        stringValues2.add("456.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues2);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test DatePermissibleValue
        permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add("11-10-2008");
        stringValues.add("05-28-2008");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues4 = new ArrayList<String>();
        stringValues4.add("11-10-2008");
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
        Set <AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
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
        permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add("123.0");
        annotationValues.add("456.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("456.0");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add("01-15-1995");
        annotationValues.add("12-01-2007");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("12-01-2007");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
    }

    @Test
    public void testUpdate() throws ParseException {
        Collection<AbstractPermissibleValue> permissibleValueCollection;
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
        newStringValues.add("01-15-1995");
        newStringValues.add("11-02-2008");
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
    }
    
    private Collection<AbstractPermissibleValue> createStringPermissible() throws ParseException {
        Collection<AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("ABC");
        stringValues.add("DEF");
        stringValues.add("GHI");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<AbstractPermissibleValue> createNumericPermissible() throws ParseException {
        Collection<AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.7");
        stringValues.add("789.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<AbstractPermissibleValue> createDatePermissible() throws ParseException {
        Collection<AbstractPermissibleValue> permissibleValueCollection = new HashSet<AbstractPermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("01-15-1995");
        stringValues.add("02-01-1987");
        stringValues.add("12-15-2007");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
}
