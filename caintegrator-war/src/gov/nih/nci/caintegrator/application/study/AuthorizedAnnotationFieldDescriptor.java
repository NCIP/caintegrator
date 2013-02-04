/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * An AuthorizedAnnotationFieldDescriptor is an AnnotationFieldDescriptor that has been
 * authorized for inclusion in an <code>AuthorizedStudyElementsGroup</code>.
 */
public class AuthorizedAnnotationFieldDescriptor extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
    private AuthorizedStudyElementsGroup authorizedStudyElementsGroup;
    
    /**
     * @return the annotationFieldDescriptor
     */
    public AnnotationFieldDescriptor getAnnotationFieldDescriptor() {
        return annotationFieldDescriptor;
    }
    /**
     * @param annotationFieldDescriptor the annotationFieldDescriptor to set
     */
    public void setAnnotationFieldDescriptor(AnnotationFieldDescriptor annotationFieldDescriptor) {
        this.annotationFieldDescriptor = annotationFieldDescriptor;
    }
    /**
     * @param authorizedStudyElementsGroup the authorizedStudyElementsGroup to set
     */
    public void setAuthorizedStudyElementsGroup(AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        this.authorizedStudyElementsGroup = authorizedStudyElementsGroup;
    }
    /**
     * @return the authorizedStudyElementsGroup
     */
    public AuthorizedStudyElementsGroup getAuthorizedStudyElementsGroup() {
        return authorizedStudyElementsGroup;
    }

}
