/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * 
 */
public class ReporterList extends AbstractCaIntegrator2Object implements Comparable<ReporterList> {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private ReporterTypeEnum reporterType;
    private String genomeVersion;
    private List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
    private Platform platform;
    private Set<ArrayData> arrayDatas = new HashSet<ArrayData>();

    ReporterList() {
        super();
    }

    ReporterList(String name, ReporterTypeEnum reporterTypeEnum) {
        this();
        if (name == null) {
            throw new IllegalArgumentException("ReporterList name must not be null");
        }
        if (reporterTypeEnum == null) {
            throw new IllegalArgumentException("ReporterList type must not be null");
        }
        setName(name);
        setReporterType(reporterTypeEnum);
    }
    
    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }
    
    /**
     * @param platform the platform to set
     */
    void setPlatform(Platform platform) {
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
    private void setReporterType(ReporterTypeEnum reporterType) {
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
    private void setName(String name) {
        this.name = name;
    }

    Integer getFirstDataStorageIndex() {
        SortedSet<ReporterList> precedingReporterLists = 
            getPlatform().getReporterLists(getReporterType()).headSet(this);
        if (precedingReporterLists.isEmpty()) {
            return 0;
        } else {
            return precedingReporterLists.last().getFirstDataStorageIndex() 
            + precedingReporterLists.last().getReporters().size();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(ReporterList reporterList) {
        int nameComparison = getName().compareTo(reporterList.getName());
        if (nameComparison == 0) {
            return getReporterType().compareTo(reporterList.getReporterType());
        } else {
            return nameComparison;
        }
    }

    /**
     * Adds a new reporter to this <code>ReporterList</code>, creating the necessary associations.
     * 
     * @param reporter  the reporter to add
     */
    public void addReporter(AbstractReporter reporter) {
        reporter.setReporterList(this);
        reporters.add(reporter);
    }

    /**
     * @return the genomeVersion
     */
    public String getGenomeVersion() {
        return genomeVersion;
    }

    /**
     * @param genomeVersion the genomeVersion to set
     */
    public void setGenomeVersion(String genomeVersion) {
        this.genomeVersion = genomeVersion;
    }

}
