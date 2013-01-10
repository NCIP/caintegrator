/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import java.util.Date;

/**
 * 
 */
public interface TimeStampable {
    
    /**
     * 
     * @param lastModifiedDate timestamp of the date of last modification.
     */
    void setLastModifiedDate(Date lastModifiedDate);
    
    /**
     * 
     * @return timestamp of date of last modification.
     */
    Date getLastModifiedDate();
    
    /**
     * 
     * @return formatted lastModifiedDate object.
     */
    String getDisplayableLastModifiedDate();

}
