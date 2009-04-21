ALTER TABLE ABSTRACT_LIST DROP FOREIGN KEY FK_ABSTRACT_LIST_STUDY_SUBSCRIPTION;
ALTER TABLE SAMPLE DROP FOREIGN KEY SAMPLE_SAMPLE_ACQUISITION;
ALTER TABLE GENOMIC_DATA_QUERY_RESULT DROP FOREIGN KEY FK_GENOMIC_DATA_QUERY_RESULT_QUERY;
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_COLUMN_SAMPLE_ACQ;
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_COLUMN_GENOMIC_QUERY_RESULT;
ALTER TABLE GENOMIC_DATA_RESULT_ROW DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_ROW_ABSTRACT_REPORTER;
ALTER TABLE GENOMIC_DATA_RESULT_ROW DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_ROW_GENOMIC_QUERY_RESULT;
ALTER TABLE GENOMIC_DATA_RESULT_VALUE DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_VALUE_RESULT_ROW;
ALTER TABLE GENOMIC_DATA_RESULT_VALUE DROP FOREIGN KEY FK_GENOMIC_DATA_RESULT_VALUE_RESULT_COLUMN;
ALTER TABLE SAMPLE_LIST_SAMPLE DROP FOREIGN KEY FK_SAMPLE_LIST_SAMPLE_SAMPLE_LIST;
ALTER TABLE SAMPLE_LIST_SAMPLE DROP FOREIGN KEY FK_SAMPLE_LIST_SAMPLE_SAMPLE;
ALTER TABLE GENE_LIST_GENE DROP FOREIGN KEY FK_GENE_LIST_GENE_GENE_LIST;
ALTER TABLE GENE_LIST_GENE DROP FOREIGN KEY FK_GENE_LIST_GENE_GENE;
ALTER TABLE FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES DROP FOREIGN KEY FK_FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES_CRITERION;
ALTER TABLE FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES DROP FOREIGN KEY FK_FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES_SAMPLE;

ALTER TABLE SUBJECT_ANNOTATION DROP FOREIGN KEY FK_SUBJECT_ANNOTATION_ANNOTATION_VALUE;
ALTER TABLE SUBJECT_ANNOTATION DROP FOREIGN KEY FK_SUBJECT_ANNOTATION_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE SUBJECT_ANNOTATION DROP FOREIGN KEY FK_SUBJECT_ANNOTATION_TIMEPOINT;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_IMAGE_SERIES;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_SAMPLE_ACQUISITION;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_IMAGE;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_ABSTRACT_PERMISSIBLE_VALUE;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_DEFINITION;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_FIELD_DESCRIPTOR;
ALTER TABLE ABSTRACT_PERMISSIBLE_VALUE DROP FOREIGN KEY FK_ABSTRACT_PERMISSIBLE_VALUE_ANNOTATION_DEFINITION;
ALTER TABLE RESULT_COLUMN DROP FOREIGN KEY FK_RESULT_COLUMN_QUERY;
ALTER TABLE RESULT_COLUMN DROP FOREIGN KEY FK_RESULT_COLUMN_ANNOTATION_DEFINITION;
ALTER TABLE RESULT_ROW DROP FOREIGN KEY RESULT_ROW_QUERY_RESULT;
ALTER TABLE RESULT_ROW DROP FOREIGN KEY RESULT_ROW_SAMPLE_ACQUISITION ;
ALTER TABLE RESULT_ROW DROP FOREIGN KEY RESULT_ROW_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE RESULT_VALUE DROP FOREIGN KEY FK_RESULT_VALUE_ANNOTATION_VALUE ;
ALTER TABLE RESULT_VALUE DROP FOREIGN KEY FK_RESULT_VALUE_RESULT_COLUMN;
ALTER TABLE RESULT_VALUE DROP FOREIGN KEY FK_RESULT_VALUE_RESULT_ROW;
ALTER TABLE QUERY_RESULT DROP FOREIGN KEY FK_QUERY_RESULT_QUERY;
ALTER TABLE ABSTRACT_CRITERION DROP FOREIGN KEY FK_ABSTRACT_CRITERION_COUMPOUND_CRITERION;
ALTER TABLE ABSTRACT_ANNOTATION_CRITERION DROP FOREIGN KEY FK_ABSTRACT_ANNOTATION_CRITERION_ANNOTATION_DEFINITION;
ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES DROP FOREIGN KEY FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_PERMISSIBLE;
ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES DROP FOREIGN KEY FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_SELECTED;
ALTER TABLE ARRAY_DATA DROP FOREIGN KEY FK_ARRAY_DATA_ARRAY;
ALTER TABLE IMAGE DROP FOREIGN KEY FK_IMAGE_IMAGE_SERIES;
ALTER TABLE IMAGE_SERIES DROP FOREIGN KEY FK_IMAGE_SERIES_IMAGE_STUDY;
ALTER TABLE MICROARRAY DROP FOREIGN KEY FK_ARRAY_PLATFORM;
ALTER TABLE QUERY DROP FOREIGN KEY FK_QUERY_STUDY_SUBSCRIPTION;
ALTER TABLE QUERY DROP FOREIGN KEY FK_QUERY_COMPUND_CRITERION;

ALTER TABLE SAMPLE DROP FOREIGN KEY FK_SAMPLE_GENOMIC_DATA_SOURCE;
ALTER TABLE SAMPLE_ACQUISITION DROP FOREIGN KEY FK_SAMPLE_ACQUISITION_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE SAMPLE_ACQUISITION DROP FOREIGN KEY FK_SAMPLE_ACQUISITION_TIMEPOINT;
ALTER TABLE STUDY_IMAGE_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_IMAGE_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_IMAGE_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_IMAGE_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SAMPLE_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_SAMPLE_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_SAMPLE_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_SAMPLE_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SUBJECT_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_CLINICAL_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_SUBJECT_ANNOTATIONS DROP FOREIGN KEY FK_STUDY_CLINICAL_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SUBJECT_ASSIGNMENT DROP FOREIGN KEY FK_STUDY_SUBJECT_ASSIGNMENT_STUDY;
ALTER TABLE STUDY_SUBJECT_ASSIGNMENT DROP FOREIGN KEY FK_STUDY_SUBJECT_ASSIGNMENT_SUBJECT;
ALTER TABLE STUDY_SUBSCRIPTION DROP FOREIGN KEY FK_STUDY_SUBSCRIPTION_STUDY;
ALTER TABLE STUDY_SUBSCRIPTION DROP FOREIGN KEY FK_STUDY_SUBSCRIPTION_USER_WORKSPACE;
ALTER TABLE SUBJECT_LIST_SUBJECT DROP FOREIGN KEY FK_SUBJECT_LIST_SUBJECT_SUBJECT;
ALTER TABLE SUBJECT_LIST_SUBJECT DROP FOREIGN KEY FK_SUBJECT_LIST_SUBJECT_SUBJECT_LIST;
ALTER TABLE TIMEPOINT DROP FOREIGN KEY FK_TIMEPOINT_STUDY;
ALTER TABLE STUDY DROP FOREIGN KEY FK_STUDY_TIMEPOINT;
ALTER TABLE USER_WORKSPACE DROP FOREIGN KEY FK_USER_WORKSPACE_STUDY_SUBSCRIPTION;
ALTER TABLE SURVIVAL_VALUE_DEFINITION DROP FOREIGN KEY FK_SURVIVAL_VALUE_DEFINITION_SURVIVAL_START_DATE;
ALTER TABLE SURVIVAL_VALUE_DEFINITION DROP FOREIGN KEY FK_SURVIVAL_VALUE_DEFINITION_DEATH_DATE;
ALTER TABLE SURVIVAL_VALUE_DEFINITION DROP FOREIGN KEY FK_SURVIVAL_VALUE_DEFINITION_LAST_FOLLOWUP_DATE;
ALTER TABLE SURVIVAL_VALUE_DEFINITION DROP FOREIGN KEY FK_SURVIVAL_VALUE_DEFINITION_STUDY;
ALTER TABLE ABSTRACT_PERMISSIBLE_VALUE DROP FOREIGN KEY FK_ABSTRACT_PERMISSIBLE_VALUE_VALUE_DOMAIN;
ALTER TABLE COMMON_DATA_ELEMENT DROP FOREIGN KEY FK_COMMON_DATA_ELEMENT_VALUE_DOMAIN;
ALTER TABLE SAMPLE DROP FOREIGN KEY FK_SAMPLE_STUDY;
ALTER TABLE RESULT_ROW DROP FOREIGN KEY RESULT_ROW_IMAGE_SERIES;
    
alter table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_ABSTRACT_CLINICAL_SOURCE_STUDY_CONFIGURATION;
alter table ANNOTATION_FIELD_DESCRIPTOR DROP FOREIGN KEY FK_ANNOTATION_FIELD_DESCRIPTOR_ANNOTATION_DEFINITION;
alter table ANNOTATION_FILE DROP FOREIGN KEY FK_ANNOTATION_FILE_TIMEPOINT_COLUMN;
alter table ANNOTATION_FILE DROP FOREIGN KEY FK_ANNOTATION_FILE_IDENTIFIER_COLUMN;
alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_DEL_TEXT_CLINICAL_ANNOTATION_FILE;
alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_DEL_TEXT_CLINICAL_ABSTRACT_CLINICAL_SOURCE;
alter table FILE_COLUMN DROP FOREIGN KEY FK_FILE_COLUMN_ANN_FIELD_DESC;
alter table FILE_COLUMN DROP FOREIGN KEY FK_FILE_COLUMN_ANNOTATION_FILE;
alter table GENOMIC_DATA_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_GENOMIC_DATA_SOURCE_SERVER_CONNECTION;
alter table GENOMIC_DATA_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_GENOMIC_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION;
alter table IMAGE_DATA_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_IMAGE_DATA_SOURCE_SERVER_CONNECTION;
alter table IMAGE_DATA_SOURCE_CONFIGURATION DROP FOREIGN KEY FK_IMAGE_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION;
alter table IMAGE_ANNOTATION_CONFIGURATION DROP FOREIGN KEY FK_IMAGE_ANNOTATION_CONFIGURATION_STUDY_CONFIGURATION;
alter table IMAGE_ANNOTATION_CONFIGURATION DROP FOREIGN KEY FK_IMAGE_ANNOTATION_CLINICAL_ANNOTATION_FILE;
alter table SAMPLE_IDENTIFIER DROP FOREIGN KEY FK_SAMPLE_IDENTIFIER_GENOMIC_DATA_SOURCE;
alter table STUDY_CONFIGURATION DROP FOREIGN KEY FK_STUDY_CONFIGURATION_STUDY_ID;
alter table STUDY_CONFIGURATION DROP FOREIGN KEY FK_STUDY_CONFIGURATION_STUDY_LOGO_ID;
ALTER TABLE ARRAY_DATA DROP FOREIGN KEY FK_ARRAY_DATA_STUDY;
ALTER TABLE GENE_PATTERN_ANALYSIS_JOB DROP FOREIGN KEY FK_GENE_PATTERN_JOB_STUDY_SUBSCRIPTION ;
ALTER TABLE COMPARATIVE_MARKER_SELECTION_ANALYSIS_JOB DROP FOREIGN KEY FK_COMPARATIVE_MARKER_SELECTION_JOB_STUDY_SUBSCRIPTION ;
ALTER TABLE MARKER_RESULT DROP FOREIGN KEY FK_MARKER_RESULT_CMS_JOBFK_MARKER_RESULT_CMS_JOB ;
alter table REPORTER_GENES DROP FOREIGN KEY FK_REPORTER_GENES_REPORTER;
alter table REPORTER_GENES DROP FOREIGN KEY FK_REPORTER_GENES_GENE;
alter table SEGMENT_DATA DROP FOREIGN KEY FK_SEGMENT_ARRAY_DATA;

DROP TABLE IF EXISTS VALUE_DOMAIN;
DROP TABLE IF EXISTS SURVIVAL_VALUE_DEFINITION;
DROP TABLE IF EXISTS GENOMIC_DATA_QUERY_RESULT;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_COLUMN;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_ROW;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_VALUE;
DROP TABLE IF EXISTS SAMPLE_LIST_SAMPLE;
DROP TABLE IF EXISTS SAMPLE_LIST;
DROP TABLE IF EXISTS GENE_LIST_GENE;
DROP TABLE IF EXISTS GENE_LIST;
DROP TABLE IF EXISTS GENE_CRITERION;
DROP TABLE IF EXISTS ABSTRACT_GENOMIC_CRITERION;
DROP TABLE IF EXISTS GENE_NAME_CRITERION;
DROP TABLE IF EXISTS GENE_LIST_CRITERION;
DROP TABLE IF EXISTS FOLD_CHANGE_CRITERION;
DROP TABLE IF EXISTS FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES;
DROP TABLE IF EXISTS SEGMENT_DATA;

DROP TABLE IF EXISTS SUBJECT_ANNOTATION;
DROP TABLE IF EXISTS IMAGE;
DROP TABLE IF EXISTS IMAGE_SERIES;
DROP TABLE IF EXISTS GENE_EXPRESSION_REPORTER;
DROP TABLE IF EXISTS ARRAY_DATA;
DROP TABLE IF EXISTS USER_WORKSPACE;
DROP TABLE IF EXISTS STUDY_SUBJECT_ANNOTATIONS;
DROP TABLE IF EXISTS STUDY_SAMPLE_ANNOTATIONS;
DROP TABLE IF EXISTS STUDY_IMAGE_ANNOTATIONS;
DROP TABLE IF EXISTS SAMPLE_ACQUISITION;
DROP TABLE IF EXISTS QUERY;
DROP TABLE IF EXISTS IMAGE_SERIES_ACQUISITION;
DROP TABLE IF EXISTS ABSTRACT_REPORTER;
DROP TABLE IF EXISTS REPORTER_GENES;
DROP TABLE IF EXISTS TIMEPOINT;
DROP TABLE IF EXISTS SUBJECT_LIST_SUBJECT;
DROP TABLE IF EXISTS SUBJECT_LIST;
DROP TABLE IF EXISTS STUDY_SUBSCRIPTION;
DROP TABLE IF EXISTS STUDY_SUBJECT_ASSIGNMENT;
DROP TABLE IF EXISTS REPORTER_LIST;
DROP TABLE IF EXISTS MICROARRAY_SAMPLES;
DROP TABLE IF EXISTS MICROARRAY;
DROP TABLE IF EXISTS ANNOTATION_DEFINITION;
DROP TABLE IF EXISTS SUBJECT;
DROP TABLE IF EXISTS STUDY;
DROP TABLE IF EXISTS SAMPLE;
DROP TABLE IF EXISTS PLATFORM;
DROP TABLE IF EXISTS GENE;
DROP TABLE IF EXISTS COMMON_DATA_ELEMENT;
DROP TABLE IF EXISTS ABSTRACT_LIST;

DROP TABLE IF EXISTS ABSTRACT_ANNOTATION_VALUE;
DROP TABLE IF EXISTS NUMERIC_ANNOTATION_VALUE;
DROP TABLE IF EXISTS STRING_ANNOTATION_VALUE;
DROP TABLE IF EXISTS DATE_ANNOTATION_VALUE;
DROP TABLE IF EXISTS ABSTRACT_PERMISSIBLE_VALUE;
DROP TABLE IF EXISTS NUMERIC_PERMISSIBLE_VALUE;
DROP TABLE IF EXISTS STRING_PERMISSIBLE_VALUE;
DROP TABLE IF EXISTS DATE_PERMISSIBLE_VALUE;
DROP TABLE IF EXISTS RESULT_COLUMN;
DROP TABLE IF EXISTS RESULT_ROW;
DROP TABLE IF EXISTS RESULT_VALUE;
DROP TABLE IF EXISTS QUERY_RESULT;
DROP TABLE IF EXISTS ABSTRACT_CRITERION;
DROP TABLE IF EXISTS COMPOUND_CRITERION;
DROP TABLE IF EXISTS ABSTRACT_ANNOTATION_CRITERION;
DROP TABLE IF EXISTS STRING_COMPARISON_CRITERION;
DROP TABLE IF EXISTS NUMERIC_COMPARISON_CRITERION;
DROP TABLE IF EXISTS DATE_COMPARISON_CRITERION;
DROP TABLE IF EXISTS SELECTED_VALUE_CRITERION;
DROP TABLE IF EXISTS IDENTIFIER_CRITERION;
DROP TABLE IF EXISTS SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES;

DROP TABLE IF EXISTS ABSTRACT_CLINICAL_SOURCE_CONFIGURATION;
DROP TABLE IF EXISTS ANNOTATION_FIELD_DESCRIPTOR;
DROP TABLE IF EXISTS ANNOTATION_FILE;
DROP TABLE IF EXISTS CONFIGURATION_PARAMETER;
DROP TABLE IF EXISTS DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION;
DROP TABLE IF EXISTS FILE_COLUMN;
DROP TABLE IF EXISTS GENOMIC_DATA_SOURCE_CONFIGURATION;
DROP TABLE IF EXISTS GENOMIC_DATA_SOURCE_SAMPLES;
DROP TABLE IF EXISTS IMAGE_ANNOTATION_CONFIGURATION;
DROP TABLE IF EXISTS IMAGE_DATA_SOURCE_CONFIGURATION;
DROP TABLE IF EXISTS SAMPLE_IDENTIFIER;
DROP TABLE IF EXISTS SERVER_CONNECTION_PROFILE;
DROP TABLE IF EXISTS STUDY_CONFIGURATION;
DROP TABLE IF EXISTS STUDY_LOGO;
DROP TABLE IF EXISTS DNA_ANALYSIS_REPORTER;
DROP TABLE IF EXISTS GENE_PATTERN_ANALYSIS_JOB;
DROP TABLE IF EXISTS COMPARATIVE_MARKER_SELECTION_ANALYSIS_JOB;
DROP TABLE IF EXISTS MARKER_RESULT;

