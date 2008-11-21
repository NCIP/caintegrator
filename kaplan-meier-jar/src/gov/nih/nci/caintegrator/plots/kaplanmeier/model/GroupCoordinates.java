package gov.nih.nci.caintegrator.plots.kaplanmeier.model;

import gov.nih.nci.caintegrator.plots.kaplanmeier.model.XYCoordinate;

import java.util.Collection;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ram Bhattaru
 * Date: Sep 12, 2007
 * Time: 11:33:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupCoordinates {
    Collection<XYCoordinate> dataPoints;
    String groupName;
    Color color;

    public GroupCoordinates(Collection<XYCoordinate> dataPoints, String groupName, Color color) {
        this.dataPoints = dataPoints;
        this.groupName = groupName;
        this.color = color;
    }

    public Collection<XYCoordinate> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Collection<XYCoordinate> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
