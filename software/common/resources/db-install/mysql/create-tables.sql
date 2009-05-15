create table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION (
    ID bigint not null auto_increment, 
    STUDY_CONFIGURATION_ID bigint, 
    LIST_INDEX integer, 
    primary key (ID)
) ENGINE=InnoDB;

create table ANNOTATION_FIELD_DESCRIPTOR (
    ID bigint not null auto_increment, 
    NAME varchar(255), 
    TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint, 
    SHOWN_IN_BROWSE tinyint,
    primary key (ID)
) ENGINE=InnoDB;

create table ANNOTATION_FILE (
    ID bigint not null auto_increment, 
    PATH varchar(255), 
    CURRENTLY_LOADED varchar(20),
    IDENTIFIER_COLUMN_ID bigint, 
    TIMEPOINT_COLUMN_ID bigint, 
    primary key (ID)
) ENGINE=InnoDB;

create table CONFIGURATION_PARAMETER (
    PARAMETER varchar(255), 
    RAW_VALUE varchar(255), 
    primary key (PARAMETER)
) ENGINE=InnoDB;

create table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION (
    ID bigint not null, 
    ANNOTATION_FILE_ID bigint, 
    primary key (ID)
) ENGINE=InnoDB;

create table FILE_COLUMN (
    ID bigint not null auto_increment, 
    POSITION integer, 
    NAME varchar(255), 
    ANNOTATION_FIELD_DESCRIPTOR_ID bigint, 
    ANNOTATION_FILE_ID bigint, 
    LIST_INDEX integer, 
    primary key (ID)
) ENGINE=InnoDB;

create table GENOMIC_DATA_SOURCE_CONFIGURATION (
    ID bigint not null auto_increment, 
    SERVER_CONNECTION_PROFILE_ID bigint, 
    EXPERIMENT_IDENTIFIER varchar(255), 
    PLATFORM_VENDOR varchar(50),
    PLATFORM_NAME varchar(255),
    STUDY_CONFIGURATION_ID bigint, 
    LIST_INDEX integer,
    COPY_NUMBER_MAPPING_FILE_PATH varchar(255),
    COPY_NUMBER_CHANGE_POINT_SIGNIFICANCE_LEVEL double,
    COPY_NUMBER_EARLY_STOPPING_CRITERION double,
    COPY_NUMBER_PERMUTATION_REPLICATES integer,
    COPY_NUMBER_RANDOM_NUMBER_SEED integer,
    COPY_NUMBER_CADNA_COPY_SERVICE_ID bigint,
    SAMPLE_MAPPING_FILE_NAME varchar(255),
    CONTROL_SAMPLE_MAPPING_FILE_NAME varchar(255),
    primary key (ID)
) ENGINE=InnoDB;

create table IMAGE_ANNOTATION_CONFIGURATION (
    ID bigint not null auto_increment, 
    IMAGE_DATA_SOURCE_CONFIGURATION_ID bigint, 
    ANNOTATION_FILE_ID bigint,
    primary key (ID)
) ENGINE=InnoDB;

create table IMAGE_DATA_SOURCE_CONFIGURATION (
    ID bigint not null auto_increment, 
    SERVER_CONNECTION_PROFILE_ID bigint, 
    COLLECTION_NAME varchar(255), 
    MAPPING_FILE_NAME varchar(255), 
    STUDY_CONFIGURATION_ID bigint, 
    IMAGE_ANNOTATION_CONFIGURATION_ID bigint, 
    LIST_INDEX integer,
    primary key (ID)
) ENGINE=InnoDB;

create table SAMPLE_IDENTIFIER (
    ID bigint not null auto_increment, 
    EXPERIMENT_IDENTIFIER varchar(255), 
    SAMPLE_NAME varchar(255), 
    GENOMIC_DATA_SOURCE_CONFIGURATION_ID bigint, 
    LIST_INDEX integer, 
    primary key (ID)
) ENGINE=InnoDB;

create table SERVER_CONNECTION_PROFILE (
    ID bigint not null auto_increment, 
    HOSTNAME varchar(255), 
    PORT integer, URL varchar(255), 
    USERNAME varchar(255), 
    PASSWORD varchar(255), 
    primary key (ID)
) ENGINE=InnoDB;


create table STUDY_CONFIGURATION (
    ID bigint not null auto_increment, 
    VISIBILITY varchar(255), 
    STATUS varchar(255), 
    STATUS_DESCRIPTION varchar(255),
    DEPLOYMENT_START_DATE datetime,
    DEPLOYMENT_FINISH_DATE datetime,
    STUDY_ID bigint, 
    STUDY_LOGO_ID bigint,
    USER_WORKSPACE_ID bigint,
    primary key (ID)
) ENGINE=InnoDB;

create table STUDY_LOGO (
    ID bigint not null auto_increment, 
    FILENAME varchar(155),
    FILETYPE varchar(100),
    PATH varchar(255),
    primary key (ID)
) ENGINE=InnoDB;


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
    COMPARE_TO_SAMPLE_SET_ID bigint,
    primary key (ID)
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
    ANNOTATION_FIELD_DESCRIPTOR_ID bigint, 
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

CREATE TABLE DATE_COMPARISON_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    DATE_COMPARISON_OPERATOR varchar(255), 
    DATE_VALUE datetime,
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
    PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE TABLE DNA_ANALYSIS_REPORTER
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    CHROMOSOME varchar(2),
    POSITION INTEGER,
    DB_SNP_ID varchar(255),
    ALLELE_A CHAR,
    ALLELE_B CHAR,
    PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE TABLE ARRAY_DATA
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ARRAY_ID BIGINT,
	SAMPLE_ID BIGINT,
	STUDY_ID BIGINT,
	REPORTER_LIST_ID BIGINT,
    ARRAY_DATA_TYPE varchar(50),
	PRIMARY KEY (ID),
	KEY (ARRAY_ID),
	KEY (REPORTER_LIST_ID),
	KEY (SAMPLE_ID),
	KEY (STUDY_ID)
) TYPE=InnoDB;

CREATE TABLE SEGMENT_DATA
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ARRAY_DATA_ID BIGINT,
    NUMBER_OF_MARKERS INTEGER,
    SEGMENT_VALUE FLOAT,
    CHROMOSOME VARCHAR(2),
    START_POSITION INTEGER,
    END_POSITION INTEGER,
    PRIMARY KEY (ID),
    KEY (ARRAY_DATA_ID)
) Type=InnoDB;

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


CREATE TABLE ABSTRACT_PERSISTED_ANALYSIS_JOB
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(200),
    STATUS VARCHAR(40),
    TYPE VARCHAR(40),
    CREATION_DATE datetime,
    LAST_UPDATE_DATE datetime,
    STUDY_SUBSCRIPTION_ID BIGINT,
    JOB_URL varchar(150),
    GP_JOB_NUMBER integer,
    PREPROCESS_GRID_URL varchar(150),
    MAIN_GRID_URL varchar(150),
    RESULTS_ZIP_FILE_PATH VARCHAR(155),
    PRIMARY KEY (ID),
    KEY (STUDY_SUBSCRIPTION_ID)
) TYPE=InnoDB;


CREATE TABLE MARKER_RESULT 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    COMPARATIVE_MARKER_JOB_ID bigint, 
    MARKER_INDEX integer, 
    RANK integer,
    FEATURE VARCHAR(200),
    DESCRIPTION VARCHAR(200),
    SCORE double precision,
    FEATURE_P double precision,
    FEATURE_P_LOW double precision,
    FEATURE_P_HIGH double precision,
    FDR double precision,
    Q_VALUE double precision,
    BONFERRONI double precision,
    MAX_T double precision,
    FWER double precision,
    FOLD_CHANGE double precision,
    CLASS_0_MEAN double precision,
    CLASS_0_STD double precision,
    CLASS_1_MEAN double precision,
    CLASS_1_STD double precision,
    K double precision,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE GISTIC_RESULT 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    GISTIC_JOB_ID bigint, 
    GISTIC_INDEX integer, 
    AMPLIFICATION tinyint,
    AMPLITUDE double precision,
    SCORE double precision,
    Q_VALUE double precision,
    FREQUENCY double precision,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE IMAGE_SERIES_ACQUISITION
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
    IMAGE_DATA_SOURCE_CONFIGURATION_ID bigint,
    IMAGE_DATA_SOURCE_CONFIGURATION_INDEX integer,
    IDENTIFIER varchar(200),
    NCIA_TRIAL_IDENTIFIER varchar(200),
	STUDY_SUBJECT_ASSIGN_ID BIGINT,
	TIMEPOINT_ID BIGINT,
	PRIMARY KEY (ID),
	KEY (TIMEPOINT_ID),
	KEY (STUDY_SUBJECT_ASSIGN_ID)
) TYPE=InnoDB;


CREATE TABLE ABSTRACT_REPORTER
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(2048),
	REPORTER_LIST_ID BIGINT,
    REPORTER_INDEX INTEGER NOT NULL,
	PRIMARY KEY (ID),
	KEY (REPORTER_LIST_ID)
) TYPE=InnoDB;


CREATE TABLE REPORTER_GENES
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    REPORTER_ID BIGINT,
    GENE_ID BIGINT,
    PRIMARY KEY (ID),
    KEY (REPORTER_ID),
    KEY (GENE_ID)
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


CREATE TABLE REPORTER_LIST
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
	DEFAULT_CONTROL_SAMPLE_SET_ID bigint,
	PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE UNIQUE INDEX UNIQUE_TITLE ON STUDY(SHORT_TITLE_TEXT);

CREATE TABLE SAMPLE
(
	ID BIGINT NOT NULL AUTO_INCREMENT,
    GENOMIC_DATA_SOURCE_ID bigint,
	NAME VARCHAR(100),
    GENOMIC_DATA_SOURCE_INDEX integer,
	SAMPLE_ACQUISITION_ID bigint,
	PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE TABLE SAMPLE_SET
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(100),
    PRIMARY KEY (ID)
) TYPE=InnoDB;

CREATE TABLE SAMPLE_SET_SAMPLES
(
    SAMPLE_SET_ID BIGINT NOT NULL,
    SAMPLE_ID BIGINT NOT NULL,
    PRIMARY KEY (SAMPLE_SET_ID, SAMPLE_ID),
    KEY (SAMPLE_SET_ID),
    KEY (SAMPLE_ID)
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



alter table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION 
    add index FK_ABSTRACT_CLINICAL_SOURCE_STUDY_CONFIGURATION (STUDY_CONFIGURATION_ID), 
    add constraint FK_ABSTRACT_CLINICAL_SOURCE_STUDY_CONFIGURATION foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);


alter table ANNOTATION_FIELD_DESCRIPTOR 
    add index FK_ANNOTATION_FIELD_DESCRIPTOR_ANNOTATION_DEFINITION (ANNOTATION_DEFINITION_ID), 
    add constraint FK_ANNOTATION_FIELD_DESCRIPTOR_ANNOTATION_DEFINITION foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

alter table ANNOTATION_FILE 
    add index FK_ANNOTATION_FILE_TIMEPOINT_COLUMN (TIMEPOINT_COLUMN_ID), 
    add constraint FK_ANNOTATION_FILE_TIMEPOINT_COLUMN foreign key (TIMEPOINT_COLUMN_ID) references FILE_COLUMN (ID);

alter table ANNOTATION_FILE 
    add index FK_ANNOTATION_FILE_IDENTIFIER_COLUMN (IDENTIFIER_COLUMN_ID), 
    add constraint FK_ANNOTATION_FILE_IDENTIFIER_COLUMN foreign key (IDENTIFIER_COLUMN_ID) references FILE_COLUMN (ID);

    
alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION 
    add index FK_DEL_TEXT_CLINICAL_ANNOTATION_FILE (ANNOTATION_FILE_ID), 
    add constraint FK_DEL_TEXT_CLINICAL_ANNOTATION_FILE foreign key (ANNOTATION_FILE_ID) references ANNOTATION_FILE (ID);

alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION 
    add index FK_DEL_TEXT_CLINICAL_ABSTRACT_CLINICAL_SOURCE (ID), 
    add constraint FK_DEL_TEXT_CLINICAL_ABSTRACT_CLINICAL_SOURCE foreign key (ID) references ABSTRACT_CLINICAL_SOURCE_CONFIGURATION (ID);

alter table FILE_COLUMN 
    add index FK_FILE_COLUMN_ANN_FIELD_DESC (ANNOTATION_FIELD_DESCRIPTOR_ID), 
    add constraint FK_FILE_COLUMN_ANN_FIELD_DESC foreign key (ANNOTATION_FIELD_DESCRIPTOR_ID) references ANNOTATION_FIELD_DESCRIPTOR (ID);

alter table FILE_COLUMN 
    add index FK_FILE_COLUMN_ANNOTATION_FILE (ANNOTATION_FILE_ID), 
    add constraint FK_FILE_COLUMN_ANNOTATION_FILE foreign key (ANNOTATION_FILE_ID) references ANNOTATION_FILE (ID);
    
alter table GENOMIC_DATA_SOURCE_CONFIGURATION 
    add index FK_GENOMIC_DATA_SOURCE_SERVER_CONNECTION (SERVER_CONNECTION_PROFILE_ID), 
    add constraint FK_GENOMIC_DATA_SOURCE_SERVER_CONNECTION foreign key (SERVER_CONNECTION_PROFILE_ID) references SERVER_CONNECTION_PROFILE (ID);

alter table GENOMIC_DATA_SOURCE_CONFIGURATION 
    add index FK_GENOMIC_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION (STUDY_CONFIGURATION_ID), 
    add constraint FK_GENOMIC_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);

alter table GENOMIC_DATA_SOURCE_CONFIGURATION 
    add index FK_GENOMIC_DATA_SOURCE_CONFIGURATION_CADNA_COPY_SERVICE (COPY_NUMBER_CADNA_COPY_SERVICE_ID), 
    add constraint FK_GENOMIC_DATA_SOURCE_CONFIGURATION_CADNA_COPY_SERVICE foreign key (COPY_NUMBER_CADNA_COPY_SERVICE_ID) references SERVER_CONNECTION_PROFILE (ID);
    
alter table IMAGE_DATA_SOURCE_CONFIGURATION 
    add index FK_IMAGE_DATA_SOURCE_SERVER_CONNECTION (SERVER_CONNECTION_PROFILE_ID), 
    add constraint FK_IMAGE_DATA_SOURCE_SERVER_CONNECTION foreign key (SERVER_CONNECTION_PROFILE_ID) references SERVER_CONNECTION_PROFILE (ID);

alter table IMAGE_DATA_SOURCE_CONFIGURATION 
    add index FK_IMAGE_DATA_SOURCE_ANNOTATION_CONFIGURATION (IMAGE_ANNOTATION_CONFIGURATION_ID), 
    add constraint FK_IMAGE_DATA_SOURCE_ANNOTATION_CONFIGURATION foreign key (IMAGE_ANNOTATION_CONFIGURATION_ID) references IMAGE_ANNOTATION_CONFIGURATION (ID);
   
alter table IMAGE_DATA_SOURCE_CONFIGURATION 
    add index FK_IMAGE_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION (STUDY_CONFIGURATION_ID), 
    add constraint FK_IMAGE_DATA_SOURCE_CONFIGURATION_STUDY_CONFIGURATION foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);
   
alter table IMAGE_SERIES_ACQUISITION
    add index FK_IMAGE_DATA_SOURCE_CONFIGURATION_SOURCE (IMAGE_DATA_SOURCE_CONFIGURATION_ID), 
    add constraint FK_IMAGE_DATA_SOURCE_CONFIGURATION_SOURCE foreign key (IMAGE_DATA_SOURCE_CONFIGURATION_ID) references IMAGE_DATA_SOURCE_CONFIGURATION (ID);

alter table IMAGE_ANNOTATION_CONFIGURATION 
    add index FK_IMAGE_ANNOTATION_DATA_SOURCE_CONFIGURATION (IMAGE_DATA_SOURCE_CONFIGURATION_ID), 
    add constraint FK_IMAGE_ANNOTATION_DATA_SOURCE_CONFIGURATION foreign key (IMAGE_DATA_SOURCE_CONFIGURATION_ID) references IMAGE_DATA_SOURCE_CONFIGURATION (ID);
    
alter table IMAGE_ANNOTATION_CONFIGURATION 
    add index FK_IMAGE_ANNOTATION_CLINICAL_ANNOTATION_FILE (ANNOTATION_FILE_ID), 
    add constraint FK_IMAGE_ANNOTATION_CLINICAL_ANNOTATION_FILE foreign key (ANNOTATION_FILE_ID) references ANNOTATION_FILE (ID);

alter table SAMPLE
    add index FK_SAMPLE_GENOMIC_DATA_SOURCE (GENOMIC_DATA_SOURCE_ID), 
    add constraint FK_SAMPLE_GENOMIC_DATA_SOURCE foreign key (GENOMIC_DATA_SOURCE_ID) references GENOMIC_DATA_SOURCE_CONFIGURATION (ID);

alter table SAMPLE_IDENTIFIER 
    add index FK_SAMPLE_IDENTIFIER_GENOMIC_DATA_SOURCE (GENOMIC_DATA_SOURCE_CONFIGURATION_ID), 
    add constraint FK_SAMPLE_IDENTIFIER_GENOMIC_DATA_SOURCE foreign key (GENOMIC_DATA_SOURCE_CONFIGURATION_ID) references GENOMIC_DATA_SOURCE_CONFIGURATION (ID);

alter table STUDY_CONFIGURATION 
    add index FK_STUDY_CONFIGURATION_STUDY_ID (STUDY_ID), 
    add constraint FK_STUDY_CONFIGURATION_STUDY_ID foreign key (STUDY_ID) references STUDY (ID);

alter table STUDY_CONFIGURATION 
    add index FK_STUDY_CONFIGURATION_STUDY_LOGO_ID (STUDY_LOGO_ID), 
    add constraint FK_STUDY_CONFIGURATION_STUDY_LOGO_ID foreign key (STUDY_LOGO_ID) references STUDY_LOGO (ID);

    
ALTER TABLE STUDY_CONFIGURATION ADD CONSTRAINT FK_STUDY_CONFIGURATION_USER_WORKSPACE 
    FOREIGN KEY (USER_WORKSPACE_ID) REFERENCES USER_WORKSPACE (ID);


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
ALTER TABLE ABSTRACT_ANNOTATION_VALUE ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_VALUE_ANNOTATION_FIELD_DESCRIPTOR
    FOREIGN KEY (ANNOTATION_FIELD_DESCRIPTOR_ID) references ANNOTATION_FIELD_DESCRIPTOR (ID);

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

ALTER TABLE FOLD_CHANGE_CRITERION ADD CONSTRAINT FK_FOLD_CHANGE_CRITERION_SAMPLE_SET
    FOREIGN KEY (COMPARE_TO_SAMPLE_SET_ID) references SAMPLE_SET (ID);


ALTER TABLE IMAGE ADD CONSTRAINT FK_IMAGE_IMAGE_SERIES 
	FOREIGN KEY (IMAGE_SERIES_ID) REFERENCES IMAGE_SERIES (ID);

ALTER TABLE IMAGE_SERIES ADD CONSTRAINT FK_IMAGE_SERIES_IMAGE_STUDY 
	FOREIGN KEY (IMAGE_STUDY_ID) REFERENCES IMAGE_SERIES_ACQUISITION (ID);


ALTER TABLE REPORTER_GENES ADD CONSTRAINT FK_REPORTER_GENES_REPORTER 
    FOREIGN KEY (REPORTER_ID) REFERENCES ABSTRACT_REPORTER (ID);

ALTER TABLE REPORTER_GENES ADD CONSTRAINT FK_REPORTER_GENES_GENE 
    FOREIGN KEY (GENE_ID) REFERENCES GENE (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_ARRAY 
	FOREIGN KEY (ARRAY_ID) REFERENCES MICROARRAY (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_REPORTER_LIST 
	FOREIGN KEY (REPORTER_LIST_ID) REFERENCES REPORTER_LIST (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE ARRAY_DATA ADD CONSTRAINT FK_ARRAY_DATA_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SEGMENT_DATA ADD CONSTRAINT FK_SEGMENT_ARRAY_DATA 
    FOREIGN KEY (ARRAY_DATA_ID) REFERENCES ARRAY_DATA (ID);


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

ALTER TABLE QUERY ADD CONSTRAINT FK_QUERY_COMPUND_CRITERION 
    FOREIGN KEY (COMPOUND_CRITERION_ID) references COMPOUND_CRITERION (ID);


ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_SERIES_ACQUISITION_TIMEPOINT 
	FOREIGN KEY (TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);

ALTER TABLE IMAGE_SERIES_ACQUISITION ADD CONSTRAINT FK_IMAGE_STUDY_STUDY_SUBJECT_ASSIGNMENT 
	FOREIGN KEY (STUDY_SUBJECT_ASSIGN_ID) REFERENCES STUDY_SUBJECT_ASSIGNMENT (ID);

ALTER TABLE ABSTRACT_REPORTER ADD CONSTRAINT FK_ABSTRACT_REPORTER_REPORTER_LIST 
	FOREIGN KEY (REPORTER_LIST_ID) REFERENCES REPORTER_LIST (ID);

ALTER TABLE TIMEPOINT ADD CONSTRAINT FK_TIMEPOINT_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT 
	FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT (ID);

ALTER TABLE SUBJECT_LIST_SUBJECT ADD CONSTRAINT FK_SUBJECT_LIST_SUBJECT_SUBJECT_LIST 
	FOREIGN KEY (SUBJECT_LIST_ID) REFERENCES SUBJECT_LIST (ID);
	
ALTER TABLE STUDY ADD CONSTRAINT FK_STUDY_TIMEPOINT 
    FOREIGN KEY (DEFAULT_TIMEPOINT_ID) REFERENCES TIMEPOINT (ID);
    
ALTER TABLE STUDY ADD CONSTRAINT FK_STUDY_DEFAULT_CONTROL_SAMPLE 
    FOREIGN KEY (DEFAULT_CONTROL_SAMPLE_SET_ID) REFERENCES SAMPLE_SET (ID);

ALTER TABLE STUDY_SUBSCRIPTION ADD CONSTRAINT FK_STUDY_SUBSCRIPTION_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_SUBSCRIPTION ADD CONSTRAINT FK_STUDY_SUBSCRIPTION_USER_WORKSPACE 
	FOREIGN KEY (USER_WORKSPACE_ID) REFERENCES USER_WORKSPACE (ID);

ALTER TABLE STUDY_SUBJECT_ASSIGNMENT ADD CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_STUDY 
	FOREIGN KEY (STUDY_ID) REFERENCES STUDY (ID);

ALTER TABLE STUDY_SUBJECT_ASSIGNMENT ADD CONSTRAINT FK_STUDY_SUBJECT_ASSIGNMENT_SUBJECT 
	FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT (ID);

ALTER TABLE REPORTER_LIST ADD CONSTRAINT FK_REPORTER_LIST_PLATFORM 
	FOREIGN KEY (PLATFORM_ID) REFERENCES PLATFORM (ID);

ALTER TABLE MICROARRAY_SAMPLES ADD CONSTRAINT FK_MICROARRAY_SAMPLES_MICROARRAY 
	FOREIGN KEY (MICROARRAY_ID) REFERENCES MICROARRAY (ID);

ALTER TABLE MICROARRAY_SAMPLES ADD CONSTRAINT FK_MICROARRAY_SAMPLES_SAMPLE 
	FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);

ALTER TABLE MICROARRAY ADD CONSTRAINT FK_ARRAY_PLATFORM 
	FOREIGN KEY (PLATFORM_ID) REFERENCES PLATFORM (ID);

ALTER TABLE ANNOTATION_DEFINITION ADD CONSTRAINT FK_ANNOTATION_DEFINITION_CDE 
	FOREIGN KEY (COMMON_DATA_ELEMENT_ID) REFERENCES COMMON_DATA_ELEMENT (ID);

ALTER TABLE GENE ADD INDEX GENE_SYMBOL_INDEX(SYMBOL);

ALTER TABLE MARKER_RESULT ADD CONSTRAINT FK_MARKER_RESULT_CMS_JOB 
    FOREIGN KEY (COMPARATIVE_MARKER_JOB_ID) references ABSTRACT_PERSISTED_ANALYSIS_JOB (ID);

ALTER TABLE GISTIC_RESULT ADD CONSTRAINT FK_GISTIC_RESULT_GISTIC_JOB 
    FOREIGN KEY (GISTIC_JOB_ID) references ABSTRACT_PERSISTED_ANALYSIS_JOB (ID);

ALTER TABLE ABSTRACT_PERSISTED_ANALYSIS_JOB ADD CONSTRAINT FK_PERSISTED_JOB_STUDY_SUBSCRIPTION 
    FOREIGN KEY (STUDY_SUBSCRIPTION_ID) REFERENCES STUDY_SUBSCRIPTION (ID);
    
    
ALTER TABLE SAMPLE_SET_SAMPLES ADD CONSTRAINT FK_SAMPLE_SET_SAMPLES_SAMPLE_SET 
    FOREIGN KEY (SAMPLE_SET_ID) REFERENCES SAMPLE_SET (ID);

ALTER TABLE SAMPLE_SET_SAMPLES ADD CONSTRAINT FK_SAMPLE_SET_SAMPLES_SAMPLE 
    FOREIGN KEY (SAMPLE_ID) REFERENCES SAMPLE (ID);
