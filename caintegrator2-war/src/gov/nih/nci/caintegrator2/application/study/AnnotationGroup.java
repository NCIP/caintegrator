/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Object that logically links a Study to a group of AnnotationFieldDescriptors.
 */
public class AnnotationGroup extends AbstractCaIntegrator2Object implements Comparable<AnnotationGroup> {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private Study study;
    private Set<AnnotationFieldDescriptor> annotationFieldDescriptors = new HashSet<AnnotationFieldDescriptor>();
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    /**
     * @return the annotationFieldDescriptors
     */
    public Set<AnnotationFieldDescriptor> getAnnotationFieldDescriptors() {
        return annotationFieldDescriptors;
    }
    /**
     * @param annotationFieldDescriptors the annotationFieldDescriptors to set
     */
    public void setAnnotationFieldDescriptors(Set<AnnotationFieldDescriptor> annotationFieldDescriptors) {
        this.annotationFieldDescriptors = annotationFieldDescriptors;
    }

    /**
     * Gets all visible AnnotationFieldDescriptors that have AnnotationDefinitions and belong to this group.
     * @return visible AFDs in the group.
     */
    public Set<AnnotationFieldDescriptor> getVisibleAnnotationFieldDescriptors() {
        Set<AnnotationFieldDescriptor> visibleSet = new HashSet<AnnotationFieldDescriptor>();
        for (AnnotationFieldDescriptor descriptor : getAnnotationFieldDescriptors()) {
            if (descriptor.isShownInBrowse() && descriptor.getDefinition() != null) {
                visibleSet.add(descriptor);
            }
        }
        return visibleSet;
    }

    /**
     * Gets all visible AnnotationFieldDescriptors that have AnnotationDefinitions
     *   and belong to this group order by name.
     * @return visible AFDs in the group, sorted by name.
     */
    public List<AnnotationFieldDescriptor> getSortedVisibleAnnotationFieldDescriptors() {
        List<AnnotationFieldDescriptor> annotationFieldDescriptorList = new ArrayList<AnnotationFieldDescriptor>();
        annotationFieldDescriptorList.addAll(getVisibleAnnotationFieldDescriptors());
        Collections.sort(annotationFieldDescriptorList);
        return annotationFieldDescriptorList;
    }

    /**
     *
     * @return boolean value of whether this group is deletable.
     */
    public boolean isDeletable() {
        if (annotationFieldDescriptors.isEmpty()
            || (!study.hasImageDataSources() && !study.hasClinicalDataSources())) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(AnnotationGroup o) {
        return getName().compareTo(o.getName());
    }

}
