/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * Contains the information about a particular annotation field prior to association to an 
 * <code>AnnotationDefinition</code>.
 */
public class AnnotationFieldDescriptor extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private String name;
    private AnnotationFieldType type;
    private AnnotationDefinition definition;
    private boolean shownInBrowse = true;

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
     * @return the type
     */
    public AnnotationFieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AnnotationFieldType type) {
        this.type = type;
    }

    /**
     * @return the definition
     */
    public AnnotationDefinition getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(AnnotationDefinition definition) {
        this.definition = definition;
    }

    /**
     * @return the shownInBrowse
     */
    public boolean isShownInBrowse() {
        return shownInBrowse;
    }

    /**
     * @param shownInBrowse the shownInBrowse to set
     */
    public void setShownInBrowse(boolean shownInBrowse) {
        this.shownInBrowse = shownInBrowse;
    }

}
