package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;

import java.util.Collection;

/**
 * 
 */
public class SelectedValueCriterion extends AbstractAnnotationCriterion {
    
    private static final long serialVersionUID = 1L;
    
    private Collection<AbstractPermissibleValue> valueCollection;

    /**
     * @return the valueCollection
     */
    public Collection<AbstractPermissibleValue> getValueCollection() {
        return valueCollection;
    }

    /**
     * @param valueCollection the valueCollection to set
     */
    public void setValueCollection(Collection<AbstractPermissibleValue> valueCollection) {
        this.valueCollection = valueCollection;
    }

}