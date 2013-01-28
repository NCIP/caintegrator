/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public abstract class AbstractReporter extends AbstractCaIntegrator2Object implements Comparable<AbstractReporter> {

    private static final long serialVersionUID = 1L;
    
    /**
     * Comparator that sorts reporters by index.
     */
    public static final Comparator<AbstractReporter> INDEX_COMPARATOR = new Comparator<AbstractReporter>() {
        public int compare(AbstractReporter reporter1, AbstractReporter reporter2) {
            return reporter1.getIndex() - reporter2.getIndex();
        }
    };
    
    private String name;
    private Integer index;
    private ReporterList reporterList;
    private Set<Gene> genes = new HashSet<Gene>();
    private Set<Sample> samplesHighVariance = new HashSet<Sample>();
    
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

    /**
     * {@inheritDoc}
     */
    public int compareTo(AbstractReporter reporter) {
        int geneComparison = getGeneComparison(reporter);
        if (geneComparison == 0) {
            return getName().compareTo(reporter.getName());
        } else {
            return geneComparison;
        }
    }

    private int getGeneComparison(AbstractReporter reporter) {
        if (getGenes().isEmpty()) {
            return reporter.getGenes().isEmpty() ? 0 : 1;
        } else if (reporter.getGenes().isEmpty()) {
            return -1;
        } else {
            return getGeneForComparison().compareTo(reporter.getGeneForComparison());
        }
    }

    private Gene getGeneForComparison() {
        if (getGenes().size() == 1) {
            return getGenes().iterator().next();
        } else {
            List<Gene> sortedGenes = new ArrayList<Gene>(getGenes().size());
            sortedGenes.addAll(getGenes());
            Collections.sort(sortedGenes);
            return sortedGenes.get(0);
        }
    }

    /**
     * @return the genes
     */
    public Set<Gene> getGenes() {
        return genes;
    }

    /**
     * @param genes the genes to set
     */
    @SuppressWarnings("unused") // Used by Hibernate
    private void setGenes(Set<Gene> genes) {
        this.genes = genes;
    }
    
    /**
     * @return the samplesHighVariance
     */
    public Set<Sample> getSamplesHighVariance() {
        return samplesHighVariance;
    }

    /**
     * @param samplesHighVariance the samplesHighVariance to set
     */
    @SuppressWarnings("unused") // Used by Hibernate
    private void setSamplesHighVariance(Set<Sample> samplesHighVariance) {
        this.samplesHighVariance = samplesHighVariance;
    }

    /**
     * Provides a comma-separated list of gene symbols intended for display.
     * 
     * @return symbols of all associated genes.
     */
    public String getGeneSymbols() {
        StringBuffer buffer = new StringBuffer();
        for (Gene gene : getGenes()) {
            if (buffer.length() > 0) {
                buffer.append(", ");
            }
            buffer.append(gene.getSymbol());
        }
        return buffer.toString();
    }
    
    /**
     * Provides a URL to CGAP for the gene symbols.
     * 
     * @return symbols of all associated genes.
     */
    public String getGeneSymbolsCgapUrl() {
        return ConfigurationParameter.CGAP_URL.getDefaultValue() + getGeneSymbols();
    }

    /**
     * Returns the index used for storage and retrieval of array data associated with this reporter.
     * 
     * @return the storage index.
     */
    public int getDataStorageIndex() {
        return getReporterList().getFirstDataStorageIndex() + getIndex();
    }

}
