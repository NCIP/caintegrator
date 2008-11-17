ALTER TABLE ABSTRACT_LIST DROP CONSTRAINT FK_ABSTRACT_LIST_STUDY_SUBSCRIPTION;
ALTER TABLE SAMPLE DROP CONSTRAINT SAMPLE_SAMPLE_ACQUISITION;
ALTER TABLE GENOMIC_DATA_QUERY_RESULT DROP CONSTRAINT FK_GENOMIC_DATA_QUERY_RESULT_QUERY;
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_COLUMN_SAMPLE_ACQ;
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_COLUMN_GENOMIC_QUERY_RESULT;
ALTER TABLE GENOMIC_DATA_RESULT_ROW DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_ROW_ABSTRACT_REPORTER;
ALTER TABLE GENOMIC_DATA_RESULT_ROW DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_ROW_GENOMIC_QUERY_RESULT;
ALTER TABLE GENOMIC_DATA_RESULT_VALUE DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_VALUE_RESULT_ROW;
ALTER TABLE GENOMIC_DATA_RESULT_VALUE DROP CONSTRAINT FK_GENOMIC_DATA_RESULT_VALUE_RESULT_COLUMN;
ALTER TABLE SAMPLE_LIST_SAMPLE DROP CONSTRAINT FK_SAMPLE_LIST_SAMPLE_SAMPLE_LIST;
ALTER TABLE SAMPLE_LIST_SAMPLE DROP CONSTRAINT FK_SAMPLE_LIST_SAMPLE_SAMPLE;
ALTER TABLE GENE_LIST_GENE DROP CONSTRAINT FK_GENE_LIST_GENE_GENE_LIST;
ALTER TABLE GENE_LIST_GENE DROP CONSTRAINT FK_GENE_LIST_GENE_GENE;
ALTER TABLE GENE_CRITERION DROP CONSTRAINT FK_GENE_CRITERION_GENE;
ALTER TABLE GENE_LIST_CRITERION DROP CONSTRAINT FK_GENE_LIST_CRITERION_GENE_LIST; 
ALTER TABLE FOLD_CHANGE_CRITERION DROP CONSTRAINT FK_FOLD_CHANGE_CRITERION_SAMPLE_LIST;

ALTER TABLE ABSTRACT_REPORTER DROP CONSTRAINT FK_ABSTRACT_REPORTER_PLATFORM;
ALTER TABLE ANALYSIS_JOB_CONFIG DROP CONSTRAINT FK_ANALYSIS_JOB_CONFIG_ANALYSIS_METHOD;
ALTER TABLE ANALYSIS_JOB_CONFIG DROP CONSTRAINT FK_ANALYSIS_JOB_CONFIG_STUDY_SUBSCRIPTION;
ALTER TABLE SUBJECT_ANNOTATION DROP CONSTRAINT FK_SUBJECT_ANNOTATION_ANNOTATION_VALUE; 
ALTER TABLE SUBJECT_ANNOTATION DROP CONSTRAINT FK_SUBJECT_ANNOTATION_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE SUBJECT_ANNOTATION DROP CONSTRAINT FK_SUBJECT_ANNOTATION_TIMEPOINT;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_IMAGE_SERIES; 
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_SAMPLE_ACQUISITION;
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_IMAGE; 
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_ABSTRACT_PERMISSIBLE_VALUE; 
ALTER TABLE ABSTRACT_ANNOTATION_VALUE DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_DEFINITION; 
ALTER TABLE ABSTRACT_PERMISSIBLE_VALUE DROP CONSTRAINT FK_ABSTRACT_PERMISSIBLE_VALUE_ANNOTATION_DEFINITION ;
ALTER TABLE RESULT_COLUMN DROP CONSTRAINT FK_RESULT_COLUMN_QUERY ;
ALTER TABLE RESULT_COLUMN DROP CONSTRAINT FK_RESULT_COLUMN_ANNOTATION_DEFINITION;
ALTER TABLE RESULT_ROW DROP CONSTRAINT RESULT_ROW_QUERY_RESULT;
ALTER TABLE RESULT_ROW DROP CONSTRAINT RESULT_ROW_SAMPLE_ACQUISITION ;
ALTER TABLE RESULT_ROW DROP CONSTRAINT RESULT_ROW_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE RESULT_ROW DROP CONSTRAINT RESULT_ROW_IMAGE_SERIES_ACQUISITION;
ALTER TABLE RESULT_VALUE DROP CONSTRAINT FK_RESULT_VALUE_ANNOTATION_VALUE ;
ALTER TABLE RESULT_VALUE DROP CONSTRAINT FK_RESULT_VALUE_RESULT_COLUMN; 
ALTER TABLE RESULT_VALUE DROP CONSTRAINT FK_RESULT_VALUE_RESULT_ROW; 
ALTER TABLE QUERY_RESULT DROP CONSTRAINT FK_QUERY_RESULT_QUERY; 
ALTER TABLE ABSTRACT_CRITERION DROP CONSTRAINT FK_ABSTRACT_CRITERION_COUMPOUND_CRITERION; 
ALTER TABLE ABSTRACT_ANNOTATION_CRITERION DROP CONSTRAINT FK_ABSTRACT_ANNOTATION_CRITERION_ANNOTATION_DEFINITION; 
ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES DROP CONSTRAINT FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_PERMISSIBLE; 
ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES DROP CONSTRAINT FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_SELECTED; 
ALTER TABLE ARRAY_DATA DROP CONSTRAINT FK_ARRAY_DATA_ARRAY;
ALTER TABLE ARRAY_DATA DROP CONSTRAINT FK_ARRAY_DATA_ARRAY_DATA_MATRIX;
ALTER TABLE ARRAY_DATA_MATRIX_REPORTERS DROP CONSTRAINT FK_ARRAY_DATA_MATRIX_REPORTERS_ABSTRACT_REPORTER;
ALTER TABLE ARRAY_DATA_MATRIX_REPORTERS DROP CONSTRAINT FK_ARRAY_DATA_MATRIX_REPORTERS_ARRAY_DATA_MATRIX;
ALTER TABLE GENE_EXPRESSION_REPORTER DROP CONSTRAINT FK_GENE_EXPRESSION_REPORTER_GENE;
ALTER TABLE IMAGE DROP CONSTRAINT FK_IMAGE_IMAGE_SERIES;
ALTER TABLE IMAGE_SERIES DROP CONSTRAINT FK_IMAGE_SERIES_IMAGE_STUDY;
ALTER TABLE IMAGE_STUDY DROP CONSTRAINT FK_IMAGE_STUDY_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE MICROARRAY DROP CONSTRAINT FK_ARRAY_PLATFORM;
ALTER TABLE MICROARRAY DROP CONSTRAINT FK_ARRAY_SAMPLE;
ALTER TABLE QUERY DROP CONSTRAINT FK_QUERY_STUDY_SUBSCRIPTION;
ALTER TABLE QUERY DROP CONSTRAINT FK_QUERY_COMPUND_CRITERION; 
ALTER TABLE SAMPLE DROP CONSTRAINT FK_SAMPLE_SAMPLE_ACQUISITION;
ALTER TABLE SAMPLE_ACQUISITION DROP CONSTRAINT FK_SAMPLE_ACQUISITION_STUDY_SUBJECT_ASSIGNMENT;
ALTER TABLE SAMPLE_ACQUISITION DROP CONSTRAINT FK_SAMPLE_ACQUISITION_TIMEPOINT;
ALTER TABLE STUDY_IMAGE_ANNOTATIONS DROP CONSTRAINT FK_STUDY_IMAGE_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_IMAGE_ANNOTATIONS DROP CONSTRAINT FK_STUDY_IMAGE_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SAMPLE_ANNOTATIONS DROP CONSTRAINT FK_STUDY_SAMPLE_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_SAMPLE_ANNOTATIONS DROP CONSTRAINT FK_STUDY_SAMPLE_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SUBJECT_ANNOTATIONS DROP CONSTRAINT FK_STUDY_CLINICAL_ANNOTATIONS_ANNOTATION_DEFINITION;
ALTER TABLE STUDY_SUBJECT_ANNOTATIONS DROP CONSTRAINT FK_STUDY_CLINICAL_ANNOTATIONS_STUDY;
ALTER TABLE STUDY_SUBJECT_ASSIGNMENT DROP CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_STUDY;
ALTER TABLE STUDY_SUBJECT_ASSIGNMENT DROP CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_SUBJECT;
ALTER TABLE STUDY_SUBSCRIPTION DROP CONSTRAINT FK_STUDY_SUBSCRIPTION_STUDY;
ALTER TABLE STUDY_SUBSCRIPTION DROP CONSTRAINT FK_STUDY_SUBSCRIPTION_USER_WORKSPACE;
ALTER TABLE SUBJECT_LIST_SUBJECT DROP CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT;
ALTER TABLE SUBJECT_LIST_SUBJECT DROP CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT_LIST;
ALTER TABLE TIMEPOINT DROP CONSTRAINT FK_TIMEPOINT_STUDY;
ALTER TABLE STUDY DROP CONSTRAINT FK_STUDY_TIMEPOINT;
ALTER TABLE USER_WORKSPACE DROP CONSTRAINT FK_USER_WORKSPACE_STUDY_SUBSCRIPTION;


DROP TABLE IF EXISTS GENOMIC_DATA_QUERY_RESULT;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_COLUMN;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_ROW;
DROP TABLE IF EXISTS GENOMIC_DATA_RESULT_VALUE;
DROP TABLE IF EXISTS SAMPLE_LIST_SAMPLE;
DROP TABLE IF EXISTS GENE_LIST_GENE;
DROP TABLE IF EXISTS GENE_CRITERION;
DROP TABLE IF EXISTS GENE_NAME_CRITERION;
DROP TABLE IF EXISTS GENE_LIST_CRITERION;
DROP TABLE IF EXISTS FOLD_CHANGE_CRITERION;

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
DROP TABLE IF EXISTS ARRAY_DATA_MATRIX;
DROP TABLE IF EXISTS ABSTRACT_REPORTER;
DROP TABLE IF EXISTS TIMEPOINT;
DROP TABLE IF EXISTS SUBJECT_LIST_SUBJECT;
DROP TABLE IF EXISTS SUBJECT_LIST;
DROP TABLE IF EXISTS STUDY_SUBSCRIPTION;
DROP TABLE IF EXISTS STUDY_SUBJECT_ASSIGNMENT;
DROP TABLE IF EXISTS REPORTER_SET;
DROP TABLE IF EXISTS MICROARRAY_SAMPLES;
DROP TABLE IF EXISTS MICROARRAY;
DROP TABLE IF EXISTS ANNOTATION_DEFINITION;
DROP TABLE IF EXISTS ANALYSIS_JOB_CONFIG;
DROP TABLE IF EXISTS SUBJECT;
DROP TABLE IF EXISTS STUDY;
DROP TABLE IF EXISTS SAMPLE;
DROP TABLE IF EXISTS PLATFORM;
DROP TABLE IF EXISTS GENE;
DROP TABLE IF EXISTS COMMON_DATA_ELEMENT;
DROP TABLE IF EXISTS ANALYSIS_METHOD;
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
DROP TABLE IF EXISTS SELECTED_VALUE_CRITERION;
DROP TABLE IF EXISTS SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES;