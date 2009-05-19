package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class ReporterList extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private ReporterTypeEnum reporterType;
    private List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
    private Platform platform;
    private Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
    
    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }
    
    /**
     * @param platform the platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    /**
     * @return the reporters
     */
    public List<AbstractReporter> getReporters() {
        return reporters;
    }

    /**
     * @param reporters the reporters to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setReporters(List<AbstractReporter> reporters) {
        this.reporters = reporters;
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
    }
    
    /**
     * Sets the index field for the contained reporters in natural sort order.
     */
    public void sortAndLoadReporterIndexes() {
        Collections.sort(reporters);
        for (int i = 0; i < reporters.size(); i++) {
            reporters.get(i).setIndex(i);
        }
    }

    /**
     * @return the arrayDatas
     */
    public Set<ArrayData> getArrayDatas() {
        return arrayDatas;
    }

    /**
     * @param arrayDatas the arrayDatas to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setArrayDatas(Set<ArrayData> arrayDatas) {
        this.arrayDatas = arrayDatas;
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

}