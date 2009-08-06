package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GenomicDataResultRow extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private AbstractReporter reporter;
    private final List<GenomicDataResultValue> values = new ArrayList<GenomicDataResultValue>();

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
     * @return the values
     */
    public List<GenomicDataResultValue> getValues() {
        return values;
    }

}