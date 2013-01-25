/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
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
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
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
    private AnnotationDefinition subjectAnnotationDefinition2;
    private PermissibleValue permval1;
    private PermissibleValue permval2;
    private Timepoint defaultTimepoint;
    private ArrayDataType arrayDataType = ArrayDataType.GENE_EXPRESSION;
    private List<StudySubjectAssignment> studySubjects = new ArrayList<StudySubjectAssignment>();
    private Platform platform = new Platform();
    private DelimitedTextClinicalSourceConfiguration clinicalConf;
    private UserWorkspace userWorkspace;
    private String USER_NAME = "ncimanager";
    StudySubscription studySubscription;

    @SuppressWarnings("deprecation")
    public StudySubscription populateAndRetrieveStudy() {
        userWorkspace = new UserWorkspace();
        userWorkspace.setUsername(USER_NAME);
        Study myStudy = new Study();
        myStudy.setShortTitleText("Test Study");

        studySubscription = new StudySubscription();
        studySubscription.setStudy(myStudy);
        studySubscription.setUserWorkspace(userWorkspace);

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

        subjectAnnotationDefinition2 = new AnnotationDefinition();
        subjectAnnotationDefinition2.getCommonDataElement().setLongName("Gender");
        subjectAnnotationDefinition2.setDataType(AnnotationTypeEnum.STRING);

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
        subjectDefinitions.add(subjectAnnotationDefinition2);

        // Setup everything to use the same definition collection for simplicity.
        AnnotationGroup defaultAnnotationGroup = new AnnotationGroup();
        defaultAnnotationGroup.setStudy(myStudy);
        defaultAnnotationGroup.setName("default");
        defaultAnnotationGroup.getAnnotationFieldDescriptors().add(getSubjectAnnotationFieldDescriptor());
        defaultAnnotationGroup.getAnnotationFieldDescriptors().add(getImageSeriesAnnotationFieldDescriptor());
        defaultAnnotationGroup.getAnnotationFieldDescriptors().add(getSampleAnnotationFieldDescriptor());
        defaultAnnotationGroup.getAnnotationFieldDescriptors().add(getSubjectAnnotationFieldDescriptor2());
        myStudy.getAnnotationGroups().add(defaultAnnotationGroup);

        clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        myStudy.getStudyConfiguration().getClinicalConfigurationCollection().add(clinicalConf);
        AnnotationFile annotationFile = new AnnotationFileStub();
        clinicalConf.setAnnotationFile(annotationFile);

        addColumn(annotationFile, subjectAnnotationDefinition);
        addColumn(annotationFile, subjectAnnotationDefinition2);


        ImageDataSourceConfiguration imagingSourceConf = new ImageDataSourceConfiguration();
        myStudy.getStudyConfiguration().getImageDataSources().add(imagingSourceConf);
        ImageAnnotationConfiguration imageConf = new ImageAnnotationConfiguration();
        imagingSourceConf.setImageAnnotationConfiguration(imageConf);
        AnnotationFile imageAnnotationFile = new AnnotationFile();
        imageConf.setAnnotationFile(imageAnnotationFile);

        addColumn(imageAnnotationFile, imageSeriesAnnotationDefinition);

        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        Subject subject3 = new Subject();
        Subject subject4 = new Subject();
        Subject subject5 = new Subject();
        Subject subject6 = new Subject();

        Collection<Subject> subjectCollection = new HashSet<Subject>();
        subjectCollection.add(subject1);
        subjectCollection.add(subject2);
        subjectCollection.add(subject3);
        subjectCollection.add(subject4);
        subjectCollection.add(subject5);

        StudySubjectAssignment studySubjectAssignment1 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment2 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment3 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment4 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment5 = new StudySubjectAssignment();
        StudySubjectAssignment studySubjectAssignment6 = new StudySubjectAssignment();
        studySubjects.add(studySubjectAssignment1);
        studySubjects.add(studySubjectAssignment2);
        studySubjects.add(studySubjectAssignment3);
        studySubjects.add(studySubjectAssignment4);
        studySubjects.add(studySubjectAssignment5);
        studySubjects.add(studySubjectAssignment6);

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
        StringAnnotationValue stringval6 = new StringAnnotationValue();

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

        SubjectAnnotation subjectAnnotation6 = new SubjectAnnotation();
        stringval6.setStringValue("F");
        subjectAnnotation6.setAnnotationValue(stringval6);
        stringval6.setAnnotationDefinition(subjectAnnotationDefinition2);
        subjectAnnotation6.setStudySubjectAssignment(studySubjectAssignment6);
        studySubjectAssignment6.setSubject(subject6);

        Collection<SubjectAnnotation> subjectAnnotationCollection1 = studySubjectAssignment1.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection2 = studySubjectAssignment2.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection3 = studySubjectAssignment3.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection4 = studySubjectAssignment4.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection5 = studySubjectAssignment5.getSubjectAnnotationCollection();
        Collection<SubjectAnnotation> subjectAnnotationCollection6 = studySubjectAssignment6.getSubjectAnnotationCollection();

        subjectAnnotationCollection1.add(subjectAnnotation1);
        subjectAnnotationCollection2.add(subjectAnnotation2);
        subjectAnnotationCollection3.add(subjectAnnotation3);
        subjectAnnotationCollection4.add(subjectAnnotation4);
        subjectAnnotationCollection5.add(subjectAnnotation5);
        subjectAnnotationCollection6.add(subjectAnnotation6);


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

        Array array = new Array();
        array.setPlatform(platform);
        saCollection1.add(sampleAcquisition1);
        createGenomicData(sampleAcquisition1, 1, array, myStudy);
        saCollection1.add(sampleAcquisition1_2);
        createGenomicData(sampleAcquisition1_2, 12, array, myStudy);
        saCollection1.add(sampleAcquisition1_3);
        createGenomicData(sampleAcquisition1_3, 13, array, myStudy);
        saCollection2.add(sampleAcquisition2);
        createGenomicData(sampleAcquisition2, 2, array, myStudy);
        saCollection3.add(sampleAcquisition3);
        createGenomicData(sampleAcquisition3, 3, array, myStudy);
        saCollection4.add(sampleAcquisition4);
        createGenomicData(sampleAcquisition4, 4, array, myStudy);
        saCollection5.add(sampleAcquisition5);
        createGenomicData(sampleAcquisition5, 5, array, myStudy);

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
        ssaCollection.add(studySubjectAssignment6);

        return studySubscription;
    }

    private void addColumn(AnnotationFile annotationFile, AnnotationDefinition subjectDef) {
        FileColumn column = new FileColumn();
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setShownInBrowse(true);
        fieldDescriptor.setDefinition(subjectDef);
        column.setFieldDescriptor(fieldDescriptor);
        annotationFile.getColumns().add(column);
    }

    /**
     * @param sampleAcquisition1
     */
    private void createGenomicData(SampleAcquisition sampleAcquisition, int sampleNum, Array array, Study study) {
        Sample sample = new Sample();
        sample.getSampleAcquisitions().add(sampleAcquisition);
        sampleAcquisition.setSample(sample);
        sample.setName("SAMPLE_" + sampleNum);
        ArrayData arrayData = createArrayData(sampleNum);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        arrayData.setStudy(study);
    }

    private ArrayData createArrayData(int sampleNum) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(arrayDataType);
        ReporterList reporterList = null;
        if (ArrayDataType.COPY_NUMBER.equals(arrayDataType)) {
            reporterList = platform.addReporterList("reporterList" + sampleNum, ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            createDnaReporter(sampleNum, reporterList);
            addSegmentationData(arrayData, sampleNum);
        } else {
            reporterList = platform.addReporterList("reporterList" + sampleNum, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            createGeneExpressionReporter(sampleNum, reporterList);
        }
        reporterList.getArrayDatas().add(arrayData);
        arrayData.getReporterLists().add(reporterList);
        return arrayData;
    }

    private void addSegmentationData(ArrayData arrayData, int sampleNum) {
        SegmentData segmentData = new SegmentData();
        segmentData.setArrayData(arrayData);
        segmentData.setNumberOfMarkers(7813);
        segmentData.setSegmentValue(Float.valueOf(sampleNum) / 100f);
        segmentData.setCallsValue((sampleNum % 4) - 1);
        ChromosomalLocation location = new ChromosomalLocation();
        location.setChromosome(String.valueOf(sampleNum));
        location.setStartPosition(10000 * sampleNum);
        location.setEndPosition(20000 * sampleNum);
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

    private void createDnaReporter(int sampleNum, ReporterList reporterList) {
        DnaAnalysisReporter reporter = new DnaAnalysisReporter();
        Gene gene = new Gene();
        gene.setSymbol("GENE_" + sampleNum);
        reporter.getGenes().add(gene);
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
        imageSeries.setIdentifier("1.3.6.1.4.1.9328.50.45.239261393324265132190998071373586264552"); // for AIM annotations loading.
        seriesCollection.add(imageSeries);
        imageSeriesAcquisition.setSeriesCollection(seriesCollection);
        imageSeries.getImageCollection().add(new Image());
        imageSeries.getImageCollection().add(new Image());
        List<ImageSeriesAcquisition> imageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        imageSeriesAcquisitions.add(imageSeriesAcquisition);
        imageSeriesAcquisition.setAssignment(new StudySubjectAssignment());
        imageDataSourceConfiguration.getImageSeriesAcquisitions().addAll(imageSeriesAcquisitions);
        imageDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getClinicalConfigurationCollection().add(clinicalConf);
        clinicalConf.setStudyConfiguration(studyConfiguration);
        return study;
    }

    public CompoundCriterion createCompoundCriterion1() {
        // Sample criterion (will return 3 Subjects: #1,#2,#3), but it will return #1 twice.
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(12.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        criterion.setAnnotationFieldDescriptor(getSampleAnnotationFieldDescriptor());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);

        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion1.setAnnotationFieldDescriptor(getImageSeriesAnnotationFieldDescriptor());

        // Clinical criterion (Will return 4 subjects: #2, #3, #4, #5)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(2.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        criterion2.setEntityType(EntityTypeEnum.SUBJECT);
        criterion2.setAnnotationFieldDescriptor(getSubjectAnnotationFieldDescriptor());

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
        criterion.setAnnotationFieldDescriptor(getSampleAnnotationFieldDescriptor());
        criterion.setEntityType(EntityTypeEnum.SAMPLE);

        // Image Series criterion (Will return 1 subject: #3)
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string3");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion1.setAnnotationFieldDescriptor(getImageSeriesAnnotationFieldDescriptor());

        // Clinical criterion (Will return 3 subjects: #1, #2, #3)
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(4.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        criterion2.setEntityType(EntityTypeEnum.SUBJECT);
        criterion2.setAnnotationFieldDescriptor(getSubjectAnnotationFieldDescriptor());

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
        criterion.setAnnotationFieldDescriptor(getSampleAnnotationFieldDescriptor());
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
        criterion.setAnnotationFieldDescriptor(getSubjectAnnotationFieldDescriptor());

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
        criterion.setAnnotationFieldDescriptor(getImageSeriesAnnotationFieldDescriptor());

        StringComparisonCriterion criterion2 = new StringComparisonCriterion();
        criterion2.setStringValue("string3");
        criterion2.setEntityType(EntityTypeEnum.IMAGESERIES);
        criterion2.setAnnotationFieldDescriptor(getImageSeriesAnnotationFieldDescriptor());

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
    public AnnotationDefinition getImageSeriesAnnotationDefinition() {
        return imageSeriesAnnotationDefinition;
    }
    public AnnotationDefinition getSubjectAnnotationDefinition() {
        return subjectAnnotationDefinition;
    }
    public AnnotationDefinition getSubjectAnnotationDefinition2() {
        return subjectAnnotationDefinition2;
    }
    public AnnotationFieldDescriptor getSampleAnnotationFieldDescriptor() {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(getSampleAnnotationDefinition());
        descriptor.setAnnotationEntityType(EntityTypeEnum.SAMPLE);
        return descriptor;
    }

    public AnnotationFieldDescriptor getSubjectAnnotationFieldDescriptor() {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(getSubjectAnnotationDefinition());
        descriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        return descriptor;
    }

    public AnnotationFieldDescriptor getSubjectAnnotationFieldDescriptor2() {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(getSubjectAnnotationDefinition2());
        descriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        return descriptor;
    }

    public AnnotationFieldDescriptor getImageSeriesAnnotationFieldDescriptor() {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(getImageSeriesAnnotationDefinition());
        descriptor.setAnnotationEntityType(EntityTypeEnum.IMAGESERIES);
        return descriptor;
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

    /**
     * @return the studySubjects
     */
    public List<StudySubjectAssignment> getStudySubjects() {
        return studySubjects;
    }

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public StudySubscription getStudySubscription() {
        return studySubscription;
    }

}
