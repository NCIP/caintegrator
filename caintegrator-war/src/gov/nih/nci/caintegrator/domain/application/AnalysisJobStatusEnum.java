/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible job status values for <code>GenePatternAnalysisJob.status</code>.
 */
public enum AnalysisJobStatusEnum {

    /**
     * Not yet submitted, still in creation state.
     */
    NOT_SUBMITTED("Not Submitted", true, false),

    /**
     * Submitted and validated, ready to be run.
     */
    SUBMITTED("Submitted", false, false),

    /**
     * Processing Locally.
     */
    PROCESSING_LOCALLY("Processing Locally", false, false),

    /**
     * Processing Remotely.
     */
    PROCESSING_REMOTELY("Processing Remotely", false, false),

    /**
     * Error Connecting.
     */
    ERROR_CONNECTING("Error Connecting", true, true),

    /**
     * Error Connecting.
     */
    INVALID_PARAMETER("Invalid Parameter", true, true),

    /**
     * Local Error.
     */
    LOCAL_ERROR("Local Error", true, true),

    /**
     * Completed.
     */
    COMPLETED("Completed", true, false);

    private String value;
    private boolean deletable;
    private boolean errorState;

    private AnalysisJobStatusEnum(String value, boolean deletable, boolean errorState) {
        this.value = value;
        this.deletable = deletable;
        this.errorState = errorState;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     *
     * @return if the status is in an error state.
     */
    public boolean isErrorState() {
        return errorState;
    }
}
