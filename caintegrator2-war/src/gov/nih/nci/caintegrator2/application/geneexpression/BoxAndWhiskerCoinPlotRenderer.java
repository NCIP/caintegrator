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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.Outlier;
import org.jfree.chart.renderer.OutlierList;
import org.jfree.chart.renderer.OutlierListCollection;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;

/**
 * This code was taken from Rembrandt and modified slightly.  The purpose is to show all
 * outliers as coins on the plot as opposed to just triangles which represent far outliers.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity", "PMD.ExcessiveMethodLength", 
                    "PMD.ExcessiveParameterList", "unchecked" }) // This complex code was taken from Rembrandt
public class BoxAndWhiskerCoinPlotRenderer extends BoxAndWhiskerRenderer {

    private static final long serialVersionUID = 1L;
    private static final Double DEFAULT_MAX_BAR_WIDTH = 40.00;
    private double maxBarWidth = DEFAULT_MAX_BAR_WIDTH;

    private boolean displayMean = true;
    private boolean displayMedian = true;
    private boolean displayAllOutliers = false;
    private int outlierRadiusDenominator = 3;
    private String plotColor = null;

    /**
     * Default constructor.
     */
    public BoxAndWhiskerCoinPlotRenderer() {
        super();
        this.setFillBox(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, CategoryPlot plot,
            int rendererIndex, PlotRenderingInfo info) {

        CategoryItemRendererState state = super.initialise(g2, dataArea, plot, rendererIndex, info);

        if (state.getBarWidth() > maxBarWidth) {
            state.setBarWidth(maxBarWidth);
        }
        return state;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawVerticalItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
            CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row,
            int column) {
        BoxAndWhiskerCategoryDataset bawDataset = (BoxAndWhiskerCategoryDataset) dataset;
        double categoryEnd = domainAxis.getCategoryEnd(column, getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryStart = domainAxis
                .getCategoryStart(column, getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryWidth = categoryEnd - categoryStart;
        double xx = categoryStart;
        int seriesCount = getRowCount();
        int categoryCount = getColumnCount();
        xx = retrieveXx(state, dataArea, row, categoryWidth, xx, seriesCount, categoryCount);
        Paint p = null;
        if (this.getPlotColor() != null) {
            p = PaintUtilities.stringToColor(getPlotColor()); // coin plot should all be one color
        } else {
            p = getItemPaint(row, column);
        }
        if (p != null) {
            g2.setPaint(p);
        }
        Stroke s = getItemStroke(row, column);
        g2.setStroke(s);
        double aRadius = 0; // average radius
        RectangleEdge location = drawRectangles(g2, state, dataArea, plot, rangeAxis, row, column, bawDataset, xx);
        g2.setPaint(getArtifactPaint());
        if (this.isDisplayMean()) {
            aRadius = drawMean(g2, state, dataArea, rangeAxis, row, column, bawDataset, xx, aRadius, location);
        }
        if (this.isDisplayMedian()) {
            // draw median...
            Number yMedian = bawDataset.getMedianValue(row, column);
            if (yMedian != null) {
                double yyMedian = rangeAxis.valueToJava2D(yMedian.doubleValue(), dataArea, location);
                g2.draw(new Line2D.Double(xx, yyMedian, xx + state.getBarWidth(), yyMedian));
            }
        }
        double maxAxisValue = rangeAxis.valueToJava2D(rangeAxis.getUpperBound(), dataArea, location) + aRadius;
        double minAxisValue = rangeAxis.valueToJava2D(rangeAxis.getLowerBound(), dataArea, location) - aRadius;
        g2.setPaint(p);
        drawOutliers(g2, state, dataArea, rangeAxis, row, column, bawDataset, xx, aRadius, location, maxAxisValue,
                minAxisValue);
    }

    private double retrieveXx(CategoryItemRendererState state, Rectangle2D dataArea, int row, double categoryWidth,
            double xx, int seriesCount, int categoryCount) {
        double newXx = xx;
        if (seriesCount > 1) {
            double seriesGap = dataArea.getWidth() * getItemMargin() / (categoryCount * (seriesCount - 1));
            double usedWidth = (state.getBarWidth() * seriesCount) + (seriesGap * (seriesCount - 1));
            // offset the start of the boxes if the total width used is smaller
            // than the category width
            double offset = (categoryWidth - usedWidth) / 2;
            newXx = xx + offset + (row * (state.getBarWidth() + seriesGap));
        } else {
            double offset = (categoryWidth - state.getBarWidth()) / 2;
            newXx = xx + offset;
        }
        return newXx;
    }

    private double drawMean(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, ValueAxis rangeAxis,
            int row, int column, BoxAndWhiskerCategoryDataset bawDataset, double xx, double aRadius,
            RectangleEdge location) {
        double yyAverage;
        double newARadius = aRadius;
        // draw mean - SPECIAL AIMS REQUIREMENT...
        Number yMean = bawDataset.getMeanValue(row, column);
        if (yMean != null) {
            yyAverage = rangeAxis.valueToJava2D(yMean.doubleValue(), dataArea, location);
            newARadius = state.getBarWidth() / 4;
            Ellipse2D.Double avgEllipse = new Ellipse2D.Double(xx + aRadius, yyAverage - aRadius, aRadius * 2,
                    aRadius * 2);
            g2.fill(avgEllipse);
            g2.draw(avgEllipse);
        }
        return newARadius;
    }

    private RectangleEdge drawRectangles(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
            CategoryPlot plot, ValueAxis rangeAxis, int row, int column, BoxAndWhiskerCategoryDataset bawDataset,
            double xx) {
        RectangleEdge location = plot.getRangeAxisEdge();

        Number yQ1 = bawDataset.getQ1Value(row, column);
        Number yQ3 = bawDataset.getQ3Value(row, column);
        Number yMax = bawDataset.getMaxRegularValue(row, column);
        Number yMin = bawDataset.getMinRegularValue(row, column);
        Shape box = null;
        if (yQ1 != null && yQ3 != null && yMax != null && yMin != null) {
            double yyQ1 = rangeAxis.valueToJava2D(yQ1.doubleValue(), dataArea, location);
            double yyQ3 = rangeAxis.valueToJava2D(yQ3.doubleValue(), dataArea, location);
            double yyMax = rangeAxis.valueToJava2D(yMax.doubleValue(), dataArea, location);
            double yyMin = rangeAxis.valueToJava2D(yMin.doubleValue(), dataArea, location);
            double xxmid = xx + state.getBarWidth() / 2.0;

            // draw the upper shadow...
            g2.draw(new Line2D.Double(xxmid, yyMax, xxmid, yyQ3));
            g2.draw(new Line2D.Double(xx, yyMax, xx + state.getBarWidth(), yyMax));

            // draw the lower shadow...
            g2.draw(new Line2D.Double(xxmid, yyMin, xxmid, yyQ1));
            g2.draw(new Line2D.Double(xx, yyMin, xx + state.getBarWidth(), yyMin));

            // draw the body...
            box = new Rectangle2D.Double(xx, Math.min(yyQ1, yyQ3), state.getBarWidth(), Math.abs(yyQ1 - yyQ3));
            if (getFillBox()) {
                g2.fill(box);
            }
            g2.draw(box);
        }
        return location;
    }

    private void drawOutliers(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
            ValueAxis rangeAxis, int row, int column, BoxAndWhiskerCategoryDataset bawDataset, double xx,
            double aRadius, RectangleEdge location, double maxAxisValue, double minAxisValue) {
        double yyOutlier;
        double oRadius = state.getBarWidth() / this.outlierRadiusDenominator; // outlier radius
        List<Outlier> outliers = new ArrayList<Outlier>();
        OutlierListCollection outlierListCollection = new OutlierListCollection();

        List<Number> yOutliers = (List<Number>) bawDataset.getOutliers(row, column);
        if (yOutliers != null) {
            for (int i = 0; i < yOutliers.size(); i++) {
                double outlier = yOutliers.get(i).doubleValue();
                Number minOutlier = bawDataset.getMinOutlier(row, column);
                Number maxOutlier = bawDataset.getMaxOutlier(row, column);
                Number minRegular = bawDataset.getMinRegularValue(row, column);
                Number maxRegular = bawDataset.getMaxRegularValue(row, column);
                if (outlier > maxOutlier.doubleValue()) {
                    outlierListCollection.setHighFarOut(true);
                } else if (outlier < minOutlier.doubleValue()) {
                    outlierListCollection.setLowFarOut(true);
                } else if (outlier > maxRegular.doubleValue()) {
                    yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                    outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
                } else if (outlier < minRegular.doubleValue()) {
                    yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                    outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
                }

                Collections.sort(outliers);
            }
            if (!displayAllOutliers) {
                for (int i = 0; i < yOutliers.size(); i++) {
                    Number minRegular = bawDataset.getMinRegularValue(row, column);
                    Number maxRegular = bawDataset.getMaxRegularValue(row, column);
                    double outlier = ((Number) yOutliers.get(i)).doubleValue();
                    if (outlier < minRegular.doubleValue() || outlier > maxRegular.doubleValue()) {
                        yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                        outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
                    }
                }
                for (Iterator <Outlier> iterator = outliers.iterator(); iterator.hasNext();) {
                    Outlier outlier = iterator.next();
                    outlierListCollection.add(outlier);
                }
                for (Iterator <OutlierList> iterator = outlierListCollection.iterator(); iterator.hasNext();) {
                    OutlierList list =  iterator.next();
                    Outlier outlier = list.getAveragedOutlier();
                    Point2D point = outlier.getPoint();

                    if (list.isMultiple()) {
                        drawMultipleEllipse(point, state.getBarWidth(), oRadius, g2);
                    } else {
                        drawEllipse(point, oRadius, g2);
                    }
                }
                if (outlierListCollection.isHighFarOut()) {
                    drawHighFarOut(aRadius / 2.0, g2, xx + state.getBarWidth() / 2.0, maxAxisValue);
                }
                if (outlierListCollection.isLowFarOut()) {
                    drawLowFarOut(aRadius / 2.0, g2, xx + state.getBarWidth() / 2.0, minAxisValue);
                }
            } else {
                drawCoinOutliers(g2, state, dataArea, rangeAxis, row, column, bawDataset, xx, location, oRadius,
                        outliers, yOutliers);
            }
        }
    }

    private void drawCoinOutliers(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
            ValueAxis rangeAxis, int row, int column, BoxAndWhiskerCategoryDataset bawDataset, double xx,
            RectangleEdge location, double oRadius, List<Outlier> outliers, List<Number> yOutliers) {
        double yyOutlier;
        for (int i = 0; i < yOutliers.size(); i++) {
            Number minRegular = bawDataset.getMinRegularValue(row, column);
            Number maxRegular = bawDataset.getMaxRegularValue(row, column);
            double outlier = ((Number) yOutliers.get(i)).doubleValue();
            if (outlier < minRegular.doubleValue() || outlier > maxRegular.doubleValue()) {
                yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
            }
        }
        Collections.sort(outliers);
        for (Iterator<Outlier> iterator = outliers.iterator(); iterator.hasNext();) {
            Outlier outlier = iterator.next();
            Point2D point = outlier.getPoint();

            drawEllipse(point, oRadius, g2);
        }
    }

    private void drawEllipse(Point2D point, double oRadius, Graphics2D g2) {
        Ellipse2D dot = new Ellipse2D.Double(point.getX() + oRadius / 2, point.getY(), oRadius, oRadius);
        g2.draw(dot);
    }

    private void drawMultipleEllipse(Point2D point, double boxWidth, double oRadius, Graphics2D g2) {

        Ellipse2D dot1 = new Ellipse2D.Double(point.getX() - (boxWidth / 2) + oRadius, point.getY(), oRadius, oRadius);
        Ellipse2D dot2 = new Ellipse2D.Double(point.getX() + (boxWidth / 2), point.getY(), oRadius, oRadius);
        g2.draw(dot1);
        g2.draw(dot2);
    }

    private void drawHighFarOut(double aRadius, Graphics2D g2, double xx, double m) {
        double side = aRadius * 2;
        g2.draw(new Line2D.Double(xx - side, m + side, xx + side, m + side));
        g2.draw(new Line2D.Double(xx - side, m + side, xx, m));
        g2.draw(new Line2D.Double(xx + side, m + side, xx, m));
    }

    private void drawLowFarOut(double aRadius, Graphics2D g2, double xx, double m) {
        double side = aRadius * 2;
        g2.draw(new Line2D.Double(xx - side, m - side, xx + side, m - side));
        g2.draw(new Line2D.Double(xx - side, m - side, xx, m));
        g2.draw(new Line2D.Double(xx + side, m - side, xx, m));
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.getArtifactPaint(), stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        setArtifactPaint(SerialUtilities.readPaint(stream));
    }

    /**
     * @return Returns the maxBarWidth.
     */
    public double getMaxBarWidth() {
        return maxBarWidth;
    }

    /**
     * @param maxBarWidth
     *            The maxBarWidth to set.
     */
    public void setMaxBarWidth(double maxBarWidth) {
        this.maxBarWidth = maxBarWidth;
    }

    /**
     * @return Returns the displayMean.
     */
    public boolean isDisplayMean() {
        return displayMean;
    }

    /**
     * @param displayMean
     *            The displayMean to set.
     */
    public void setDisplayMean(boolean displayMean) {
        this.displayMean = displayMean;
    }

    /**
     * @return Returns the displayMedian.
     */
    public boolean isDisplayMedian() {
        return displayMedian;
    }

    /**
     * @param displayMedian
     *            The displayMedian to set.
     */
    public void setDisplayMedian(boolean displayMedian) {
        this.displayMedian = displayMedian;
    }

    /**
     * @return Returns the outlierRadiusDenominator.
     */
    public int getOutlierRadiusDenominator() {
        return outlierRadiusDenominator;
    }

    /**
     * @param outlierRadiusDenominator
     *            The outlierRadiusDenominator to set.
     */
    public void setOutlierRadiusDenominator(int outlierRadiusDenominator) {
        this.outlierRadiusDenominator = outlierRadiusDenominator;
    }

    /**
     * @return Returns the displayAllOutliers.
     */
    public boolean isDisplayAllOutliers() {
        return displayAllOutliers;
    }

    /**
     * @param displayAllOutliers
     *            The displayAllOutliers to set.
     */
    public void setDisplayAllOutliers(boolean displayAllOutliers) {
        this.displayAllOutliers = displayAllOutliers;
    }

    /**
     * @return Returns the plotColor.
     */
    public String getPlotColor() {
        return plotColor;
    }

    /**
     * @param plotColor
     *            The plotColor to set.
     */
    public void setPlotColor(String plotColor) {
        this.plotColor = plotColor;
    }

}
