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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.kmplot.SubjectSurvivalData;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class SurvivalCalculatorTest {
    private static final String CENSORED_VALUE = "Alive";
    
    private StudySubjectAssignment subject1Uncensored;
    private StudySubjectAssignment subject2Censored;
    private SurvivalValueDefinition dateBasedSurvival;
    private SurvivalValueDefinition durationBasedSurvival;

    @Before
    public void setUp() throws Exception {
        
        subject1Uncensored = new StudySubjectAssignment();
        subject2Censored = new StudySubjectAssignment();
        
        AnnotationDefinition survivalStartDateAnnotation = new AnnotationDefinition();
        survivalStartDateAnnotation.setDisplayName("Survival Start Date");
        survivalStartDateAnnotation.setId(Long.valueOf(2));
        survivalStartDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition deathDateAnnotation = new AnnotationDefinition();
        deathDateAnnotation.setDisplayName("Death Date");
        deathDateAnnotation.setId(Long.valueOf(3));
        deathDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        AnnotationDefinition lastFollowupDateAnnotation = new AnnotationDefinition();
        lastFollowupDateAnnotation.setDisplayName("Last Followup Date");
        lastFollowupDateAnnotation.setId(Long.valueOf(4));
        lastFollowupDateAnnotation.setDataType(AnnotationTypeEnum.DATE);
        
        AnnotationDefinition survivalLengthAnnotation = new AnnotationDefinition();
        survivalLengthAnnotation.setDataType(AnnotationTypeEnum.NUMERIC);
        survivalLengthAnnotation.setId(Long.valueOf(5));
        survivalLengthAnnotation.setDisplayName("Survival Length");
        
        AnnotationDefinition survivalStatusAnnotation = new AnnotationDefinition();
        survivalStatusAnnotation.setDataType(AnnotationTypeEnum.STRING);
        survivalStatusAnnotation.setId(Long.valueOf(6));
        survivalLengthAnnotation.setDisplayName("Survival Status");
        durationBasedSurvival = new SurvivalValueDefinition();
        durationBasedSurvival.setSurvivalValueType(SurvivalValueTypeEnum.LENGTH_OF_TIME);
        durationBasedSurvival.setSurvivalLength(survivalLengthAnnotation);
        durationBasedSurvival.setSurvivalStatus(survivalStatusAnnotation);
        durationBasedSurvival.setValueForCensored(CENSORED_VALUE);
        
        dateBasedSurvival = new SurvivalValueDefinition();
        dateBasedSurvival.setSurvivalValueType(SurvivalValueTypeEnum.DATE);
        dateBasedSurvival.setSurvivalStartDate(survivalStartDateAnnotation);
        dateBasedSurvival.setDeathDate(deathDateAnnotation);
        dateBasedSurvival.setLastFollowupDate(lastFollowupDateAnnotation);
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        
        Calendar survivalStartDate = Calendar.getInstance();
        survivalStartDate.set(Calendar.YEAR, 2007);
        survivalStartDate.set(Calendar.MONTH, 1);
        survivalStartDate.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar survivalDeathDate = Calendar.getInstance();
        survivalDeathDate.set(Calendar.YEAR, 2008);
        survivalDeathDate.set(Calendar.MONTH, 3);
        survivalDeathDate.set(Calendar.DAY_OF_MONTH, 27);
        
        Calendar survivalLastFollowupDate = Calendar.getInstance();
        survivalLastFollowupDate.set(Calendar.YEAR, 2007);
        survivalLastFollowupDate.set(Calendar.MONTH, 6);
        survivalLastFollowupDate.set(Calendar.DAY_OF_MONTH, 15);
        
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(survivalStartDateAnnotation, survivalStartDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(deathDateAnnotation, survivalDeathDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createDateSurvivalValue(lastFollowupDateAnnotation, survivalLastFollowupDate));
        subject1Uncensored.getSubjectAnnotationCollection().add(createNumericSurvivalValue(survivalLengthAnnotation, 100.5));
        subject1Uncensored.getSubjectAnnotationCollection().add(createStringSurvivalValue(survivalStatusAnnotation, null));
        
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(survivalStartDateAnnotation, survivalStartDate));
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(lastFollowupDateAnnotation, survivalLastFollowupDate));
        subject2Censored.getSubjectAnnotationCollection().add(createNumericSurvivalValue(survivalLengthAnnotation, 55.4));
        subject2Censored.getSubjectAnnotationCollection().add(createStringSurvivalValue(survivalStatusAnnotation, CENSORED_VALUE));
    }

    private SubjectAnnotation createDateSurvivalValue(AnnotationDefinition annotationDefinition, Calendar date) {
        DateAnnotationValue subject1SurvivalValue = new DateAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (date != null) {
            subject1SurvivalValue.setDateValue(date.getTime());
        }
        subject1SurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subject1SurvivalValue);
        subject1SurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subject1SurvivalValue);
        return subjectAnnotation;
    }
    
    private SubjectAnnotation createNumericSurvivalValue(AnnotationDefinition annotationDefinition, Double survivalLength) {
        NumericAnnotationValue subjectSurvivalValue = new NumericAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (survivalLength != null) {
            subjectSurvivalValue.setNumericValue(survivalLength);
        }
        subjectSurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subjectSurvivalValue);
        subjectSurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subjectSurvivalValue);
        return subjectAnnotation;
    }
    
    private SubjectAnnotation createStringSurvivalValue(AnnotationDefinition annotationDefinition, String censorStatus) {
        StringAnnotationValue subjectSurvivalValue = new StringAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        if (censorStatus != null) {
            subjectSurvivalValue.setStringValue(censorStatus);
        }
        subjectSurvivalValue.setSubjectAnnotation(subjectAnnotation);
        subjectAnnotation.setAnnotationValue(subjectSurvivalValue);
        subjectSurvivalValue.setAnnotationDefinition(annotationDefinition);
        annotationDefinition.getAnnotationValueCollection().add(subjectSurvivalValue);
        return subjectAnnotation;
    }

    @Test
    public void testCreateSubjectSurvivalDataDateBased() {
        SubjectSurvivalData survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(451, survivalData1.getSurvivalLength());
        assertFalse(survivalData1.isCensor());
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.MONTHS);
        survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(15, survivalData1.getSurvivalLength());
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.WEEKS);
        survivalData1 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject1Uncensored);
        assertEquals(64, survivalData1.getSurvivalLength());
        
        dateBasedSurvival.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        SubjectSurvivalData survivalData2 = null;
        try {
            survivalData2 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject2Censored);
            fail();
        } catch (IllegalArgumentException e) {
            // expected this exception, because there's no value for death date.
        }
        
        subject2Censored.getSubjectAnnotationCollection().add(createDateSurvivalValue(dateBasedSurvival.getDeathDate(), null));
        survivalData2 = SurvivalCalculator.createSubjectSurvivalData(dateBasedSurvival, subject2Censored);
        assertEquals(164, survivalData2.getSurvivalLength());
        assertTrue(survivalData2.isCensor());
        
    }
    
    @Test
    public void testCreateSubjectSurvivalDataDurationBased() {
        SubjectSurvivalData survivalData1 = SurvivalCalculator.createSubjectSurvivalData(durationBasedSurvival, subject1Uncensored);
        assertEquals(101, survivalData1.getSurvivalLength());
        assertFalse(survivalData1.isCensor());
        
        SubjectSurvivalData survivalData2 = SurvivalCalculator.createSubjectSurvivalData(durationBasedSurvival, subject2Censored);
        assertEquals(55, survivalData2.getSurvivalLength());
        assertTrue(survivalData2.isCensor());
        
    }

}
