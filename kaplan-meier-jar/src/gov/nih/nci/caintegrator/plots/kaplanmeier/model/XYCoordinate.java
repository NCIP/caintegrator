/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier.model;





public class XYCoordinate {

    private boolean checked = false;
    private Number x;
    private Number y;

    public XYCoordinate(Number x, Number y){
        this.x = x;
        this.y = y;
    }
    public XYCoordinate(Number x, Number y, boolean b){
        this(x, y);
        this.checked = b;
    }

    public Number getX() {
        return x;
    }

    public void setX(Number x) {
        this.x = x;
    }

    public Number getY() {
        return y;
    }

    public void setY(Number y) {
        this.y = y;
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
