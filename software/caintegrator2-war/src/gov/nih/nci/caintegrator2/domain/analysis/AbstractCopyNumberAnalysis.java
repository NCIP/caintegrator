/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class representing the different copy number analysis for a study.
 */
public abstract class AbstractCopyNumberAnalysis extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    
    private Set<Sample> samplesUsedForCalculation = new HashSet<Sample>();
    private String name;
    private StudySubscription studySubscription;
    private Date creationDate;

    /**
     * @return the samplesUsedForCalculation
     */
    public Set<Sample> getSamplesUsedForCalculation() {
        return samplesUsedForCalculation;
    }

    /**
     * @param samplesUsedForCalculation the samplesUsedForCalculation to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setSamplesUsedForCalculation(Set<Sample> samplesUsedForCalculation) {
        this.samplesUsedForCalculation = samplesUsedForCalculation;
    }

    /**
     * @return the studySubscription
     */
    public StudySubscription getStudySubscription() {
        return studySubscription;
    }

    /**
     * @param studySubscription the studySubscription to set
     */
    public void setStudySubscription(StudySubscription studySubscription) {
        this.studySubscription = studySubscription;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
}
