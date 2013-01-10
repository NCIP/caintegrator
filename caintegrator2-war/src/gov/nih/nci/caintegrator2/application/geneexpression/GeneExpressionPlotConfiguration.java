/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
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
    private final List<String> subjectsNotFound = new ArrayList<String>();
    private List<PlotSampleGroup> plotSampleGroups = new ArrayList<PlotSampleGroup>();
    private GenomicValueResultsTypeEnum genomicValueResultsType = GenomicValueResultsTypeEnum.GENE_EXPRESSION;
    private boolean twoChannelType;
    
    /**
     * @return the twoChannelType
     */
    public boolean isTwoChannelType() {
        return twoChannelType;
    }

    /**
     * @param twoChannelType the twoChannelType to set
     */
    public void setTwoChannelType(boolean twoChannelType) {
        this.twoChannelType = twoChannelType;
    }

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

    /**
     * @return the subjectsNotFound
     */
    public List<String> getSubjectsNotFound() {
        return subjectsNotFound;
    }
    

}
