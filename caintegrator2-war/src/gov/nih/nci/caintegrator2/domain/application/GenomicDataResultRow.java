package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;

import java.util.Collection;

/**
 * 
 */
public class GenomicDataResultRow extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private AbstractReporter reporter;
    private Collection<GenomicDataResultValue> valueCollection;

    /**
     * @return the reporter
     */
    public AbstractReporter getReporter() {
        return reporter;
    }
    
    /**
     * @param reporter the reporter to set
     */
    public void setReporter(AbstractReporter reporter) {
        this.reporter = reporter;
    }
    /**
     * @return the valueCollection
     */
    public Collection<GenomicDataResultValue> getValueCollection() {
        return valueCollection;
    }
    
    /**
     * @param valueCollection the valueCollection to set
     */
    public void setValueCollection(Collection<GenomicDataResultValue> valueCollection) {
        this.valueCollection = valueCollection;
    }

}