package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public abstract class AbstractReporter extends AbstractCaIntegrator2Object implements Comparable<AbstractReporter> {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private Integer index;
    private ReporterList reporterList;
    
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
     * @return the reporterList
     */
    public ReporterList getReporterList() {
        return reporterList;
    }
    
    /**
     * @param reporterList the reporterList to set
     */
    public void setReporterList(ReporterList reporterList) {
        this.reporterList = reporterList;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

}