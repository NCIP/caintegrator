/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier;

import gov.nih.nci.caintegrator.plots.kaplanmeier.model.GroupCoordinates;

import java.util.Collection;


/**
* @author caIntegrator Team
*/

public interface KMPlotter {

   public void writeBufferedImage(java.io.OutputStream out, java.awt.image.BufferedImage image, ImageTypes imgType)
   throws KMException;
   //public void writeBufferedImageAsJPEG(java.io.OutputStream out, java.awt.image.BufferedImage image) throws IOException ;
   public java.awt.image.BufferedImage createImage(Collection<GroupCoordinates> groupsToBePlotted, String plotName,
                                                   String durationAxisLabel, String probablityAxisLabel);
   public Object createImageOfKnownType(Collection<GroupCoordinates> groupsToBePlotted, String plotName,
           String durationAxisLabel, String probablityAxisLabel);


}
