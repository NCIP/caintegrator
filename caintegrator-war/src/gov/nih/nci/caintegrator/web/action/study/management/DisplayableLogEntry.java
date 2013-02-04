/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.LogEntry;

/**
 * 
 */
public class DisplayableLogEntry implements Comparable<DisplayableLogEntry> {
    
    private LogEntry logEntry = new LogEntry();
    private String description;
    private boolean updateDescription = false;
    
    /**
     * Default constructor.
     */
    public DisplayableLogEntry() { 
        // Empty Constructor
    }
    
    /**
     * Constructor for logEntry.
     * @param logEntry used to construct this object.
     */
    public DisplayableLogEntry(LogEntry logEntry) {
        this.logEntry = logEntry;
        description = logEntry.getDescription();
    }

    /**
     * @return the logEntry
     */
    public LogEntry getLogEntry() {
        return logEntry;
    }

    /**
     * @param logEntry the logEntry to set
     */
    public void setLogEntry(LogEntry logEntry) {
        this.logEntry = logEntry;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the updateDescription
     */
    public boolean isUpdateDescription() {
        return updateDescription;
    }

    /**
     * @param updateDescription the updateDescription to set
     */
    public void setUpdateDescription(boolean updateDescription) {
        this.updateDescription = updateDescription;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(DisplayableLogEntry o) {
        // More recent dates are sorted ahead of older dates.
        return o.getLogEntry().getLogDate().compareTo(getLogEntry().getLogDate());
    }
    


}
