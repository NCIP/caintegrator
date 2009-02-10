CREATE TABLE ABSTRACT_GENOMIC_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE GENE_NAME_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    GENE_SYMBOL varchar(55), 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE FOLD_CHANGE_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    FOLDS_UP FLOAT, 
    FOLDS_DOWN FLOAT, 
    GENE_SYMBOL varchar(255),
    REGULATION_TYPE varchar(255), 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES 
(
    FOLD_CHANGE_CRITERION_ID bigint not null, 
    SAMPLE_ID bigint not null,
    PRIMARY KEY (FOLD_CHANGE_CRITERION_ID, SAMPLE_ID)
) TYPE=InnoDB;


create table GENE_LIST 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    primary key (ID)
) TYPE=InnoDB;

create table GENE_LIST_GENE 
(
    GENE_LIST_ID bigint not null, 
    GENE_ID bigint not null
) TYPE=InnoDB;

create table SAMPLE_LIST 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    primary key (ID)
) TYPE=InnoDB;


create table SAMPLE_LIST_SAMPLE 
(
    SAMPLE_LIST_ID bigint not null, 
    SAMPLE_ID bigint not null
) TYPE=InnoDB;

create table GENOMIC_DATA_QUERY_RESULT 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    QUERY_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

create table GENOMIC_DATA_RESULT_COLUMN 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    COLUMN_INDEX integer, 
    SAMPLE_ACQUISITION_ID bigint, 
    GENOMIC_DATA_QUERY_RESULT_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

create table GENOMIC_DATA_RESULT_ROW 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    ROW_INDEX integer, 
    ABSTRACT_REPORTER_ID bigint, 
    GENOMIC_DATA_QUERY_RESULT_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

create table GENOMIC_DATA_RESULT_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    VALUE float, 
    GENOMIC_DATA_RESULT_COLUMN_ID bigint, 
    GENOMIC_DATA_RESULT_ROW_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;






CREATE TABLE SUBJECT_ANNOTATION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    STUDY_SUBJECT_ASSIGNMENT_ID bigint, 
    TIMEPOINT_ID bigint, 
    ANNOTATION_VALUE_ID bigint unique, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_ANNOTATION_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ANNOTATION_DEFINITION_ID bigint, 
    ABSTRACT_PERMISSIBLE_VALUE_ID bigint, 
    SAMPLE_ACQUISITION_ID bigint, 
    IMAGE_SERIES_ID bigint, 
    IMAGE_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_ANNOTATION_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NUMERIC_VALUE double precision, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE STRING_ANNOTATION_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    STRING_VALUE varchar(255), 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE DATE_ANNOTATION_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    DATE_VALUE datetime,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE ABSTRACT_PERMISSIBLE_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ANNOTATION_DEFINITION_ID bigint, 
    VALUE_DOMAIN_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_PERMISSIBLE_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    HIGH_VALUE double precision, 
    IS_RANGE_VALUE integer, 
    LOW_VALUE double precision, 
    NUMERIC_VALUE double precision, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE STRING_PERMISSIBLE_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    STRING_VALUE varchar(255),
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE DATE_PERMISSIBLE_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    DATE_VALUE datetime, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE RESULT_COLUMN 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    COLUMN_INDEX integer, 
    ENTITY_TYPE varchar(255), 
    SORT_ORDER integer, 
    SORT_TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint, 
    QUERY_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE RESULT_ROW 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ROW_INDEX integer, 
    SAMPLE_ACQUISITION_ID bigint, 
    STUDY_SUBJECT_ASSIGNMENT_ID bigint, 
    IMAGE_SERIES_ID bigint, 
    QUERY_RESULT_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE RESULT_VALUE 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    RESULT_COLUMN_ID bigint, 
    ANNOTATION_VALUE_ID bigint, 
    RESULT_ROW_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE QUERY_RESULT
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    QUERY_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    COMPOUND_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE COMPOUND_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    BOOLEAN_OPERATOR varchar(255),
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE ABSTRACT_ANNOTATION_CRITERION 
(
   ID BIGINT NOT NULL AUTO_INCREMENT,
    ENTITY_TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE STRING_COMPARISON_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    STRING_VALUE varchar(255),
    WILD_CARD_TYPE varchar(50),
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_COMPARISON_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NUMERIC_COMPARISON_OPERATOR varchar(255), 
    NUMERIC_VALUE double precision,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE SELECTED_VALUE_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE IDENTIFIER_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES 
(
    ABSTRACT_PERMISSIBLE_VALUE_ID bigint not null, 
    SELECTED_VALUE_CRITERION_ID bigint not null,
    PRIMARY KEY (ABSTRACT_PERMISSIBLE_VALUE_ID, SELECTED_VALUE_CRITERION_ID)
) TYPE=InnoDB;


CREATE TABLE IMAGE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IMAGE_SERIES_ID BIGINT,
    IDENTIFIER varchar(200),
	PRIMARY KEY (ID),
	KEY (IMAGE_SERIES_ID)
) TYPE=InnoDB;


CREATE TABLE IMAGE_SERIES
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IMAGE_STUDY_ID BIGINT,
    IDENTIFIER varchar(200),
	PRIMARY KEY (ID),
	KEY (IMAGE_STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE GENE_EXPRESSION_REPORTER
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	GENE_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (GENE_ID)
) TYPE=InnoDB;


CREATE TABLE ARRAY_DATA
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	ARRAY_ID BIGINT,
	ARRAY_DATA_MATRIX_ID BIGINT,
	SAMPLE_ID BIGINT,
	STUDY_ID BIGINT,
	REPORTER_SET_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ARRAY_ID),
	KEY (ARRAY_DATA_MATRIX_ID),
	KEY (REPORTER_SET_ID),
	KEY (SAMPLE_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE USER_WORKSPACE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	USERNAME VARCHAR(100),
	DEFAULT_STUDY_SUBSCRIPTION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (DEFAULT_STUDY_SUBSCRIPTION_ID)
) TYPE=InnoDB;


CREATE TABLE STUDY_SUBJECT_ANNOTATIONS
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	ANNOTATION_DEFINITION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ANNOTATION_DEFINITION_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE STUDY_SAMPLE_ANNOTATIONS
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	ANNOTATION_DEFINITION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ANNOTATION_DEFINITION_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE STUDY_IMAGE_ANNOTATIONS
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	ANNOTATION_DEFINITION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ANNOTATION_DEFINITION_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE SAMPLE_ACQUISITION
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_SUBJECT_ASSIGN_ID BIGINT,
	TIMEPOINT_ID BIGINT,
	SAMPLE_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (SAMPLE_ID),
	KEY (STUDY_SUBJECT_ASSIGN_ID),
	KEY (TIMEPOINT_ID)
) TYPE=InnoDB;


CREATE TABLE QUERY
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION VARCHAR(200),
	NAME VARCHAR(100),
	VISIBILITY VARCHAR(20),
    RESULT_TYPE varchar(70),
    REPORTER_TYPE varchar(70),
	COMPOUND_CRITERION_ID bigint unique,
	STUDY_SUBSCRIPTION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (STUDY_SUBSCRIPTION_ID)
) TYPE=InnoDB;


CREATE TABLE IMAGE_SERIES_ACQUISITION
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
    IDENTIFIER varchar(200),
    NCIA_TRIAL_IDENTIFIER varchar(200),
	STUDY_SUBJECT_ASSIGN_ID BIGINT,
	TIMEPOINT_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (TIMEPOINT_ID),
	KEY (STUDY_SUBJECT_ASSIGN_ID)
) TYPE=InnoDB;


CREATE TABLE ARRAY_DATA_MATRIX
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	REPORTER_SET_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (REPORTER_SET_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_REPORTER
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(2048),
	REPORTER_SET_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (REPORTER_SET_ID)
) TYPE=InnoDB;


CREATE TABLE TIMEPOINT
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	NAME VARCHAR(50),
	DESCRIPTION VARCHAR(200),
	START_DATE TIMESTAMP,
	END_DATE TIMESTAMP,
	PRIMARY KEY (ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE SUBJECT_LIST_SUBJECT
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	SUBJECT_LIST_ID BIGINT,
	SUBJECT_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (SUBJECT_ID),
	KEY (SUBJECT_LIST_ID)
) TYPE=InnoDB;


CREATE TABLE SUBJECT_LIST
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE STUDY_SUBSCRIPTION
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	USER_WORKSPACE_ID BIGINT,
	STUDY_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (STUDY_ID),
	KEY (USER_WORKSPACE_ID)
) TYPE=InnoDB;


CREATE TABLE STUDY_SUBJECT_ASSIGNMENT
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_ID BIGINT,
	SUBJECT_ID BIGINT,
	IDENTIFIER VARCHAR(50),
	PRIMARY KEY (ID),
	KEY (STUDY_ID),
	KEY (SUBJECT_ID)
) TYPE=InnoDB;


CREATE TABLE REPORTER_SET
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	REPORTER_TYPE VARCHAR(50),
	PLATFORM_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (PLATFORM_ID)
) TYPE=InnoDB;


CREATE TABLE MICROARRAY_SAMPLES
(
	MICROARRAY_ID BIGINT NOT NULL,
	SAMPLE_ID BIGINT NOT NULL,
	PRIMARY KEY (MICROARRAY_ID, SAMPLE_ID),
	KEY (MICROARRAY_ID),
	KEY (SAMPLE_ID)
) TYPE=InnoDB;


CREATE TABLE MICROARRAY
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PLATFORM_ID BIGINT,
	SAMPLE_ID BIGINT,
	NAME VARCHAR(500),
	PRIMARY KEY (ID),
	KEY (PLATFORM_ID)
) TYPE=InnoDB;


CREATE TABLE ANNOTATION_DEFINITION
(
	DISPLAY_NAME VARCHAR(255),
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PREFERRED_DEFINITION VARCHAR(1000),
	KEYWORDS VARCHAR(255),
	COMMON_DATA_ELEMENT_ID BIGINT,
	UNITS_OF_MEASURE VARCHAR(50),
	TYPE VARCHAR(50),
	PRIMARY KEY (ID),
	KEY (COMMON_DATA_ELEMENT_ID)
) TYPE=InnoDB;


CREATE TABLE ANALYSIS_JOB_CONFIG
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STUDY_SUBSCRIPTION_ID BIGINT,
	ANALYSIS_METHOD_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ANALYSIS_METHOD_ID),
	KEY (STUDY_SUBSCRIPTION_ID)
) TYPE=InnoDB;


CREATE TABLE SUBJECT
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE STUDY
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	LONG_TITLE_TEXT VARCHAR(200),
	SHORT_TITLE_TEXT VARCHAR(50),
	DEFAULT_TIMEPOINT_ID bigint,
	PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE UNIQUE INDEX UNIQUE_TITLE ON STUDY(SHORT_TITLE_TEXT);

CREATE TABLE SAMPLE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(100),
	SAMPLE_ACQUISITION_ID bigint,
    CONTROL_FOR_STUDY_ID BIGINT,
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE PLATFORM
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(50),
	VENDOR VARCHAR(50),
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE GENE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	SYMBOL VARCHAR(2048),
	FULL_NAME VARCHAR(1024),
	GENBANK_ACCESSION VARCHAR(1024),
	GENBANK_ACCESSION_VERSION VARCHAR(50),
	ENSEMBLE_GENE_ID VARCHAR(2048),
	UNIGENE_CLUSTER_ID VARCHAR(1024),
	ENTREZ_GENE_ID VARCHAR(2048),
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE COMMON_DATA_ELEMENT
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
	PUBLIC_ID BIGINT,
	VERSION VARCHAR(20),
	LONG_NAME VARCHAR(255),
	PREFERRED_NAME VARCHAR(255),
	CONTEXT_NAME VARCHAR(100),
	DEFINITION VARCHAR(1024),
	REGISTRATION_STATUS VARCHAR(100),
	TYPE VARCHAR(100),
	WORKFLOW_STATUS VARCHAR(100),
	VALUE_DOMAIN_ID BIGINT,
	PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE TABLE VALUE_DOMAIN
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    PUBLIC_ID BIGINT,
    LONG_NAME VARCHAR(255),
    DATA_TYPE VARCHAR(100),
    VALUE_DOMAIN_TYPE VARCHAR(100),
    PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE ANALYSIS_METHOD
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION VARCHAR(200),
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_LIST
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION VARCHAR(200),
	NAME VARCHAR(100),
	VISIBILITY VARCHAR(20),
	STUDY_SUBSCRIPTION_ID bigint,
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE SURVIVAL_VALUE_DEFINITION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(200),
    SURVIVAL_VALUE_TYPE VARCHAR(200),
    STUDY_ID bigint,
    SURVIVAL_START_DATE_ID bigint, 
    DEATH_DATE_ID bigint, 
    LAST_FOLLOWUP_DATE_ID bigint,  
    primary key (ID)
) TYPE=InnoDB;


ALTER TABLE SURVIVAL_VALUE_DEFINITION ADD CONSTRAINT FK_SURVIVAL_VALUE_DEFINITION_SURVIVAL_START_DATE
    FOREIGN KEY (SURVIVAL_START_DATE_ID) references ANNOTATION_DEFINITION (ID);
    
ALTER TABLE SURVIVAL_VALUE_DEFINITION ADD CONSTRAINT FK_SURVIVAL_VALUE_DEFINITION_DEATH_DATE
    FOREIGN KEY (DEATH_DATE_ID) references ANNOTATION_DEFINITION (ID);
    
ALTER TABLE SURVIVAL_VALUE_DEFINITION ADD CONSTRAINT FK_SURVIVAL_VALUE_DEFINITION_LAST_FOLLOWUP_DATE
    FOREIGN KEY (LAST_FOLLOWUP_DATE_ID) references ANNOTATION_DEFINITION (ID);
    
ALTER TABLE SURVIVAL_VALUE_DEFINITION ADD CONSTRAINT FK_SURVIVAL_VALUE_DEFINITION_STUDY
    FOREIGN KEY (STUDY_ID) references STUDY (ID);



ALTER TABLE ABSTRACT_LIST ADD CONSTRAINT FK_ABSTRACT_LIST_STUDY_SUBSCRIPTION 
    FOREIGN KEY (STUDY_SUBSCRIPTION_ID) references STUDY_SUBSCRIPTION (ID);

ALTER TABLE SAMPLE ADD CONSTRAINT SAMPLE_SAMPLE_ACQUISITION 
    FOREIGN KEY (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);

ALTER TABLE GENOMIC_DATA_QUERY_RESULT ADD CONSTRAINT FK_GENOMIC_DATA_QUERY_RESULT_QUERY 
    FOREIGN KEY (QUERY_ID) references QUERY (ID);
    
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_COLUMN_SAMPLE_ACQ 
    FOREIGN KEY (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);
ALTER TABLE GENOMIC_DATA_RESULT_COLUMN ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_COLUMN_GENOMIC_QUERY_RESULT 
    FOREIGN KEY (GENOMIC_DATA_QUERY_RESULT_ID) references GENOMIC_DATA_QUERY_RESULT (ID);
    
ALTER TABLE GENOMIC_DATA_RESULT_ROW ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_ROW_ABSTRACT_REPORTER 
    FOREIGN KEY (ABSTRACT_REPORTER_ID) references ABSTRACT_REPORTER (ID);
    
ALTER TABLE GENOMIC_DATA_RESULT_ROW ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_ROW_GENOMIC_QUERY_RESULT 
    FOREIGN KEY (GENOMIC_DATA_QUERY_RESULT_ID) references GENOMIC_DATA_QUERY_RESULT (ID);
    
ALTER TABLE GENOMIC_DATA_RESULT_VALUE ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_VALUE_RESULT_ROW 
    FOREIGN KEY (GENOMIC_DATA_RESULT_ROW_ID) references GENOMIC_DATA_RESULT_ROW (ID);
    
ALTER TABLE GENOMIC_DATA_RESULT_VALUE ADD CONSTRAINT FK_GENOMIC_DATA_RESULT_VALUE_RESULT_COLUMN 
    FOREIGN KEY (GENOMIC_DATA_RESULT_COLUMN_ID) references GENOMIC_DATA_RESULT_COLUMN (ID);

ALTER TABLE SAMPLE_LIST_SAMPLE ADD CONSTRAINT FK_SAMPLE_LIST_SAMPLE_SAMPLE_LIST 
    FOREIGN KEY (SAMPLE_LIST_ID) references SAMPLE_LIST (ID);
    
ALTER TABLE SAMPLE_LIST_SAMPLE ADD CONSTRAINT FK_SAMPLE_LIST_SAMPLE_SAMPLE 
    FOREIGN KEY (SAMPLE_ID) references SAMPLE (ID);

ALTER TABLE GENE_LIST_GENE ADD CONSTRAINT FK_GENE_LIST_GENE_GENE_LIST 
    FOREIGN KEY (GENE_LIST_ID) references GENE_LIST (ID);

ALTER TABLE GENE_LIST_GENE ADD CONSTRAINT FK_GENE_LIST_GENE_GENE 
    FOREIGN KEY (GENE_ID) references GENE (ID);


ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_ANNOTATION_VALUE 
	FOREIGN KEY (ANNOTATION_VALUE_ID) REFERENCES ABSTRACT_ANNOTATION_VALUE (ID);

ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGNMENT_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_IMAGE_SERIES 
    FOREIGN KEY (IMAGE_SERIES_ID) references IMAGE_SERIES (ID);
ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_SAMPLE_ACQUISITION
    FOREIGN KEY (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);
ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_IMAGE 
    FOREIGN KEY (IMAGE_ID) references IMAGE (ID);
ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_ABSTRACT_PERMISSIBLE_VALUE 
    FOREIGN KEY (ABSTRACT_PERMISSIBLE_VALUE_ID) references ABSTRACT_PERMISSIBLE_VALUE (ID);
ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_DEFINITION 
    FOREIGN KEY (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

ALTER TABLE ABSTRACT_PERMISSIBLE_VALUE ADD CONSTRAINT FK_ABSTRACT_PERMISSIBLE_VALUE_ANNOTATION_DEFINITION 
    FOREIGN KEY (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);
    
ALTER TABLE ABSTRACT_PERMISSIBLE_VALUE ADD CONSTRAINT FK_ABSTRACT_PERMISSIBLE_VALUE_VALUE_DOMAIN 
    FOREIGN KEY (VALUE_DOMAIN_ID) references VALUE_DOMAIN (ID);
    
ALTER TABLE COMMON_DATA_ELEMENT ADD CONSTRAINT FK_COMMON_DATA_ELEMENT_VALUE_DOMAIN 
    FOREIGN KEY (VALUE_DOMAIN_ID) REFERENCES VALUE_DOMAIN (ID);

ALTER TABLE RESULT_COLUMN ADD CONSTRAINT FK_RESULT_COLUMN_QUERY 
    FOREIGN KEY (QUERY_ID) references QUERY (ID);
ALTER TABLE RESULT_COLUMN ADD CONSTRAINT FK_RESULT_COLUMN_ANNOTATION_DEFINITION 
    FOREIGN KEY (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);
    
ALTER TABLE RESULT_ROW ADD CONSTRAINT RESULT_ROW_QUERY_RESULT
    FOREIGN KEY (QUERY_RESULT_ID) references QUERY_RESULT (ID);
ALTER TABLE RESULT_ROW ADD CONSTRAINT RESULT_ROW_SAMPLE_ACQUISITION 
    FOREIGN KEY (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);
ALTER TABLE RESULT_ROW ADD CONSTRAINT RESULT_ROW_STUDY_SUBJECT_ASSIGNMENT 
    FOREIGN KEY (STUDY_SUBJECT_ASSIGNMENT_ID) references STUDY_SUBJECT_ASSIGNMENT (ID);
ALTER TABLE RESULT_ROW ADD CONSTRAINT RESULT_ROW_IMAGE_SERIES
    FOREIGN KEY (IMAGE_SERIES_ID) references IMAGE_SERIES (ID);
    
ALTER TABLE RESULT_VALUE ADD CONSTRAINT FK_RESULT_VALUE_ANNOTATION_VALUE 
    FOREIGN KEY (ANNOTATION_VALUE_ID) references ABSTRACT_ANNOTATION_VALUE (ID);
ALTER TABLE RESULT_VALUE ADD CONSTRAINT FK_RESULT_VALUE_RESULT_COLUMN 
    FOREIGN KEY (RESULT_COLUMN_ID) references RESULT_COLUMN (ID);
ALTER TABLE RESULT_VALUE ADD CONSTRAINT FK_RESULT_VALUE_RESULT_ROW 
    FOREIGN KEY (RESULT_ROW_ID) references RESULT_ROW (ID);

ALTER TABLE QUERY_RESULT ADD CONSTRAINT FK_QUERY_RESULT_QUERY 
    FOREIGN KEY (QUERY_ID) references QUERY (ID);

ALTER TABLE ABSTRACT_CRITERION ADD CONSTRAINT FK_ABSTRACT_CRITERION_COUMPOUND_CRITERION 
    FOREIGN KEY (COMPOUND_CRITERION_ID) references COMPOUND_CRITERION (ID);

ALTER TABLE ABSTRACT_ANNOTATION_CRITERION ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_CRITERION_ANNOTATION_DEFINITION 
    FOREIGN KEY (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES ADD CONSTRAINT FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_SELECTED 
    FOREIGN KEY (SELECTED_VALUE_CRITERION_ID) references SELECTED_VALUE_CRITERION (ID);
ALTER TABLE SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES ADD CONSTRAINT FK_SELECTED_VALUE_CRITERIA_PERMISSIBLE_VALUES_PERMISSIBLE 
    FOREIGN KEY (ABSTRACT_PERMISSIBLE_VALUE_ID) references ABSTRACT_PERMISSIBLE_VALUE (ID);

ALTER TABLE FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES ADD CONSTRAINT FK_FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES_CRITERION
    FOREIGN KEY (FOLD_CHANGE_CRITERION_ID) references FOLD_CHANGE_CRITERION (ID);

ALTER TABLE FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES ADD CONSTRAINT FK_FOLD_CHANGE_CRITERION_COMPARE_TO_SAMPLES_SAMPLE 
    FOREIGN KEY (SAMPLE_ID) references SAMPLE (ID);

ALTER TABLE IMAGE ADD CONSTRAINT FK_IMAGE_IMAGE_SERIES 
	FOREIGN KEY (IMAGE_SERIES_ID) REFERENCES IMAGE_SERIES (ID);

ALTER TABLE IMAGE_SERIES ADD CONSTRAINT FK_IMAGE_SERIES_IMAGE_STUDY 
	FOREIGN KEY (IMAGE_STUDY_ID) REFERENCES IMAGE_SERIES_ACQUISITION (ID);


ALTER TABLE GENE_EXPRESSION_REPORTER ADD CONSTRAINT FK_GENE_EXPRESSION_REPORTER_GENE 
	FOREIGN KEY (GENE_ID) REFERENCES GENE (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_ARRAY 
	FOREIGN KEY (ARRAY_ID) REFERENCES MICROARRAY (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_ARRAY_DATA_MATRIX 
	FOREIGN KEY (ARRAY_DATA_MATRIX_ID) REFERENCES ARRAY_DATA_MATRIX (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_REPORTER_SET 
	FOREIGN KEY (REPORTER_SET_ID) REFERENCES REPORTER_SET (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE USER_WORKSPACE ADD CONSTRAINT FK_USER_WORKSPACE_STUDY_SUBSCRIPTION 
	FOREIGN KEY (DEFAULT_STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);

ALTER TABLE STUDY_SUBJECT_ANNOTATIONS ADD CONSTRAINT FK_STUDY_CLINICAL_ANNOTATIONS_ANNOTATION_DEFINITION 
	FOREIGN KEY (ANNOTATION_DEFINITION_ID) REFERENCES ANNOTATION_DEFINITION (ID);

ALTER TABLE STUDY_SUBJECT_ANNOTATIONS ADD CONSTRAINT FK_STUDY_CLINICAL_ANNOTATIONS_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_SAMPLE_ANNOTATIONS ADD CONSTRAINT FK_STUDY_SAMPLE_ANNOTATIONS_ANNOTATION_DEFINITION 
	FOREIGN KEY (ANNOTATION_DEFINITION_ID) REFERENCES ANNOTATION_DEFINITION (ID);

ALTER TABLE STUDY_SAMPLE_ANNOTATIONS ADD CONSTRAINT FK_STUDY_SAMPLE_ANNOTATIONS_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_IMAGE_ANNOTATIONS ADD CONSTRAINT FK_STUDY_IMAGE_ANNOTATIONS_ANNOTATION_DEFINITION 
	FOREIGN KEY (ANNOTATION_DEFINITION_ID) REFERENCES ANNOTATION_DEFINITION (ID);

ALTER TABLE STUDY_IMAGE_ANNOTATIONS ADD CONSTRAINT FK_STUDY_IMAGE_ANNOTATIONS_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SAMPLE ADD CONSTRAINT FK_SAMPLE_STUDY 
    FOREIGN KEY (CONTROL_FOR_STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGN_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE QUERY ADD CONSTRAINT FK_QUERY_STUDY_SUBSCRIPTION 
	FOREIGN KEY (STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);

ALTER TABLE QUERY ADD CONSTRAINT FK_QUERY_COMPUND_CRITERION 
    FOREIGN KEY (COMPOUND_CRITERION_ID) references COMPOUND_CRITERION (ID);


ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_SERIES_ACQUISITION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_STUDY_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGN_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE ARRAY_DATA_MATRIX ADD CONSTRAINT FK_ARRAY_DATA_MATRIX_REPORTER_SET 
	FOREIGN KEY (REPORTER_SET_ID) REFERENCES REPORTER_SET (ID);

ALTER TABLE ARRAY_DATA_MATRIX ADD CONSTRAINT FK_ARRAY_DATA_MATRIX_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE ABSTRACT_REPORTER ADD CONSTRAINT FK_ABSTRACT_REPORTER_REPORTER_SET 
	FOREIGN KEY (REPORTER_SET_ID) REFERENCES REPORTER_SET (ID);

ALTER TABLE TIMEPOINT ADD CONSTRAINT FK_TIMEPOINT_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT 
	FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT_LIST 
	FOREIGN KEY (SUBJECT_LIST_ID) REFERENCES SUBJECT_LIST (ID);
	
ALTER TABLE STUDY ADD CONSTRAINT FK_STUDY_TIMEPOINT 
    FOREIGN KEY (DEFAULT_TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE STUDY_SUBSCRIPTION ADD CONSTRAINT FK_STUDY_SUBSCRIPTION_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_SUBSCRIPTION ADD CONSTRAINT FK_STUDY_SUBSCRIPTION_USER_WORKSPACE 
	FOREIGN KEY (USER_WORKSPACE_ID) REFERENCES USER_WORKSPACE (ID);

ALTER TABLE STUDY_SUBJECT_ASSIGNMENT ADD CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_SUBJECT_ASSIGNMENT ADD CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_SUBJECT 
	FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT (ID);

ALTER TABLE REPORTER_SET ADD CONSTRAINT FK_REPORTER_SET_PLATFORM 
	FOREIGN KEY (PLATFORM_ID) REFERENCES PLATFORM (ID);

ALTER TABLE MICROARRAY_SAMPLES ADD CONSTRAINT FK_MICROARRAY_SAMPLES_MICROARRAY 
	FOREIGN KEY (MICROARRAY_ID) REFERENCES MICROARRAY (ID);

ALTER TABLE MICROARRAY_SAMPLES ADD CONSTRAINT FK_MICROARRAY_SAMPLES_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE MICROARRAY ADD CONSTRAINT FK_ARRAY_PLATFORM 
	FOREIGN KEY (PLATFORM_ID) REFERENCES PLATFORM (ID);

ALTER TABLE ANNOTATION_DEFINITION ADD CONSTRAINT FK_ANNOTATION_DEFINITION_CDE 
	FOREIGN KEY (COMMON_DATA_ELEMENT_ID) REFERENCES COMMON_DATA_ELEMENT (ID);

ALTER TABLE ANALYSIS_JOB_CONFIG ADD CONSTRAINT FK_ANALYSIS_JOB_CONFIG_ANALYSIS_METHOD 
	FOREIGN KEY (ANALYSIS_METHOD_ID) REFERENCES ANALYSIS_METHOD (ID);

ALTER TABLE ANALYSIS_JOB_CONFIG ADD CONSTRAINT FK_ANALYSIS_JOB_CONFIG_STUDY_SUBSCRIPTION 
	FOREIGN KEY (STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);

ALTER TABLE GENE ADD INDEX GENE_SYMBOL_INDEX(SYMBOL);
