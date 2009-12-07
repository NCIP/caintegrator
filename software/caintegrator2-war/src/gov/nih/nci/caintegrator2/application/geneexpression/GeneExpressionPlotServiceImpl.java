/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

/**
 * Implementation of the GeneExpressionPlotService interface.
 */
public class GeneExpressionPlotServiceImpl implements GeneExpressionPlotService {
    private static final String DOMAIN_AXIS_LABEL = "Groups";
    private static final double LOWER_MARGIN = .02;
    private static final double CATEGORY_MARGIN = .20; 
    private static final double UPPER_MARGIN = .02;
    private static final double ITEM_MARGIN = .01;
    private static final int WIDTH_MULTIPLIER = 33;
    private static final int MINIMUM_WIDTH = 300;
    private LegendItemCollection legendItems;
    

    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup generatePlots(GeneExpressionPlotConfiguration configuration) {
        GeneExpressionPlotGroup plotGroup = new GeneExpressionPlotGroup();
        PlotGroupDatasets dataSets = PlotGroupDatasetsFactory.createDatasets(configuration);
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEAN, 
                 createMeanTypePlot(dataSets.getMeanDataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEDIAN, 
                createMedianTypePlot(dataSets.getMedianDataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.LOG2_INTENSITY, 
                createLog2TypePlot(dataSets.getLog2Dataset(), configuration.getGenomicValueResultsType()));
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.BOX_WHISKER_LOG2_INTENSITY, 
                createBoxWhiskerTypePlot(dataSets.getBwDataset(), configuration.getGenomicValueResultsType()));
        addLegendItemsToPlot(plotGroup);
        addPlotGroupSubjectCounts(configuration, plotGroup);
        plotGroup.getGenesNotFound().addAll(configuration.getGenesNotFound());
        return plotGroup;
    }

    private GeneExpressionPlot createMeanTypePlot(DefaultCategoryDataset meanDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(meanDataset, "Mean " + genomicValueResultsType.getValue());
        legendItems = chart.getLegend().getSources()[0].getLegendItems();
        cusomtizeBarChart(chart);
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(meanDataset));
        return plot;
    }
    
    private GeneExpressionPlot createMedianTypePlot(DefaultCategoryDataset medianDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(medianDataset, "Median " + genomicValueResultsType.getValue());
        cusomtizeBarChart(chart);
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(medianDataset));
        return plot;
    }

    private GeneExpressionPlot createLog2TypePlot(DefaultStatisticalCategoryDataset log2Dataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(log2Dataset, "Log2 " + genomicValueResultsType.getValue());
        chart.getCategoryPlot().setRenderer(cusomtizeStatisticalBarChart(chart));
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(log2Dataset));
        return plot;
    }

    private GeneExpressionPlot createBoxWhiskerTypePlot(DefaultBoxAndWhiskerCategoryDataset bwDataset, 
            GenomicValueResultsTypeEnum genomicValueResultsType) {
        GeneExpressionPlotImpl plot = new GeneExpressionPlotImpl();
        JFreeChart chart = createChart(bwDataset, "Log2 " + genomicValueResultsType.getValue());
        chart.getCategoryPlot().setRenderer(cusomtizeBoxWhiskerChart(chart));
        plot.setPlotChart(chart);
        plot.setWidth(retrievePlotWidth(bwDataset));
        return plot;
    }

    private JFreeChart createChart(CategoryDataset dataset, String rangeAxisLabel) {
        JFreeChart chart = ChartFactory.createBarChart(
                null, 
                DOMAIN_AXIS_LABEL, 
                rangeAxisLabel, 
                dataset,
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // URLs
                );
        return chart;
    }

    private void cusomtizeBarChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(ITEM_MARGIN);
        renderer.setDrawBarOutline(true);
        chart.removeLegend();
    }

    
    private StatisticalBarRenderer cusomtizeStatisticalBarChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        StatisticalBarRenderer renderer = new StatisticalBarRenderer();
        renderer.setItemMargin(ITEM_MARGIN);
        renderer.setDrawBarOutline(true);
        chart.removeLegend();
        return renderer;
    }
    
    private BoxAndWhiskerCoinPlotRenderer cusomtizeBoxWhiskerChart(JFreeChart chart) {
        customizeAxisMargin(chart);
        BoxAndWhiskerCoinPlotRenderer renderer = new BoxAndWhiskerCoinPlotRenderer();
        renderer.setDisplayAllOutliers(true);
        renderer.setDisplayMean(false);
        renderer.setFillBox(false);
        renderer.setItemMargin(ITEM_MARGIN);
        chart.removeLegend();
        return renderer;
    }
    
    private void customizeAxisMargin(JFreeChart chart) {
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setLowerMargin(LOWER_MARGIN);
        axis.setCategoryMargin(CATEGORY_MARGIN);
        axis.setUpperMargin(UPPER_MARGIN);
    }


    private void addLegendItemsToPlot(GeneExpressionPlotGroup plotGroup) {
        if (legendItems != null) {
            for (int x = 0; x < legendItems.getItemCount(); x++) {
                plotGroup.getLegendItems().add(new LegendItemWrapper(legendItems.get(x)));
            }
        }
    }
    
    private void addPlotGroupSubjectCounts(GeneExpressionPlotConfiguration configuration,
            GeneExpressionPlotGroup plotGroup) {
        for (PlotSampleGroup sampleGroup : configuration.getPlotSampleGroups()) {
            plotGroup.getGroupNameToNumberSubjectsMap().put(sampleGroup.getName(), sampleGroup.getNumberSubjects());
        }
    }
    
    private int retrievePlotWidth(CategoryDataset dataset) {
        int width = dataset.getColumnCount() * dataset.getRowCount() * WIDTH_MULTIPLIER;
        return width > MINIMUM_WIDTH ? width : MINIMUM_WIDTH;
    }
}
