/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.model;

import org.jfree.data.xy.XYDataItem;

/**
 * @author caIntegrator Team
 */

/**
 * This class extends the JFreeChart class XYDataItem and 
 * adds a third parameter for census. If census == true,
 * draw a "+" at that point.
 */

@SuppressWarnings("serial")
public class KMPlotPoint extends XYDataItem {

	private boolean checked = false; 
	
	public KMPlotPoint(Number x, Number y){
		super(x,y);
	}
	public KMPlotPoint(Number x, Number y, boolean b){
        super(x,y);
		this.checked = b;
	}

    /**
	 * @return Returns the isCensus
	 */
	public boolean isChecked() {
		return checked;
	}
	public String toString(){
	    return ("( "+getX()+", "+getY()+")  Census:"+checked);
	}
}
