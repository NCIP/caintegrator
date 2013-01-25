/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.model;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;



/**
 * @author caIntegrator Team
 * This class was written to store Kaplan-Meier data points 
 * as they are input. It extends the XYSeries from the JFreeChart
 * package.  This resolves an issue with the superclass sorting
 * the inputs by X and Y, messing up the step graph data lines.
 * 
 */

@SuppressWarnings({ "unchecked", "serial" })
 public class KMPlotPointSeries extends XYSeries {
    	private SeriesType myType;
    
        public KMPlotPointSeries(String string, boolean arg) {
            super(string, arg);
        }
        public void setSeriesType(SeriesType type) {
            myType = type;
        }

        public SeriesType getType() {
            return myType;
        }
        public void add(float x, float y, int index) {
            data.add(index, new XYDataItem(x,y));
        }
        public void add(float x, float y) {
            data.add(new XYDataItem(x,y));
        }

        public void add(XYDataItem xyDataPair) {
            data.add(xyDataPair);
        }

        public void add(XYDataItem xyDataPair, int index) {
            data.add(index, xyDataPair);
        }

        public XYDataItem getDataPair(int index) {
            return (XYDataItem)data.get(index);
        }

        public Number getXValue(int i) {
            return  ((XYDataItem)(data.get(i))).getX();
        }

        public Number getYValue(int i) {
            return  ((XYDataItem)(data.get(i))).getY();
        }

        public int getItemCount() {
            return data.size();
        }

        public enum SeriesType{PROBABILITY,CENSOR}
}
