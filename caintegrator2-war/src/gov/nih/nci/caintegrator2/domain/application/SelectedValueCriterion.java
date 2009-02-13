package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class SelectedValueCriterion extends AbstractAnnotationCriterion implements Cloneable {
    
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected SelectedValueCriterion clone() throws CloneNotSupportedException {
        SelectedValueCriterion clone = (SelectedValueCriterion) super.clone();
        clone.setValueCollection(cloneValueCollection());
        return clone;
    }
    
    private Collection<AbstractPermissibleValue> cloneValueCollection() {
        Collection<AbstractPermissibleValue> clone = new HashSet<AbstractPermissibleValue>();
        for (AbstractPermissibleValue abstractPermissibleValue : valueCollection) {
            clone.add(abstractPermissibleValue);
        }
        return clone;
    }

}