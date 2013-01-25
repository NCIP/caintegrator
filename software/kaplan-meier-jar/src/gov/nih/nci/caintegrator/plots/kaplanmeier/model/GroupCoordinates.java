/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
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
    final Collection<XYCoordinate> dataPoints;
    final String groupName;
    final Color color;
    final Integer groupSize;

    public GroupCoordinates(Collection<XYCoordinate> dataPoints, String groupName, Color color, Integer groupSize) {
        this.dataPoints = dataPoints;
        this.groupName = groupName;
        this.color = color;
        this.groupSize = groupSize;
    }

    public Collection<XYCoordinate> getDataPoints() {
        return dataPoints;
    }

    public String getGroupName() {
        return groupName;
    }

    public Color getColor() {
        return color;
    }

    public Integer getGroupSize() {
        return groupSize;
    }
}
