/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A logical grouping of GeneExpressionPlot objects, based on the PlotCalculationTypeEnum.
 */
public class GeneExpressionPlotGroup {
    
    private final Map<PlotCalculationTypeEnum, GeneExpressionPlot> geneExpressionPlots =
        new HashMap<PlotCalculationTypeEnum, GeneExpressionPlot>();
    private final List<LegendItemWrapper> legendItems = new ArrayList<LegendItemWrapper>();
    private final Map<String, Integer> groupNameToNumberSubjectsMap = new HashMap<String, Integer>();
    private final List<String> genesNotFound = new ArrayList<String>();
    private final List<String> subjectsNotFound = new ArrayList<String>();
    private boolean twoChannelType = false;

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
     * @return the geneExpressionPlots
     */
    public Map<PlotCalculationTypeEnum, GeneExpressionPlot> getGeneExpressionPlots() {
        return geneExpressionPlots;
    }
    
    /**
     * Retrieves the plot for the given type.
     * @param calculationType - calculation type to use to retrieve plot.
     * @return associated plot.
     */
    public GeneExpressionPlot getPlot(PlotCalculationTypeEnum calculationType) {
        return geneExpressionPlots.get(calculationType);
    }

    /**
     * @return the legendItems
     */
    public List<LegendItemWrapper> getLegendItems() {
        return legendItems;
    }

    /**
     * @return the groupNameToNumberSubjectsMap
     */
    public Map<String, Integer> getGroupNameToNumberSubjectsMap() {
        return groupNameToNumberSubjectsMap;
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
