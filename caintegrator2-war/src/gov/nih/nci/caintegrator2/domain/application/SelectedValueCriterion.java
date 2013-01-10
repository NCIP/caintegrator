/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class SelectedValueCriterion extends AbstractAnnotationCriterion implements Cloneable {
    
    private static final long serialVersionUID = 1L;
    
    private Collection<PermissibleValue> valueCollection;

    /**
     * @return the valueCollection
     */
    public Collection<PermissibleValue> getValueCollection() {
        return valueCollection;
    }

    /**
     * @param valueCollection the valueCollection to set
     */
    public void setValueCollection(Collection<PermissibleValue> valueCollection) {
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
    
    private Collection<PermissibleValue> cloneValueCollection() {
        Collection<PermissibleValue> clone = new HashSet<PermissibleValue>();
        for (PermissibleValue abstractPermissibleValue : valueCollection) {
            clone.add(abstractPermissibleValue);
        }
        return clone;
    }

}
