/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class GeneExpressionPlotConfiguration {
    
    private final Set<String> geneNames = new HashSet<String>();
    private final List<String> genesNotFound = new ArrayList<String>();
    private List<PlotSampleGroup> plotSampleGroups = new ArrayList<PlotSampleGroup>();
    private GenomicValueResultsTypeEnum genomicValueResultsType = GenomicValueResultsTypeEnum.GENE_EXPRESSION;
    
    /**
     * @return the genomicValueResultsType
     */
    public GenomicValueResultsTypeEnum getGenomicValueResultsType() {
        return genomicValueResultsType;
    }

    /**
     * @param genomicValueResultsType the genomicValueResultsType to set
     */
    public void setGenomicValueResultsType(GenomicValueResultsTypeEnum genomicValueResultsType) {
        this.genomicValueResultsType = genomicValueResultsType;
    }

    /**
     * @return the plotSampleGroups
     */
    public List<PlotSampleGroup> getPlotSampleGroups() {
        return plotSampleGroups;
    }

    /**
     * @param plotSampleGroups the plotSampleGroups to set
     */
    public void setPlotSampleGroups(List<PlotSampleGroup> plotSampleGroups) {
        this.plotSampleGroups = plotSampleGroups;
    }

    /**
     * @return the geneName
     */
    public Set<String> getGeneNames() {
        return geneNames;
    }

    /**
     * @return the genesNotFound
     */
    public List<String> getGenesNotFound() {
        return genesNotFound;
    }
    

}
