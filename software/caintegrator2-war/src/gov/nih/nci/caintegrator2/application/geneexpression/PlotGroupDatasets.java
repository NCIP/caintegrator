/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Wrapper class for the 4 different JFreeChart dataset types.
 */
public class PlotGroupDatasets {
    
    private final DefaultCategoryDataset meanDataset = new DefaultCategoryDataset();
    private final DefaultCategoryDataset medianDataset = new DefaultCategoryDataset();
    private final DefaultStatisticalCategoryDataset log2Dataset = new DefaultStatisticalCategoryDataset();
    private final DefaultBoxAndWhiskerCategoryDataset bwDataset = new DefaultBoxAndWhiskerCategoryDataset();

    /**
     * @return the meanDataset
     */
    public DefaultCategoryDataset getMeanDataset() {
        return meanDataset;
    }

    /**
     * @return the medianDataset
     */
    public DefaultCategoryDataset getMedianDataset() {
        return medianDataset;
    }

    /**
     * @return the log2Dataset
     */
    public DefaultStatisticalCategoryDataset getLog2Dataset() {
        return log2Dataset;
    }

    /**
     * @return the bwDataset
     */
    public DefaultBoxAndWhiskerCategoryDataset getBwDataset() {
        return bwDataset;
    }
    

}
