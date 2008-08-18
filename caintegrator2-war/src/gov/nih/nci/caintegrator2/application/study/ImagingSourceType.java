package gov.nih.nci.caintegrator2.application.study;

public enum ImagingSourceType {

	/**
     * Imaging data will be loaded from an external CTODS server.
     */
    NCIA,
    
    /**
     * Imaging data will be loaded from a comma-separated value format text file.
     */
    DELIMITED_TEXT
}
