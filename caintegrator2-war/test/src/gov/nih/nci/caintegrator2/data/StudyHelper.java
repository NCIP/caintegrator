/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caArray
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caArray Software License (the License) is between NCI and You. You (or 
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
 * its rights in the caArray Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caArray Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator Software and any 
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
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.study.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.study.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissableValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericPermissableValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Subject;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class StudyHelper {

    private AnnotationDefinition sampleAnnotationDefinition;
    private AnnotationDefinition imageSeriesAnnotationDefinition;
    private AnnotationDefinition subjectAnnotationDefinition;
    private NumericPermissableValue permval1;
    private NumericPermissableValue permval2;
    private StudySubscription studySubscription;
    private Timepoint defaultTimepoint;
    
    @SuppressWarnings({"PMD"}) // This is a long method for setting up test data
    public Study populateAndRetrieveStudy() {
        Study myStudy = new Study();
        myStudy.setShortTitleText("Test Study");
        
        studySubscription = new StudySubscription();
        studySubscription.setStudy(myStudy);
        
        sampleAnnotationDefinition = new AnnotationDefinition();
        sampleAnnotationDefinition.setDisplayName("SampleAnnotation");
        
        imageSeriesAnnotationDefinition = new AnnotationDefinition();
        imageSeriesAnnotationDefinition.setDisplayName("ImageSeriesAnnotation");
        
        subjectAnnotationDefinition = new AnnotationDefinition();
        subjectAnnotationDefinition.setDisplayName("SubjectAnnotation");
        
        defaultTimepoint = new Timepoint();
        defaultTimepoint.setDescription("This is the default timepoint assuming none is given.");
        defaultTimepoint.setName("Default Timepoint");
        myStudy.setDefaultTimepoint(defaultTimepoint);
        
        Collection<AnnotationDefinition> sampleDefinitions = new HashSet<AnnotationDefinition>();
        sampleDefinitions.add(sampleAnnotationDefinition);
        
        Collection<AnnotationDefinition> imageSeriesDefinitions = new HashSet<AnnotationDefinition>();
        imageSeriesDefinitions.add(imageSeriesAnnotationDefinition);
        
        Collection<AnnotationDefinition> subjectDefinitions = new HashSet<AnnotationDefinition>();
        subjectDefinitions.add(subjectAnnotationDefinition);
        
        // Setup everything to use the same definition collection for simplicity.
        myStudy.setSampleAnnotationCollection(sampleDefinitions);
        myStudy.setImageSeriesAnnotationCollection(imageSeriesDefinitions);
        myStudy.setSubjectAnnotationCollection(subjectDefinitions);

        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Subject subject3 = new Subject();
        Subject subject4 = new Subject();
        Subject subject5 = new Subject();
        
        Collection<Subject> subjectCollection = new HashSet<Subject>();
        subjectCollection.add(subject1);
        subjectCollection.add(subject2);
        subjectCollection.add(subject3);
        subjectCollection.add(subject4);
        subjectCollection.add(subject5);
        
        SubjectList subjectList = new SubjectList();
        subjectList.setSubjectCollection(subjectCollection);
        subjectList.setSubscription(studySubscription);
        
        StudySubjectAssignment studySubjectAssignment1 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment2 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment3 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment4 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment5 = new StudySubjectAssignment();
        
        NumericAnnotationValue numval1 = new NumericAnnotationValue();
        NumericAnnotationValue numval1_2 = new NumericAnnotationValue();
        NumericAnnotationValue numval1_3 = new NumericAnnotationValue();
        NumericAnnotationValue numval2 = new NumericAnnotationValue();
        NumericAnnotationValue numval3 = new NumericAnnotationValue();
        NumericAnnotationValue numval4 = new NumericAnnotationValue();
        NumericAnnotationValue numval5 = new NumericAnnotationValue();
        
        StringAnnotationValue stringval1 = new StringAnnotationValue();
        StringAnnotationValue stringval2 = new StringAnnotationValue();
        StringAnnotationValue stringval3 = new StringAnnotationValue();
        StringAnnotationValue stringval4 = new StringAnnotationValue();
        StringAnnotationValue stringval5 = new StringAnnotationValue();
        
        NumericAnnotationValue subjnumval1 = new NumericAnnotationValue();
        NumericAnnotationValue subjnumval2 = new NumericAnnotationValue();
        NumericAnnotationValue subjnumval3 = new NumericAnnotationValue();
        NumericAnnotationValue subjnumval4 = new NumericAnnotationValue();
        NumericAnnotationValue subjnumval5 = new NumericAnnotationValue();
        
        permval1 = new NumericPermissableValue();
        permval2 = new NumericPermissableValue();
        
        Collection<AbstractPermissableValue> permissableValueCollection = new HashSet<AbstractPermissableValue>();
        permissableValueCollection.add(permval1);
        permissableValueCollection.add(permval2);


        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        sampleAcquisition1.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition1_2 = new SampleAcquisition();
        sampleAcquisition1_2.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition1_3 = new SampleAcquisition();
        sampleAcquisition1_3.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        sampleAcquisition2.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition3 = new SampleAcquisition();
        sampleAcquisition3.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
       
        SampleAcquisition sampleAcquisition4 = new SampleAcquisition();
        sampleAcquisition4.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
       
        SampleAcquisition sampleAcquisition5 = new SampleAcquisition();
        sampleAcquisition5.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries1 = new ImageSeries();
        imageSeries1.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries2 = new ImageSeries();
        imageSeries2.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries3 = new ImageSeries();
        imageSeries3.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries4 = new ImageSeries();
        imageSeries4.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries5 = new ImageSeries();
        imageSeries5.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        /**
         * Add the 5 SubjectAnnotations
         */
        SubjectAnnotation subjectAnnotation1 = new SubjectAnnotation();
        subjnumval1.setNumericValue(1.0);
        subjectAnnotation1.setAnnotationValue(subjnumval1);
        subjnumval1.setAnnotationDefinition(subjectAnnotationDefinition);
        subjectAnnotation1.setStudySubjectAssignment(studySubjectAssignment1);
        studySubjectAssignment1.setSubject(subject1);
        
        SubjectAnnotation subjectAnnotation2 = new SubjectAnnotation();
        subjnumval2.setNumericValue(2.0);
        subjectAnnotation2.setAnnotationValue(subjnumval2);
        subjnumval2.setAnnotationDefinition(subjectAnnotationDefinition);
        subjectAnnotation2.setStudySubjectAssignment(studySubjectAssignment2);
        studySubjectAssignment2.setSubject(subject2);
        
        SubjectAnnotation subjectAnnotation3 = new SubjectAnnotation();
        subjnumval3.setNumericValue(3.0);
        subjectAnnotation3.setAnnotationValue(subjnumval3);
        subjnumval3.setAnnotationDefinition(subjectAnnotationDefinition);
        subjectAnnotation3.setStudySubjectAssignment(studySubjectAssignment3);
        studySubjectAssignment3.setSubject(subject3);
        
        SubjectAnnotation subjectAnnotation4 = new SubjectAnnotation();
        subjnumval4.setNumericValue(4.0);
        subjectAnnotation4.setAnnotationValue(subjnumval4);
        subjnumval4.setAnnotationDefinition(subjectAnnotationDefinition);
        subjectAnnotation4.setStudySubjectAssignment(studySubjectAssignment4);
        studySubjectAssignment4.setSubject(subject4);
        
        SubjectAnnotation subjectAnnotation5 = new SubjectAnnotation();
        subjnumval5.setNumericValue(5.0);
        subjectAnnotation5.setAnnotationValue(subjnumval5);
        subjnumval5.setAnnotationDefinition(subjectAnnotationDefinition);
        subjectAnnotation5.setStudySubjectAssignment(studySubjectAssignment5);
        studySubjectAssignment5.setSubject(subject5);

        Collection<SubjectAnnotation> subjectAnnotationCollection1 = new HashSet<SubjectAnnotation>();
        Collection<SubjectAnnotation> subjectAnnotationCollection2 = new HashSet<SubjectAnnotation>();
        Collection<SubjectAnnotation> subjectAnnotationCollection3 = new HashSet<SubjectAnnotation>();
        Collection<SubjectAnnotation> subjectAnnotationCollection4 = new HashSet<SubjectAnnotation>();
        Collection<SubjectAnnotation> subjectAnnotationCollection5 = new HashSet<SubjectAnnotation>();
        
        subjectAnnotationCollection1.add(subjectAnnotation1);
        subjectAnnotationCollection2.add(subjectAnnotation2);
        subjectAnnotationCollection3.add(subjectAnnotation3);
        subjectAnnotationCollection4.add(subjectAnnotation4);
        subjectAnnotationCollection5.add(subjectAnnotation5);
        

        /**
         * Add the 5 SampleAcquisitions
         */
        // Add permissable values.
        sampleAnnotationDefinition.setPermissableValueCollection(permissableValueCollection);
        permval1.setLowValue(10.0);
        permval1.setHighValue(12.0);
        
        permval2.setLowValue(13.0);
        permval2.setHighValue(14.0);
        
        numval1.setAnnotationDefinition(sampleAnnotationDefinition);
        numval1.setNumericValue(10.0);
        numval1.setBoundedValue(permval1);
        sampleAcquisition1.getAnnotationCollection().add(numval1);
        sampleAcquisition1.setAssignment(studySubjectAssignment1);
//        sampleAcquisition1.setTimepoint(defaultTimepoint);
        
        // Add 2 more samples to Study Subject Assignment 1
        numval1_2.setAnnotationDefinition(sampleAnnotationDefinition);
        numval1_2.setNumericValue(100.0);
        sampleAcquisition1_2.getAnnotationCollection().add(numval1_2);
        sampleAcquisition1_2.setAssignment(studySubjectAssignment1);
//        sampleAcquisition1_2.setTimepoint(defaultTimepoint);
        
        numval1_3.setAnnotationDefinition(sampleAnnotationDefinition);
        numval1_3.setNumericValue(1.0);
        sampleAcquisition1_3.getAnnotationCollection().add(numval1_3);
        sampleAcquisition1_3.setAssignment(studySubjectAssignment1);
//        sampleAcquisition1_3.setTimepoint(defaultTimepoint);
        
        numval2.setAnnotationDefinition(sampleAnnotationDefinition);
        numval2.setNumericValue(11.0);
        numval2.setBoundedValue(permval1);
        sampleAcquisition2.getAnnotationCollection().add(numval2);
        sampleAcquisition2.setAssignment(studySubjectAssignment2);
//        sampleAcquisition2.setTimepoint(defaultTimepoint);
        
        numval3.setAnnotationDefinition(sampleAnnotationDefinition);
        numval3.setNumericValue(12.0);
        numval3.setBoundedValue(permval1);
        sampleAcquisition3.getAnnotationCollection().add(numval3);
        sampleAcquisition3.setAssignment(studySubjectAssignment3);
//        sampleAcquisition3.setTimepoint(defaultTimepoint);
        
        numval4.setAnnotationDefinition(sampleAnnotationDefinition);
        numval4.setNumericValue(13.0);
        numval4.setBoundedValue(permval2);
        sampleAcquisition4.getAnnotationCollection().add(numval4);
        sampleAcquisition4.setAssignment(studySubjectAssignment4);
//        sampleAcquisition4.setTimepoint(defaultTimepoint);
        
        numval5.setAnnotationDefinition(sampleAnnotationDefinition);
        numval5.setNumericValue(14.0);
        numval5.setBoundedValue(permval2);
        sampleAcquisition5.getAnnotationCollection().add(numval5);
        sampleAcquisition5.setAssignment(studySubjectAssignment5);
//        sampleAcquisition5.setTimepoint(defaultTimepoint);

        Collection<SampleAcquisition> saCollection1 = new HashSet<SampleAcquisition>();
        Collection<SampleAcquisition> saCollection2 = new HashSet<SampleAcquisition>();
        Collection<SampleAcquisition> saCollection3 = new HashSet<SampleAcquisition>();
        Collection<SampleAcquisition> saCollection4 = new HashSet<SampleAcquisition>();
        Collection<SampleAcquisition> saCollection5 = new HashSet<SampleAcquisition>();
        
        saCollection1.add(sampleAcquisition1);
        saCollection1.add(sampleAcquisition1_2);
        saCollection1.add(sampleAcquisition1_3);
        saCollection2.add(sampleAcquisition2);
        saCollection3.add(sampleAcquisition3);
        saCollection4.add(sampleAcquisition4);
        saCollection5.add(sampleAcquisition5);

        /**
         * Add the 5 Image Series
         */
        stringval1.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval1.setStringValue("string1");
        imageSeries1.getAnnotationCollection().add(stringval1);
        
        stringval2.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval2.setStringValue("string2");
        imageSeries2.getAnnotationCollection().add(stringval2);
        
        stringval3.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval3.setStringValue("string3");
        imageSeries3.getAnnotationCollection().add(stringval3);
        
        stringval4.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval4.setStringValue("string4");
        imageSeries4.getAnnotationCollection().add(stringval4);
        
        stringval5.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval5.setStringValue("string5");
        imageSeries5.getAnnotationCollection().add(stringval5);
        
        Collection<ImageSeries> isCollection1 = new HashSet<ImageSeries>();
        Collection<ImageSeries> isCollection2 = new HashSet<ImageSeries>();
        Collection<ImageSeries> isCollection3 = new HashSet<ImageSeries>();
        Collection<ImageSeries> isCollection4 = new HashSet<ImageSeries>();
        Collection<ImageSeries> isCollection5 = new HashSet<ImageSeries>();
        
        isCollection1.add(imageSeries1);
        isCollection2.add(imageSeries2);
        isCollection3.add(imageSeries3);
        isCollection4.add(imageSeries4);
        isCollection5.add(imageSeries5);
        
        ImageSeriesAcquisition isAcquisition1 = new ImageSeriesAcquisition();
        isAcquisition1.setSeriesCollection(isCollection1);
        isAcquisition1.setAssignment(studySubjectAssignment1);
//        isAcquisition1.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition2 = new ImageSeriesAcquisition();
        isAcquisition2.setSeriesCollection(isCollection2);
        isAcquisition2.setAssignment(studySubjectAssignment2);
//        isAcquisition2.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition3 = new ImageSeriesAcquisition();
        isAcquisition3.setSeriesCollection(isCollection3);
        isAcquisition3.setAssignment(studySubjectAssignment3);
//        isAcquisition3.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition4 = new ImageSeriesAcquisition();
        isAcquisition4.setSeriesCollection(isCollection4);
        isAcquisition4.setAssignment(studySubjectAssignment4);
//        isAcquisition4.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition5 = new ImageSeriesAcquisition();
        isAcquisition5.setSeriesCollection(isCollection5);
        isAcquisition5.setAssignment(studySubjectAssignment5);
//        isAcquisition5.setTimepoint(defaultTimepoint);
        
        Collection<ImageSeriesAcquisition> isaCollection1 = new HashSet<ImageSeriesAcquisition>();
        Collection<ImageSeriesAcquisition> isaCollection2 = new HashSet<ImageSeriesAcquisition>();
        Collection<ImageSeriesAcquisition> isaCollection3 = new HashSet<ImageSeriesAcquisition>();
        Collection<ImageSeriesAcquisition> isaCollection4 = new HashSet<ImageSeriesAcquisition>();
        Collection<ImageSeriesAcquisition> isaCollection5 = new HashSet<ImageSeriesAcquisition>();
        
        isaCollection1.add(isAcquisition1);
        isaCollection2.add(isAcquisition2);
        isaCollection3.add(isAcquisition3);
        isaCollection4.add(isAcquisition4);
        isaCollection5.add(isAcquisition5);
        

        
        // studySubjectAssignment1 is unique to the others in that he has 3 sample acquisitions in his collection
        studySubjectAssignment1.setSampleAcquisitionCollection(saCollection1);
        studySubjectAssignment1.setImageStudyCollection(isaCollection1);
        studySubjectAssignment1.setSubjectAnnotationCollection(subjectAnnotationCollection1);
        
        studySubjectAssignment2.setSampleAcquisitionCollection(saCollection2);
        studySubjectAssignment2.setImageStudyCollection(isaCollection2);
        studySubjectAssignment2.setSubjectAnnotationCollection(subjectAnnotationCollection2);
        
        studySubjectAssignment3.setSampleAcquisitionCollection(saCollection3);
        studySubjectAssignment3.setImageStudyCollection(isaCollection3);
        studySubjectAssignment3.setSubjectAnnotationCollection(subjectAnnotationCollection3);
        
        studySubjectAssignment4.setSampleAcquisitionCollection(saCollection4);
        studySubjectAssignment4.setImageStudyCollection(isaCollection4);
        studySubjectAssignment4.setSubjectAnnotationCollection(subjectAnnotationCollection4);
        
        studySubjectAssignment5.setSampleAcquisitionCollection(saCollection5);
        studySubjectAssignment5.setImageStudyCollection(isaCollection5);
        studySubjectAssignment5.setSubjectAnnotationCollection(subjectAnnotationCollection5);
        
        Collection<StudySubjectAssignment> ssaCollection = new HashSet<StudySubjectAssignment>();
        ssaCollection.add(studySubjectAssignment1);
        ssaCollection.add(studySubjectAssignment2);
        ssaCollection.add(studySubjectAssignment3);
        ssaCollection.add(studySubjectAssignment4);
        ssaCollection.add(studySubjectAssignment5);
        
        myStudy.setAssignmentCollection(ssaCollection);
        return myStudy;
    }
    
    public CompoundCriterion createCompoundCriterion1() {    
        // Sample criterion (will return 3 Subjects: #1,#2,#3), but it will return #1 twice.
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(12.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL.getValue());
        criterion.setAnnotationDefinition(getSampleAnnotationDefinition());
        criterion.setEntityType(EntityTypeEnum.SAMPLE.getValue());
        
        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES.getValue());
        criterion1.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        // Clinical criterion (Will return 4 subjects: #2, #3, #4, #5)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(2.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL.getValue());
        criterion2.setEntityType(EntityTypeEnum.SUBJECT.getValue());
        criterion2.setAnnotationDefinition(getSubjectAnnotationDefinition());

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND.getValue());
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        compoundCriterion.getCriterionCollection().add(criterion1);
        compoundCriterion.getCriterionCollection().add(criterion2);
        return compoundCriterion;
    }
    
    
    public CompoundCriterion createCompoundCriterion2() {
        // Sample criterion (will return 2 Subjects: #1, #5)
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(13.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER.getValue());
        criterion.setAnnotationDefinition(getSampleAnnotationDefinition());
        criterion.setEntityType(EntityTypeEnum.SAMPLE.getValue());
        
        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES.getValue());
        criterion1.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        // Clinical criterion (Will return 3 subjects: #1, #2, #3)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(4.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS.getValue());
        criterion2.setEntityType(EntityTypeEnum.SUBJECT.getValue());
        criterion2.setAnnotationDefinition(getSubjectAnnotationDefinition());
        
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND.getValue());
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        compoundCriterion.getCriterionCollection().add(criterion1);
        compoundCriterion.getCriterionCollection().add(criterion2);
        return compoundCriterion;
    }
    
    public Query createQuery(CompoundCriterion compoundCriterion, Collection<ResultColumn> columnCollection) {
        Query query = new Query();
        query.setName("Test Query");
        query.setCompoundCriterion(compoundCriterion);
        query.setSubscription(studySubscription);
        query.setColumnCollection(columnCollection);
        return query;
    }

    
    public AnnotationDefinition getSampleAnnotationDefinition() {
        return sampleAnnotationDefinition;
    }
    public void setSampleAnnotationDefinition(AnnotationDefinition sampleAnnotationDefinition) {
        this.sampleAnnotationDefinition = sampleAnnotationDefinition;
    }
    public AnnotationDefinition getImageSeriesAnnotationDefinition() {
        return imageSeriesAnnotationDefinition;
    }
    public void setImageSeriesAnnotationDefinition(AnnotationDefinition imageSeriesAnnotationDefinition) {
        this.imageSeriesAnnotationDefinition = imageSeriesAnnotationDefinition;
    }
    public AnnotationDefinition getSubjectAnnotationDefinition() {
        return subjectAnnotationDefinition;
    }
    public void setSubjectAnnotationDefinition(AnnotationDefinition subjectAnnotationDefinition) {
        this.subjectAnnotationDefinition = subjectAnnotationDefinition;
    }
    public NumericPermissableValue getPermval1() {
        return permval1;
    }
    public void setPermval1(NumericPermissableValue permval1) {
        this.permval1 = permval1;
    }
    public NumericPermissableValue getPermval2() {
        return permval2;
    }
    public void setPermval2(NumericPermissableValue permval2) {
        this.permval2 = permval2;
    }

    public Timepoint getDefaultTimepoint() {
        return defaultTimepoint;
    }

    public void setDefaultTimepoint(Timepoint defaultTimepoint) {
        this.defaultTimepoint = defaultTimepoint;
    }
    
    
}
