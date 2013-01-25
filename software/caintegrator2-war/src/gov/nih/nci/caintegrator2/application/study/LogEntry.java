/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Date;

/**
 * 
 */
public class LogEntry extends AbstractCaIntegrator2Object {

    private static final int MAX_LENGTH = 255;

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;
    
    private static final String QUOTE = "'";
    private static final String COMMA = ", ";
    private static final String NAME = "Name = ";
    private static final String FILENAME = "Filename = ";
    private static final String SERVER_INFORMATION = "Server Information = ";
    private static final String EXPERIMENT_IDENTIFIER = "Experiment Identifier = ";
    private static final String COLLECTION_NAME = "Collection Name = ";
    
    private static final String CREATE_STUDY = "Create Study: ";
    private static final String SAVE_STUDY = "Save Study: ";
    private static final String DEPLOY_STUDY = "Deploy Study: ";
    
    private static final String ADD_SUBJECT_ANNOTATION_FILE = "Add Subject Annotation File: ";
    private static final String ADD_IMAGING_ANNOTATION_FILE = "Add Imaging Annotation File: ";
    private static final String ADD_EXTERNAL_LINKS = "Add External Links: ";
    private static final String ADD_SAMPLE_MAPPING_FILE = "Add Sample Mapping File: ";
    private static final String ADD_CONTROL_SAMPLE_MAPPING_FILE = "Add Control Sample Mapping File: ";
    
    
    private static final String LOAD_SUBJECT_ANNOTATION_FILE = "Load Subject Annotation File: ";
    private static final String LOAD_GENOMIC_SOURCE = "Load Genomic Source: ";
    private static final String LOAD_IMAGING_SOURCE_ANNOTATIONS = "Load Imaging Source Annotations: ";
    
    private static final String SAVE_ANNOTATION_GROUP = "Save Annotation Group: ";
    private static final String SAVE_SURVIVAL_VALUE_DEFINITION = "Save Survival Value Definition: ";
    private static final String SAVE_SUBJECT_ANNOTATION_SOURCE = "Save Subject Annotation Source: ";
    private static final String SAVE_GENOMIC_SOURCE = "Save Genomic Source: ";
    private static final String SAVE_IMAGING_SOURCE = "Save Imaging Source: ";
    
    private static final String DELETE_ANNOTATION_GROUP = "Delete Annotation Group: ";
    private static final String DELETE_SUBJECT_ANNOTATION_FILE = "Delete Subject Annotation File: ";
    private static final String DELETE_SURVIVAL_VALUE_DEFINITION = "Delete Survival Value Definition: ";
    private static final String DELETE_EXTERNAL_LINKS = "Delete External Links: ";
    private static final String DELETE_GENOMIC_SOURCE = "Delete Genomic Source: ";
    private static final String DELETE_IMAGING_SOURCE = "Delete Imaging Source: ";
    
    
    private Date logDate = new Date();
    private String systemLogMessage;
    private String description;
    private String username;
    
    /**
     * @return the logDate
     */
    public Date getLogDate() {
        return logDate;
    }
    /**
     * @param logDate the logDate to set
     */
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    /**
     * @return the systemLogMessage
     */
    public String getSystemLogMessage() {
        return systemLogMessage;
    }
    /**
     * @param systemLogMessage the systemLogMessage to set
     */
    public void setSystemLogMessage(String systemLogMessage) {
        this.systemLogMessage = systemLogMessage;
    }
    /**
     * @param trimSystemLogMessage the systemLogMessage to trim and set
     */
    public void setTrimSystemLogMessage(String trimSystemLogMessage) {
        this.systemLogMessage = Cai2Util.trimStringIfTooLong(trimSystemLogMessage, MAX_LENGTH);
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
     * @param trimDescription the description to trim and set
     */
    public void setTrimDescription(String trimDescription) {
        this.description = Cai2Util.trimStringIfTooLong(trimDescription, MAX_LENGTH);
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 
     * @return displayableLogDate.
     */
    public String getDisplayableLogDate() {
        return DateUtil.getDisplayableTimeStamp(logDate); 
    }
    
    /**
     * 
     * @param study to get system log message for.
     * @return System log message
     */
    public static String getSystemLogCreate(Study study) {
        return CREATE_STUDY 
            + NAME + QUOTE + study.getShortTitleText() + QUOTE; 
    }
    
    /**
     * 
     * @param study to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSave(Study study) {
        return SAVE_STUDY 
            + NAME + QUOTE + study.getShortTitleText() + QUOTE; 
    }
    
    /**
     * 
     * @param study to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDeploy(Study study) {
        return DEPLOY_STUDY 
            + NAME + QUOTE + study.getShortTitleText() + QUOTE; 
    }
    
    /**
     * 
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogAddSubjectAnnotationFile(String fileName) {
        return ADD_SUBJECT_ANNOTATION_FILE 
            + FILENAME + QUOTE + fileName + QUOTE; 
    }
    
    /**
     * 
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogLoadSubjectAnnotationFile(String fileName) {
        return LOAD_SUBJECT_ANNOTATION_FILE 
            + FILENAME + QUOTE + fileName + QUOTE; 
    }
    
    /**
     * 
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDeleteSubjectAnnotationFile(String fileName) {
        return DELETE_SUBJECT_ANNOTATION_FILE 
            + FILENAME + QUOTE + fileName + QUOTE; 
    }
    
    /**
     * 
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSaveSubjectAnnotationSource(String fileName) {
        return SAVE_SUBJECT_ANNOTATION_SOURCE 
            + FILENAME + QUOTE + fileName + QUOTE; 
    }
    
    /**
     * 
     * @param externalLinks to get system log message for.
     * @return System log message
     */
    public static String getSystemLogAdd(ExternalLinkList externalLinks) {
        return ADD_EXTERNAL_LINKS 
            + NAME + QUOTE + externalLinks.getName() + QUOTE + COMMA
            + FILENAME + QUOTE + externalLinks.getFileName() + QUOTE; 
    }
    
    /**
     * 
     * @param externalLinks to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDelete(ExternalLinkList externalLinks) {
        return DELETE_EXTERNAL_LINKS 
            + NAME + QUOTE + externalLinks.getName() + QUOTE + COMMA
            + FILENAME + QUOTE + externalLinks.getFileName() + QUOTE; 
    }
    
    /**
     * 
     * @param annotationGroup to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSave(AnnotationGroup annotationGroup) {
        return SAVE_ANNOTATION_GROUP 
            + NAME + QUOTE + annotationGroup.getName() + QUOTE;
    }
    
    /**
     * 
     * @param annotationGroup to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDelete(AnnotationGroup annotationGroup) {
        return DELETE_ANNOTATION_GROUP 
            + NAME + QUOTE + annotationGroup.getName() + QUOTE;
    }
    
    /**
     * 
     * @param survivalValueDefinition to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSave(SurvivalValueDefinition survivalValueDefinition) {
        return SAVE_SURVIVAL_VALUE_DEFINITION 
            + NAME + QUOTE + survivalValueDefinition.getName() + QUOTE;
    }
    
    /**
     * 
     * @param survivalValueDefinition to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDelete(SurvivalValueDefinition survivalValueDefinition) {
        return DELETE_SURVIVAL_VALUE_DEFINITION 
            + NAME + QUOTE + survivalValueDefinition.getName() + QUOTE;
    }
    
    /**
     * 
     * @param genomicSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSave(GenomicDataSourceConfiguration genomicSource) {
        return SAVE_GENOMIC_SOURCE 
            + SERVER_INFORMATION + QUOTE + genomicSource.getServerProfile().toString() + QUOTE + COMMA
            + EXPERIMENT_IDENTIFIER + QUOTE + genomicSource.getExperimentIdentifier() + QUOTE + COMMA
            + "Data Type = " + QUOTE + genomicSource.getDataTypeString() + QUOTE + COMMA
            + "Platform Name = " + QUOTE + genomicSource.getPlatformName() + QUOTE;
    }
    
    /**
     * 
     * @param genomicSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDelete(GenomicDataSourceConfiguration genomicSource) {
        return DELETE_GENOMIC_SOURCE 
            + EXPERIMENT_IDENTIFIER + QUOTE + genomicSource.getExperimentIdentifier() + QUOTE;
    }
    
    /**
     * 
     * @param genomicSource to get system log message for.
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogAddSampleMappingFile(GenomicDataSourceConfiguration genomicSource, 
            String fileName) {
        return ADD_SAMPLE_MAPPING_FILE 
            + EXPERIMENT_IDENTIFIER + QUOTE + genomicSource.getExperimentIdentifier() + QUOTE + COMMA
            + FILENAME + QUOTE + fileName + QUOTE;
    }
    
    /**
     * 
     * @param genomicSource to get system log message for.
     * @param fileName to get system log message for.
     * @return System log message
     */
    public static String getSystemLogAddControlSampleMappingFile(GenomicDataSourceConfiguration genomicSource, 
            String fileName) {
        return ADD_CONTROL_SAMPLE_MAPPING_FILE 
            + EXPERIMENT_IDENTIFIER + QUOTE + genomicSource.getExperimentIdentifier() + QUOTE + COMMA
            + FILENAME + QUOTE + fileName + QUOTE;
    }
    
    /**
     * 
     * @param genomicSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogLoad(GenomicDataSourceConfiguration genomicSource) {
        return LOAD_GENOMIC_SOURCE 
            + SERVER_INFORMATION + QUOTE + genomicSource.getServerProfile().toString() + QUOTE + COMMA
            + EXPERIMENT_IDENTIFIER + QUOTE + genomicSource.getExperimentIdentifier() + QUOTE + COMMA
            + "Data Type = " + QUOTE + genomicSource.getDataTypeString() + QUOTE + COMMA
            + "Platform Name = " + QUOTE + genomicSource.getPlatformName() + QUOTE;
    }
    
    /**
     * 
     * @param imagingSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogSave(ImageDataSourceConfiguration imagingSource) {
        return SAVE_IMAGING_SOURCE 
            + SERVER_INFORMATION + QUOTE + imagingSource.getServerProfile().toString() + QUOTE + COMMA
            + COLLECTION_NAME + QUOTE + imagingSource.getCollectionName() + QUOTE;
    }
    
    /**
     * 
     * @param imagingSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogDeleteImagingSource(ImageDataSourceConfiguration imagingSource) {
        return DELETE_IMAGING_SOURCE 
            + COLLECTION_NAME + QUOTE + imagingSource.getCollectionName() + QUOTE;
    }
    
    /**
     * 
     * @param imagingSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogLoad(ImageDataSourceConfiguration imagingSource) {
        return LOAD_IMAGING_SOURCE_ANNOTATIONS 
        + COLLECTION_NAME + QUOTE + imagingSource.getCollectionName() + QUOTE;
    }
    
    /**
     * 
     * @param imagingSource to get system log message for.
     * @return System log message
     */
    public static String getSystemLogAdd(ImageDataSourceConfiguration imagingSource) {
        if (imagingSource.getImageAnnotationConfiguration().isAimDataService()) {
            return ADD_IMAGING_ANNOTATION_FILE
            + COLLECTION_NAME + QUOTE + imagingSource.getCollectionName() + QUOTE + COMMA
            + "using AIM Data Service: " + QUOTE
            + imagingSource.getImageAnnotationConfiguration().getAimServerProfile().getUrl() + QUOTE;
        } else {
            return ADD_IMAGING_ANNOTATION_FILE
                + COLLECTION_NAME + QUOTE + imagingSource.getCollectionName() + QUOTE + COMMA
                + FILENAME + QUOTE 
                + imagingSource.getImageAnnotationConfiguration().getAnnotationFile().getFile().getName() + QUOTE;
        }
    }
}
