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
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.imaging.Image;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Subject;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class StudyHelper {

    private AnnotationDefinition sampleAnnotationDefinition;
    private AnnotationDefinition imageSeriesAnnotationDefinition;
    private AnnotationDefinition subjectAnnotationDefinition;
    private PermissibleValue permval1;
    private PermissibleValue permval2;
    private Timepoint defaultTimepoint;
    private ArrayDataType arrayDataType = ArrayDataType.GENE_EXPRESSION;
    
    @SuppressWarnings({"PMD"}) // This is a long method for setting up test data
    public StudySubscription populateAndRetrieveStudy() {
        Study myStudy = new Study();
        myStudy.setShortTitleText("Test Study");
        
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(myStudy);
        
        myStudy.setStudyConfiguration(new StudyConfiguration());
        
        sampleAnnotationDefinition = new AnnotationDefinition();
        sampleAnnotationDefinition.getCommonDataElement().setLongName("SampleAnnotation");
        sampleAnnotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        
        imageSeriesAnnotationDefinition = new AnnotationDefinition();
        imageSeriesAnnotationDefinition.getCommonDataElement().setLongName("ImageSeriesAnnotation");
        imageSeriesAnnotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        
        subjectAnnotationDefinition = new AnnotationDefinition();
        subjectAnnotationDefinition.getCommonDataElement().setLongName("SubjectAnnotation");
        subjectAnnotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        
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
        myStudy.getSampleAnnotationCollection().addAll(sampleDefinitions);
        myStudy.getImageSeriesAnnotationCollection().addAll(imageSeriesDefinitions);
        myStudy.getSubjectAnnotationCollection().addAll(subjectDefinitions);
        
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
        
        permval1 = new PermissibleValue();
        permval2 = new PermissibleValue();
        
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        permissibleValueCollection.add(permval1);
        permissibleValueCollection.add(permval2);


        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition1_2 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition1_3 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition3 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition4 = new SampleAcquisition();
        SampleAcquisition sampleAcquisition5 = new SampleAcquisition();

        ImageSeries imageSeries1 = new ImageSeries();
        ImageSeries imageSeries2 = new ImageSeries();
        ImageSeries imageSeries3 = new ImageSeries();
        ImageSeries imageSeries4 = new ImageSeries();
        ImageSeries imageSeries5 = new ImageSeries();
        
        Image image1 = new Image();
        image1.setSeries(imageSeries1);
        imageSeries1.getImageCollection().add(image1);
        
        Image image2 = new Image();
        image2.setSeries(imageSeries2);
        imageSeries2.getImageCollection().add(image2);
        
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

        Collection<SubjectAnnotation> subjectAnnotationCollection1 = studySubjectAssignment1.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection2 = studySubjectAssignment2.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection3 = studySubjectAssignment3.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection4 = studySubjectAssignment4.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection5 = studySubjectAssignment5.getSubjectAnnotationCollection();
        
        subjectAnnotationCollection1.add(subjectAnnotation1);
        subjectAnnotationCollection2.add(subjectAnnotation2);
        subjectAnnotationCollection3.add(subjectAnnotation3);
        subjectAnnotationCollection4.add(subjectAnnotation4);
        subjectAnnotationCollection5.add(subjectAnnotation5);
        

        /**
         * Add the 5 SampleAcquisitions
         */
        // Add permissible values.
        sampleAnnotationDefinition.getPermissibleValueCollection().addAll(permissibleValueCollection);
        permval1.setValue("100.0");
        permval2.setValue("15.0");
        
        numval1.setAnnotationDefinition(sampleAnnotationDefinition);
        numval1.setNumericValue(10.0);
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
        sampleAcquisition2.getAnnotationCollection().add(numval2);
        sampleAcquisition2.setAssignment(studySubjectAssignment2);
//        sampleAcquisition2.setTimepoint(defaultTimepoint);
        
        numval3.setAnnotationDefinition(sampleAnnotationDefinition);
        numval3.setNumericValue(12.0);
        sampleAcquisition3.getAnnotationCollection().add(numval3);
        sampleAcquisition3.setAssignment(studySubjectAssignment3);
//        sampleAcquisition3.setTimepoint(defaultTimepoint);
        
        numval4.setAnnotationDefinition(sampleAnnotationDefinition);
        numval4.setNumericValue(13.0);
        sampleAcquisition4.getAnnotationCollection().add(numval4);
        sampleAcquisition4.setAssignment(studySubjectAssignment4);
//        sampleAcquisition4.setTimepoint(defaultTimepoint);
        
        numval5.setAnnotationDefinition(sampleAnnotationDefinition);
        numval5.setNumericValue(14.0);
        sampleAcquisition5.getAnnotationCollection().add(numval5);
        sampleAcquisition5.setAssignment(studySubjectAssignment5);
//        sampleAcquisition5.setTimepoint(defaultTimepoint);

        Collection<SampleAcquisition> saCollection1 = studySubjectAssignment1.getSampleAcquisitionCollection();
        Collection<SampleAcquisition> saCollection2 = studySubjectAssignment2.getSampleAcquisitionCollection();
        Collection<SampleAcquisition> saCollection3 = studySubjectAssignment3.getSampleAcquisitionCollection();
        Collection<SampleAcquisition> saCollection4 = studySubjectAssignment4.getSampleAcquisitionCollection();
        Collection<SampleAcquisition> saCollection5 = studySubjectAssignment5.getSampleAcquisitionCollection();
        
        saCollection1.add(sampleAcquisition1);
        createGenomicData(sampleAcquisition1, 1);
        saCollection1.add(sampleAcquisition1_2);
        createGenomicData(sampleAcquisition1_2, 12);
        saCollection1.add(sampleAcquisition1_3);
        createGenomicData(sampleAcquisition1_3, 13);
        saCollection2.add(sampleAcquisition2);
        createGenomicData(sampleAcquisition2, 2);
        saCollection3.add(sampleAcquisition3);
        createGenomicData(sampleAcquisition3, 3);
        saCollection4.add(sampleAcquisition4);
        createGenomicData(sampleAcquisition4, 4);
        saCollection5.add(sampleAcquisition5);
        createGenomicData(sampleAcquisition5, 5);

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
        imageSeries1.setImageStudy(isAcquisition1);
//        isAcquisition1.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition2 = new ImageSeriesAcquisition();
        isAcquisition2.setSeriesCollection(isCollection2);
        isAcquisition2.setAssignment(studySubjectAssignment2);
        imageSeries2.setImageStudy(isAcquisition2);
//        isAcquisition2.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition3 = new ImageSeriesAcquisition();
        isAcquisition3.setSeriesCollection(isCollection3);
        isAcquisition3.setAssignment(studySubjectAssignment3);
        imageSeries3.setImageStudy(isAcquisition3);
//        isAcquisition3.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition4 = new ImageSeriesAcquisition();
        isAcquisition4.setSeriesCollection(isCollection4);
        isAcquisition4.setAssignment(studySubjectAssignment4);
        imageSeries4.setImageStudy(isAcquisition4);
//        isAcquisition4.setTimepoint(defaultTimepoint);
        ImageSeriesAcquisition isAcquisition5 = new ImageSeriesAcquisition();
        isAcquisition5.setSeriesCollection(isCollection5);
        isAcquisition5.setAssignment(studySubjectAssignment5);
        imageSeries5.setImageStudy(isAcquisition5);
//        isAcquisition5.setTimepoint(defaultTimepoint);
        
        Collection<ImageSeriesAcquisition> isaCollection1 = studySubjectAssignment1.getImageStudyCollection();
        Collection<ImageSeriesAcquisition> isaCollection2 = studySubjectAssignment2.getImageStudyCollection();
        Collection<ImageSeriesAcquisition> isaCollection3 = studySubjectAssignment3.getImageStudyCollection();
        Collection<ImageSeriesAcquisition> isaCollection4 = studySubjectAssignment4.getImageStudyCollection();
        Collection<ImageSeriesAcquisition> isaCollection5 = studySubjectAssignment5.getImageStudyCollection();
        
        isaCollection1.add(isAcquisition1);
        isaCollection2.add(isAcquisition2);
        isaCollection3.add(isAcquisition3);
        isaCollection4.add(isAcquisition4);
        isaCollection5.add(isAcquisition5);
        
        Collection<StudySubjectAssignment> ssaCollection = myStudy.getAssignmentCollection();
        ssaCollection.add(studySubjectAssignment1);
        ssaCollection.add(studySubjectAssignment2);
        ssaCollection.add(studySubjectAssignment3);
        ssaCollection.add(studySubjectAssignment4);
        ssaCollection.add(studySubjectAssignment5);
        
        return studySubscription;
    }
    
    /**
     * @param sampleAcquisition1
     */
    private void createGenomicData(SampleAcquisition sampleAcquisition, int sampleNum) {
        Sample sample = new Sample();
        sample.setSampleAcquisition(sampleAcquisition);
        sampleAcquisition.setSample(sample);
        sample.setName("SAMPLE_" + sampleNum);
        ArrayData arrayData = createArrayData(sampleNum); 
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
    }

    private ArrayData createArrayData(int sampleNum) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(arrayDataType);
        Platform platform = new Platform();
        ReporterList reporterList = null;
        if (ArrayDataType.COPY_NUMBER.equals(arrayDataType)) {
            reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            createDnaReporter(reporterList); 
            addSegmentationData(arrayData);
        } else {
            reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            createGeneExpressionReporter(sampleNum, reporterList);
        }
        reporterList.getArrayDatas().add(arrayData);
        arrayData.getReporterLists().add(reporterList);
        return arrayData;
    }
    
    private void addSegmentationData(ArrayData arrayData) {
        SegmentData segmentData = new SegmentData();
        segmentData.setArrayData(arrayData);
        segmentData.setNumberOfMarkers(7813);
        segmentData.setSegmentValue(.135339f);
        ChromosomalLocation location = new ChromosomalLocation();
        location.setChromosome("3");
        location.setStartPosition(48603);
        location.setEndPosition(198541751);
        segmentData.setLocation(location);
        arrayData.getSegmentDatas().add(segmentData);
        
    }

    private void createGeneExpressionReporter(int sampleNum, ReporterList reporterList) {
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        Gene gene = new Gene();
        gene.setSymbol("GENE_" + sampleNum);
        reporter.getGenes().add(gene);
        reporter.setReporterList(reporterList);
        reporter.setIndex(0);
        reporter.setName("REPORTER_" + sampleNum);
        reporterList.getReporters().add(reporter);
    }
    
    private void createDnaReporter(ReporterList reporterList) {
        DnaAnalysisReporter reporter = new DnaAnalysisReporter();
        reporter.setAlleleA('C');
        reporter.setAlleleB('G');
        reporter.setChromosome("1");
        reporter.setDbSnpId("rs950122");
        reporter.setIndex(0);
        reporter.setName("SNP_A_1677174");
        reporter.setPosition(836727);
        reporter.setReporterList(reporterList);
        reporterList.getReporters().add(reporter);
    }

    public Study populateAndRetrieveStudyWithSourceConfigurations() {
        Study study = populateAndRetrieveStudy().getStudy();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setExperimentIdentifier("experimentIdentifier");
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        imageDataSourceConfiguration.setCollectionName("collection");
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        genomicDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getImageDataSources().add(imageDataSourceConfiguration);
        List<Sample> samples = new ArrayList<Sample>();
        Platform platform1 = new Platform();
        Platform platform2 = new Platform();
        Array array1 = new Array();
        Array array2 = new Array();
        array1.setPlatform(platform1);
        array2.setPlatform(platform2);
        Sample sample1 = new Sample();
        sample1.getArrayCollection().add(array1);
        sample1.getArrayCollection().add(array2);
        samples.add(sample1);
        Sample sample2 = new Sample();
        sample2.getArrayCollection().add(array1);
        samples.add(sample2);
        genomicDataSourceConfiguration.setSamples(samples);
        ImageSeriesAcquisition imageSeriesAcquisition = new ImageSeriesAcquisition();
        Set<ImageSeries> seriesCollection = new HashSet<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        seriesCollection.add(imageSeries);
        imageSeriesAcquisition.setSeriesCollection(seriesCollection);
        imageSeries.getImageCollection().add(new Image());
        imageSeries.getImageCollection().add(new Image());
        List<ImageSeriesAcquisition> imageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        imageSeriesAcquisitions.add(imageSeriesAcquisition);
        imageSeriesAcquisition.setAssignment(new StudySubjectAssignment());
        imageDataSourceConfiguration.getImageSeriesAcquisitions().addAll(imageSeriesAcquisitions);
        return study;
    }
    
    public CompoundCriterion createCompoundCriterion1() {    
        // Sample criterion (will return 3 Subjects: #1,#2,#3), but it will return #1 twice.
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(12.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        criterion.setAnnotationDefinition(getSampleAnnotationDefinition());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);
        
        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion1.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        // Clinical criterion (Will return 4 subjects: #2, #3, #4, #5)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(2.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        criterion2.setEntityType(EntityTypeEnum.SUBJECT);
        criterion2.setAnnotationDefinition(getSubjectAnnotationDefinition());

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
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
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        criterion.setAnnotationDefinition(getSampleAnnotationDefinition());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);
        
        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion1.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        // Clinical criterion (Will return 3 subjects: #1, #2, #3)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(4.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        criterion2.setEntityType(EntityTypeEnum.SUBJECT);
        criterion2.setAnnotationDefinition(getSubjectAnnotationDefinition());
        
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        compoundCriterion.getCriterionCollection().add(criterion1);
        compoundCriterion.getCriterionCollection().add(criterion2);
        return compoundCriterion;
    }
    
    public CompoundCriterion createCompoundCriterion3() {
        // Sample criterion (will return 3 Subjects: #1, #2, #3)
        SelectedValueCriterion criterion = new SelectedValueCriterion();
        criterion.setValueCollection(new HashSet<PermissibleValue>());
        criterion.getValueCollection().add(permval1);
        criterion.setAnnotationDefinition(sampleAnnotationDefinition);
        
        criterion.setAnnotationDefinition(getSampleAnnotationDefinition());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);
        
        
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        return compoundCriterion;
    }
    
    public CompoundCriterion createCompoundCriterion4() {
        // Clinical criterion (Will return 3 subjects: #1, #2, #3)
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(4.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        criterion.setEntityType(EntityTypeEnum.SUBJECT);
        criterion.setAnnotationDefinition(getSubjectAnnotationDefinition());
        
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        return compoundCriterion;
    }
    
    public CompoundCriterion createCompoundCriterion5() {
        // Image Series criterion (Will return 2 subject: #3, #4)
        StringComparisonCriterion criterion = new StringComparisonCriterion();
        criterion.setStringValue("string4");
        criterion.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        StringComparisonCriterion criterion2 = new StringComparisonCriterion();
        criterion2.setStringValue("string3");
        criterion2.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion2.setAnnotationDefinition(getImageSeriesAnnotationDefinition());
        
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.OR);
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        compoundCriterion.getCriterionCollection().add(criterion2);
        return compoundCriterion;
    }
    
    public Query createQuery(CompoundCriterion compoundCriterion, 
                             Collection<ResultColumn> columnCollection, 
                             StudySubscription subscription) {
        Query query = new Query();
        query.setName("Test Query");
        query.setCompoundCriterion(compoundCriterion);
        query.setSubscription(subscription);
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
    public PermissibleValue getPermval1() {
        return permval1;
    }
    public void setPermval1(PermissibleValue permval1) {
        this.permval1 = permval1;
    }
    public PermissibleValue getPermval2() {
        return permval2;
    }
    public void setPermval2(PermissibleValue permval2) {
        this.permval2 = permval2;
    }

    public Timepoint getDefaultTimepoint() {
        return defaultTimepoint;
    }

    public void setDefaultTimepoint(Timepoint defaultTimepoint) {
        this.defaultTimepoint = defaultTimepoint;
    }

    /**
     * @return the arrayDataType
     */
    public ArrayDataType getArrayDataType() {
        return arrayDataType;
    }

    /**
     * @param arrayDataType the arrayDataType to set
     */
    public void setArrayDataType(ArrayDataType arrayDataType) {
        this.arrayDataType = arrayDataType;
    }
    
    
}
