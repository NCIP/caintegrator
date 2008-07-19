CREATE TABLE SUBJECT_ANNOTATION 
(
    ID bigint not null auto_increment, 
    STUDY_SUBJECT_ASSIGNMENT_ID bigint, 
    TIMEPOINT_ID bigint, 
    ANNOTATION_VALUE_ID bigint unique, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_ANNOTATION_VALUE 
(
    ID bigint not null auto_increment, 
    ANNOTATION_DEFINITION_ID bigint, 
    ABSTRACT_PERMISSABLE_VALUE_ID bigint, 
    SAMPLE_ACQUISITION_ID bigint, 
    IMAGE_SERIES_ID bigint, 
    IMAGE_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_ANNOTATION_VALUE 
(
    ID bigint not null, 
    VALUE double precision, 
    ABSTRACT_ANNOTATION_VALUE_ID bigint,
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE STRING_ANNOTATION_VALUE 
(
    ID bigint not null, 
    VALUE varchar(255), 
    ABSTRACT_ANNOTATION_VALUE_ID bigint,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE DATE_ANNOTATION_VALUE 
(
    ID bigint not null, 
    VALUE datetime,
    ABSTRACT_ANNOTATION_VALUE_ID bigint,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE ABSTRACT_PERMISSABLE_VALUE 
(
    ID bigint not null auto_increment, 
    ANNOTATION_DEFINITION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_PERMISSABLE_VALUE 
(
    ID bigint not null, 
    HIGH_VALUE double precision, 
    IS_RANGE_VALUE integer, 
    LOW_VALUE double precision, 
    VALUE double precision, 
    ABSTRACT_PERMISSABLE_VALUE_ID bigint,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE STRING_PERMISSABLE_VALUE 
(
    ID bigint not null, 
    VALUE varchar(255),
    ABSTRACT_PERMISSABLE_VALUE_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE DATE_PERMISSABLE_VALUE 
(
    ID bigint not null, 
    VALUE datetime, 
    ABSTRACT_PERMISSABLE_VALUE_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE RESULT_COLUMN 
(
    ID bigint not null auto_increment, 
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
    ID bigint not null auto_increment, 
    ROW_INDEX integer, 
    SAMPLE_ACQUISITION_ID bigint, 
    STUDY_SUBJECT_ASSIGNMENT_ID bigint, 
    IMAGE_SERIES_ACQUISITION_ID bigint, 
    QUERY_RESULT_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE RESULT_VALUE 
(
    ID bigint not null auto_increment, 
    RESULT_COLUMN_ID bigint, 
    ANNOTATION_VALUE_ID bigint, 
    RESULT_ROW_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE QUERY_RESULT
(
    ID bigint not null auto_increment, 
    QUERY_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_CRITERION 
(
    ID bigint not null auto_increment, 
    COMPOUND_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE COMPOUND_CRITERION 
(
    ID bigint not null, 
    BOOLEAN_OPERATOR varchar(255),
    ABSTRACT_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE ABSTRACT_ANNOTATION_CRITERION 
(
    ID bigint not null, 
    ENTITY_TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint, 
    ABSTRACT_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE STRING_COMPARISON_CRITERION 
(
    ID bigint not null, 
    VALUE varchar(255),
    ABSTRACT_ANNOTATION_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_COMPARISON_CRITERION 
(
    ID bigint not null,
    NUMERIC_COMPARISON_OPERATOR varchar(255), 
    VALUE double precision,
    ABSTRACT_ANNOTATION_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE SELECTED_VALUE_CRITERION 
(
    ID bigint not null,
    ABSTRACT_ANNOTATION_CRITERION_ID bigint, 
    primary key (ID)
) TYPE=InnoDB;


CREATE TABLE SELECTED_VALUE_CRITERIA_PERMISSABLE_VALUES 
(
    ABSTRACT_PERMISSABLE_VALUE_ID bigint not null, 
    SELECTED_VALUE_CRITERION_ID bigint not null,
    PRIMARY KEY (ABSTRACT_PERMISSABLE_VALUE_ID, SELECTED_VALUE_CRITERION_ID)
) TYPE=InnoDB;











CREATE TABLE IMAGE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IMAGE_SERIES_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (IMAGE_SERIES_ID)
) TYPE=InnoDB;


CREATE TABLE IMAGE_SERIES
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IMAGE_STUDY_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (IMAGE_STUDY_ID)
) TYPE=InnoDB;


CREATE TABLE GENE_EXPRESSION_REPORTER
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	GENE_ID BIGINT,
	ABSTRACT_REPORTER_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ABSTRACT_REPORTER_ID),
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
	COMPOUND_CRITERION_ID bigint unique,
	STUDY_SUBSCRIPTION_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (STUDY_SUBSCRIPTION_ID)
) TYPE=InnoDB;


CREATE TABLE IMAGE_SERIES_ACQUISITION
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
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
	PLATFORM_ID BIGINT,
	NAME VARCHAR(50),
	REPORTER_SET_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (PLATFORM_ID),
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
	STUDY_SUBSCRIPTION_ID BIGINT,
	ABSTRACT_LIST_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (ABSTRACT_LIST_ID),
	KEY (STUDY_SUBSCRIPTION_ID)
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
	NAME VARCHAR(50),
	PRIMARY KEY (ID),
	KEY (PLATFORM_ID)
) TYPE=InnoDB;


CREATE TABLE ANNOTATION_DEFINITION
(
	DISPLAY_NAME VARCHAR(100),
	ID BIGINT NOT NULL AUTO_INCREMENT,
	PREFERRED_DEFINITION VARCHAR(500),
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
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE SAMPLE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(50),
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
	SYMBOL VARCHAR(50),
	FULL_NAME VARCHAR(100),
	GENBANK_ACCESSION VARCHAR(50),
	GENBANK_ACCESSION_VERSION VARCHAR(50),
	ENSEMBLE_GENE_ID VARCHAR(50),
	UNIGENE_CLUSTER_ID VARCHAR(50),
	ENTREZ_GENE_ID VARCHAR(50),
	PRIMARY KEY (ID)
) TYPE=InnoDB;


CREATE TABLE COMMON_DATA_ELEMENT
(
	PUBLIC_ID VARCHAR(50),
	VERSION VARCHAR(20),
	ID BIGINT NOT NULL AUTO_INCREMENT,
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
	PRIMARY KEY (ID)
) TYPE=InnoDB;





ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_ANNOTATION_VALUE 
	FOREIGN KEY (ANNOTATION_VALUE_ID) REFERENCES ABSTRACT_ANNOTATION_VALUE (ID);

ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGNMENT_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE SUBJECT_ANNOTATION ADD CONSTRAINT FK_SUBJECT_ANNOTATION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

alter table ABSTRACT_ANNOTATION_VALUE add constraint FK_ABSTRACT_ANNOTATION_VALUE_IMAGE_SERIES 
    foreign key (IMAGE_SERIES_ID) references IMAGE_SERIES (ID);
alter table ABSTRACT_ANNOTATION_VALUE add constraint FK_ABSTRACT_ANNOTATION_VALUE_SAMPLE_ACQUISITION
    foreign key (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);
alter table ABSTRACT_ANNOTATION_VALUE add constraint FK_ABSTRACT_ANNOTATION_VALUE_IMAGE 
    foreign key (IMAGE_ID) references IMAGE (ID);
alter table ABSTRACT_ANNOTATION_VALUE add constraint FK_ABSTRACT_ANNOTATION_VALUE_ABSTRACT_PERMISSABLE_VALUE 
    foreign key (ABSTRACT_PERMISSABLE_VALUE_ID) references ABSTRACT_PERMISSABLE_VALUE (ID);
alter table ABSTRACT_ANNOTATION_VALUE add constraint FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_DEFINITION 
    foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

alter table NUMERIC_ANNOTATION_VALUE add constraint FK_NUMERIC_ANNOTATION_VALUE_ABSTRACT_ANNOTATION_VALUE
    foreign key (ABSTRACT_ANNOTATION_VALUE_ID) references ABSTRACT_ANNOTATION_VALUE (ID);

alter table STRING_ANNOTATION_VALUE add constraint FK_STRING_ANNOTATION_VALUE_ABSTRACT_ANNOTATION_VALUE 
    foreign key (ABSTRACT_ANNOTATION_VALUE_ID) references ABSTRACT_ANNOTATION_VALUE (ID);

alter table DATE_ANNOTATION_VALUE add constraint FK_DATE_ANNOTATION_VALUE_ABSTRACT_ANNOTATION_VALUE 
    foreign key (ABSTRACT_ANNOTATION_VALUE_ID) references ABSTRACT_ANNOTATION_VALUE (ID);

alter table ABSTRACT_PERMISSABLE_VALUE add constraint FK_ABSTRACT_PERMISSABLE_VALUE_ANNOTATION_DEFINITION 
    foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

alter table NUMERIC_PERMISSABLE_VALUE add constraint FK_NUMERIC_PERMISSABLE_VALUE_ABSTRACT_PERMISSABLE_VALUE 
    foreign key (ABSTRACT_PERMISSABLE_VALUE_ID) references ABSTRACT_PERMISSABLE_VALUE (ID);

alter table STRING_PERMISSABLE_VALUE add constraint FK_STRING_PERMISSABLE_VALUE_ABSTRACT_PERMISSABLE_VALUE 
    foreign key (ABSTRACT_PERMISSABLE_VALUE_ID) references ABSTRACT_PERMISSABLE_VALUE (ID);

alter table DATE_PERMISSABLE_VALUE add constraint FK_DATE_PERMISSABLE_VALUE_ABSTRACT_PERMISSABLE_VALUE 
    foreign key (ABSTRACT_PERMISSABLE_VALUE_ID) references ABSTRACT_PERMISSABLE_VALUE (ID);

alter table RESULT_COLUMN add constraint FK_RESULT_COLUMN_QUERY 
    foreign key (QUERY_ID) references QUERY (ID);
alter table RESULT_COLUMN add constraint FK_RESULT_COLUMN_ANNOTATION_DEFINITION 
    foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);
    
alter table RESULT_ROW add constraint RESULT_ROW_QUERY_RESULT
    foreign key (QUERY_RESULT_ID) references QUERY_RESULT (ID);
alter table RESULT_ROW add constraint RESULT_ROW_SAMPLE_ACQUISITION 
    foreign key (SAMPLE_ACQUISITION_ID) references SAMPLE_ACQUISITION (ID);
alter table RESULT_ROW add constraint RESULT_ROW_STUDY_SUBJECT_ASSIGNMENT 
    foreign key (STUDY_SUBJECT_ASSIGNMENT_ID) references STUDY_SUBJECT_ASSIGNMENT (ID);
alter table RESULT_ROW add constraint RESULT_ROW_IMAGE_SERIES_ACQUISITION 
    foreign key (IMAGE_SERIES_ACQUISITION_ID) references IMAGE_SERIES_ACQUISITION (ID);
    
alter table RESULT_VALUE add constraint FK_RESULT_VALUE_ANNOTATION_VALUE 
    foreign key (ANNOTATION_VALUE_ID) references ABSTRACT_ANNOTATION_VALUE (ID);
alter table RESULT_VALUE add constraint FK_RESULT_VALUE_RESULT_COLUMN 
    foreign key (RESULT_COLUMN_ID) references RESULT_COLUMN (ID);
alter table RESULT_VALUE add constraint FK_RESULT_VALUE_RESULT_ROW 
    foreign key (RESULT_ROW_ID) references RESULT_ROW (ID);

alter table QUERY_RESULT add constraint FK_QUERY_RESULT_QUERY 
    foreign key (QUERY_ID) references QUERY (ID);

alter table ABSTRACT_CRITERION add constraint FK_ABSTRACT_CRITERION_COUMPOUND_CRITERION 
    foreign key (COMPOUND_CRITERION_ID) references COMPOUND_CRITERION (ID);

alter table COMPOUND_CRITERION add constraint FK_COMPOUND_CRITERION_ABSTRACT_CRITERION 
    foreign key (ABSTRACT_CRITERION_ID) references ABSTRACT_CRITERION (ID);
    
alter table ABSTRACT_ANNOTATION_CRITERION add constraint FK_ABSTRACT_ANNOTATION_CRITERION_ANNOTATION_DEFINITION 
    foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);
alter table ABSTRACT_ANNOTATION_CRITERION add constraint FK_ABSTRACT_ANNOTATION_CRITERION_ABSTRACT_CRITERION 
    foreign key (ABSTRACT_CRITERION_ID) references ABSTRACT_CRITERION (ID);

alter table STRING_COMPARISON_CRITERION add constraint FK_STRING_COMPARISON_CRITERION_ABSTRACT_ANNOTATION_CRITERION 
    foreign key (ABSTRACT_ANNOTATION_CRITERION_ID) references ABSTRACT_ANNOTATION_CRITERION (ID);
alter table NUMERIC_COMPARISON_CRITERION add constraint FK_NUMERIC_COMPARISON_CRITERION_ABSTRACT_ANNOTATION_CRITERION 
    foreign key (ABSTRACT_ANNOTATION_CRITERION_ID) references ABSTRACT_ANNOTATION_CRITERION (ID);
alter table SELECTED_VALUE_CRITERION add constraint FK_SELECTED_VALUE_CRITERION_ABSTRACT_ANNOTATION_CRITERION 
    foreign key (ABSTRACT_ANNOTATION_CRITERION_ID) references ABSTRACT_ANNOTATION_CRITERION (ID);


alter table SELECTED_VALUE_CRITERIA_PERMISSABLE_VALUES add constraint FK_SELECTED_VALUE_CRITERIA_PERMISSABLE_VALUES_SELECTED 
    foreign key (SELECTED_VALUE_CRITERION_ID) references SELECTED_VALUE_CRITERION (ID);
alter table SELECTED_VALUE_CRITERIA_PERMISSABLE_VALUES add constraint FK_SELECTED_VALUE_CRITERIA_PERMISSABLE_VALUES_PERMISSABLE 
    foreign key (ABSTRACT_PERMISSABLE_VALUE_ID) references ABSTRACT_PERMISSABLE_VALUE (ID);






ALTER TABLE IMAGE ADD CONSTRAINT FK_IMAGE_IMAGE_SERIES 
	FOREIGN KEY (IMAGE_SERIES_ID) REFERENCES IMAGE_SERIES (ID);

ALTER TABLE IMAGE_SERIES ADD CONSTRAINT FK_IMAGE_SERIES_IMAGE_STUDY 
	FOREIGN KEY (IMAGE_STUDY_ID) REFERENCES IMAGE_SERIES_ACQUISITION (ID);

ALTER TABLE GENE_EXPRESSION_REPORTER ADD CONSTRAINT FK_GENE_EXPRESSION_REPORTER_ABSTRACT_REPORTER 
	FOREIGN KEY (ABSTRACT_REPORTER_ID) REFERENCES ABSTRACT_REPORTER (ID);

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

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGN_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE SAMPLE_ACQUISITION ADD CONSTRAINT FK_SAMPLE_ACQUISITION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE QUERY ADD CONSTRAINT FK_QUERY_STUDY_SUBSCRIPTION 
	FOREIGN KEY (STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);

alter table QUERY add constraint FK_QUERY_COMPUND_CRITERION 
    foreign key (COMPOUND_CRITERION_ID) references COMPOUND_CRITERION (ID);


ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_SERIES_ACQUISITION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_STUDY_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGN_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE ARRAY_DATA_MATRIX ADD CONSTRAINT FK_ARRAY_DATA_MATRIX_REPORTER_SET 
	FOREIGN KEY (REPORTER_SET_ID) REFERENCES REPORTER_SET (ID);

ALTER TABLE ARRAY_DATA_MATRIX ADD CONSTRAINT FK_ARRAY_DATA_MATRIX_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE ABSTRACT_REPORTER ADD CONSTRAINT FK_ABSTRACT_REPORTER_PLATFORM 
	FOREIGN KEY (PLATFORM_ID) REFERENCES PLATFORM (ID);

ALTER TABLE ABSTRACT_REPORTER ADD CONSTRAINT FK_ABSTRACT_REPORTER_REPORTER_SET 
	FOREIGN KEY (REPORTER_SET_ID) REFERENCES REPORTER_SET (ID);

ALTER TABLE TIMEPOINT ADD CONSTRAINT FK_TIMEPOINT_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT 
	FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT_LIST 
	FOREIGN KEY (SUBJECT_LIST_ID) REFERENCES SUBJECT_LIST (ID);

ALTER TABLE SUBJECT_LIST ADD CONSTRAINT FK_SUBJECT_LIST_ABSTRACT_LIST 
	FOREIGN KEY (ABSTRACT_LIST_ID) REFERENCES ABSTRACT_LIST (ID);

ALTER TABLE SUBJECT_LIST ADD CONSTRAINT FK_SUBJECT_LIST_STUDY_SUBSCRIPTION 
	FOREIGN KEY (STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);

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
