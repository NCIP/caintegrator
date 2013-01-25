/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class DisplayableStudySummary {
    
    private final Study study;
    private final List<DisplayableGenomicSource> genomicDataSources = new ArrayList<DisplayableGenomicSource>();
    private final List<DisplayableImageSource> imageDataSources = new ArrayList<DisplayableImageSource>();

    /**
     * Returns T/F if study has imaging data associated with it.
     * @return T/F value.
     */
    public boolean isImagingStudy() {
        if (study.hasImageDataSources()) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns T/F if study has genomic data associated with it.
     * @return T/F value.
     */
    public boolean isGenomicStudy() {
        if (study.hasGenomicDataSources()) {
            return true;
        }
        return false;
    }

    /**
     * Constructor which wraps the study object to display summary information.
     * @param study - study to wrap.
     */
    public DisplayableStudySummary(Study study) {
        if (study == null) {
            throw new IllegalArgumentException("Study cannot be null");
        }
        this.study = study;
    }
    
    /**
     * Number of subjects in a study.
     * @return - number subjects.
     */
    public int getNumberSubjects() {
        return study.getAssignmentCollection().size();
    }
    
    /**
     * Number of subject annotation columns in a study.
     * @return number annotation columns.
     */
    public int getNumberSubjectAnnotationColumns() {
        return study.getSubjectAnnotationCollection().size();
    }
    
    /**
     * Number of image series annotation columns in a study.
     * @return number annotation columns.
     */
    public int getNumberImageSeriesAnnotationColumns() {
        return study.getImageSeriesAnnotationCollection().size();
    }
    
    /**
     * Gets the name of the study.
     * @return - study name.
     */
    public String getStudyName() {
        return study.getShortTitleText();
    }
    
    /**
     * Gets the name of the study.
     * @return - study name.
     */
    public String getStudyDescription() {
        return study.getLongTitleText();
    }
    
    /**
     * Returns if study is deployed or not.
     * @return T/F value.
     */
    public boolean isDeployed() {
        return study.isDeployed();
    }
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * @return the genomicDataSources
     */
    public List<DisplayableGenomicSource> getGenomicDataSources() {
        return genomicDataSources;
    }
    
    /**
     * @return the imageDataSources
     */
    public List<DisplayableImageSource> getImageDataSources() {
        return imageDataSources;
    }

}
