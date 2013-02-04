/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;

/**
 * This is a static utility class which is used by CaIntegrator2 objects for hibernate. 
 */
public final class HibernateUtil {
    
    private HibernateUtil() { }
    

    /**
     * Make sure all persistent collections are loaded.
     * 
     * @param definition the definition to ensure is loaded from Hibernate.
     */
    public static void loadCollections(AnnotationDefinition definition) {
        loadCollection(definition.getAnnotationValueCollection());
        loadCollection(definition.getPermissibleValueCollection());
    }

    /**
     * Make sure all persistent collections are loaded.
     * 
     * @param query the query to ensure is loaded from Hibernate.
     */
    public static void loadCollection(Query query) {
        if (query.getCompoundCriterion() != null) {
            loadCollection(query.getCompoundCriterion().getCriterionCollection());
            for (AbstractCriterion criterion : query.getCompoundCriterion().getCriterionCollection()) {
                if (criterion instanceof SelectedValueCriterion) {
                    loadCollection(((SelectedValueCriterion) criterion).getValueCollection());
                } else if (criterion instanceof FoldChangeCriterion) {
                    loadCollection(((FoldChangeCriterion) criterion).getCompareToSampleSet().getSamples());
                }
            }
        }
        loadCollection(query.getColumnCollection());
    }

    /**
     * Make sure all persistent collections are loaded.
     * @param studyConfiguration to load from hibernate.
     */
    public static void loadCollection(StudyConfiguration studyConfiguration) {
        loadCollection(studyConfiguration.getClinicalConfigurationCollection());
        loadGenomicSources(studyConfiguration.getGenomicDataSources());
        loadImagingSources(studyConfiguration.getImageDataSources());
        loadClinicalSources(studyConfiguration.getClinicalConfigurationCollection());
        HibernateUtil.loadCollection(studyConfiguration.getStudy().getAssignmentCollection());
        for (StudySubjectAssignment assignment : studyConfiguration.getStudy().getAssignmentCollection()) {
            loadCollection(assignment.getSampleAcquisitionCollection());
            loadCollection(assignment.getImageStudyCollection());
            loadCollection(assignment.getSubjectAnnotationCollection());
            for (SampleAcquisition sampleAcquisition : assignment.getSampleAcquisitionCollection()) {
                loadCollection(sampleAcquisition.getAnnotationCollection());
                loadSampleCollections(sampleAcquisition.getSample());
            }
        }
        loadCollection(studyConfiguration.getStudy());
    }
    
    /**
     * Loads study annotation groups.
     * @param study to load.
     */
    public static void loadCollection(Study study) {
        loadCollection(study.getAnnotationGroups());
        for (AnnotationGroup group : study.getAnnotationGroups()) {
            loadCollection(group.getAnnotationFieldDescriptors());
        }
    }

    /**
     * Make sure all persistent collections are loaded.
     * @param studySubscription to load from hibernate.
     */
    public static void loadCollection(StudySubscription studySubscription) {
        loadCollection(studySubscription.getQueryCollection());
        loadCollection(studySubscription.getListCollection());
        loadCollection(studySubscription.getAnalysisJobCollection());
        for (Query query : studySubscription.getQueryCollection()) {
            loadCollection(query);
        }
        loadCollection(studySubscription.getStudy().getStudyConfiguration());
    }

    /**
     * Make sure all persistent collections are loaded.
     * @param genomicSources List of GenomicDataSourceConfiguration to load from hibernate.
     */
    public static void loadGenomicSources(List<GenomicDataSourceConfiguration> genomicSources) {
        loadCollection(genomicSources);
        for (GenomicDataSourceConfiguration genomicSource : genomicSources) {
            loadGenomicSource(genomicSource);
        }
    }


    /**
     * Loads the genomic source.
     * @param genomicSource to load.
     */
    public static void loadGenomicSource(GenomicDataSourceConfiguration genomicSource) {
        if (genomicSource != null) {
            loadSamples(genomicSource.getSamples());
            loadSamples(genomicSource.getControlSamples());
            loadSamples(genomicSource.getMappedSamples());
            loadSampleSets(genomicSource.getControlSampleSetCollection());
            Hibernate.initialize(genomicSource.getServerProfile());
        }
    }
    
    /**
     * Make sure all persistent collections are loaded.
     * @param imageSources List of ImageDataSourceConfiguration to load from hibernate.
     */
    public static void loadImagingSources(List<ImageDataSourceConfiguration> imageSources) {
        loadCollection(imageSources);
        for (ImageDataSourceConfiguration imageSource : imageSources) {
            loadImagingSource(imageSource);
        }
    }
    
    private static void loadClinicalSources(List<AbstractClinicalSourceConfiguration> clinicalConfigurationCollection) {
        for (AbstractClinicalSourceConfiguration clinicalSource : clinicalConfigurationCollection) {
            HibernateUtil.loadCollection(clinicalSource.getAnnotationDescriptors());
        }
    }

    /**
     * Loads the imaging source.
     * @param imageSource to load.
     */
    public static void loadImagingSource(ImageDataSourceConfiguration imageSource) {
        Hibernate.initialize(imageSource);
        Hibernate.initialize(imageSource.getServerProfile());
        loadImageSeriesAcquisitions(imageSource.getImageSeriesAcquisitions());
        if (imageSource.getImageAnnotationConfiguration() != null) {
            Hibernate.initialize(imageSource.getImageAnnotationConfiguration());
            Hibernate.initialize(imageSource.getImageAnnotationConfiguration().getAnnotationFile());
            loadCollection(imageSource.getImageAnnotationConfiguration().getAnnotationFile().getColumns());
            for (FileColumn column 
                    : imageSource.getImageAnnotationConfiguration().getAnnotationFile().getColumns()) {
                Hibernate.initialize(column);
                if (column.getFieldDescriptor() != null) {
                    Hibernate.initialize(column.getFieldDescriptor());
                    Hibernate.initialize(column.getFieldDescriptor().getDefinition());
                }
            }
        }
    }
    

    private static void loadImageSeriesAcquisitions(List<ImageSeriesAcquisition> imageSeriesAcquisitions) {
        loadCollection(imageSeriesAcquisitions);
        for (ImageSeriesAcquisition imageSeriesAcquisition : imageSeriesAcquisitions) {
            Hibernate.initialize(imageSeriesAcquisition);
            for (ImageSeries imageSeries : imageSeriesAcquisition.getSeriesCollection()) {
                Hibernate.initialize(imageSeries);
                loadCollection(imageSeries.getImageCollection());
            }
        }
        
    }

    /**
     * Loads the sample set collection, as well as the subcollections.
     * @param sampleSets to load.
     */
    
    public static void loadSampleSets(Collection<SampleSet> sampleSets) {
        for (SampleSet sampleSet : sampleSets) {
            loadSamples(sampleSet.getSamples());
        }
    }
    
    /**
     * Loads the samples collection, as well as the subcollections.
     * @param samples to load.
     */
    public static void loadSamples(Collection<Sample> samples) {
        loadCollection(samples);
        for (Sample sample : samples) {
            loadSampleCollections(sample);
        }
    }
    
    /**
     * Loads the subcollections for the Sample object.
     * @param sample to load.
     */
    public static void loadSampleCollections(Sample sample) {
        loadCollection(sample.getArrayCollection());
        loadCollection(sample.getArrayDataCollection());
        for (ArrayData arrayData : sample.getArrayDataCollection()) {
            loadCollection(arrayData.getReporterLists());
        }
    }

    /**
     * Ensure that a persistent collection is loaded.
     * 
     * @param collection the collection to load.
     */
    public static void loadCollection(Collection<? extends Object> collection) {
        Hibernate.initialize(collection);
    }
    
}
