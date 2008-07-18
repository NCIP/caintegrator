create table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION (
    ID bigint not null auto_increment, 
    STUDY_CONFIGURATION_ID bigint, 
    LIST_INDEX integer, 
    primary key (ID)
) ENGINE=InnoDB;

create table ANNOTATION_FIELD_DESCRIPTOR (
    ID bigint not null auto_increment, 
    NAME varchar(255), 
    KEYWORDS varchar(255),
    TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint, 
    SHOWN_IN_BROWSE tinyint,
    primary key (ID)
) ENGINE=InnoDB;

create table ANNOTATION_FILE (
    ID bigint not null auto_increment, 
    PATH varchar(255), 
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
    STUDY_CONFIGURATION_ID bigint, 
    LIST_INDEX integer,
    primary key (ID)
) ENGINE=InnoDB;

create table GENOMIC_DATA_SOURCE_SAMPLES (
    GENOMIC_DATA_SOURCE_ID bigint,
    SAMPLE_ID bigint,
    LIST_INDEX integer,
    primary key(GENOMIC_DATA_SOURCE_ID, SAMPLE_ID)
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
    STUDY_ID bigint, 
    primary key (ID)
) ENGINE=InnoDB;


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

alter table SAMPLE_IDENTIFIER 
    add index FK_SAMPLE_IDENTIFIER_GENOMIC_DATA_SOURCE (GENOMIC_DATA_SOURCE_CONFIGURATION_ID), 
    add constraint FK_SAMPLE_IDENTIFIER_GENOMIC_DATA_SOURCE foreign key (GENOMIC_DATA_SOURCE_CONFIGURATION_ID) references GENOMIC_DATA_SOURCE_CONFIGURATION (ID);

alter table STUDY_CONFIGURATION 
    add index FK_STUDY_CONFIGURATION_STUDY_ID (STUDY_ID), 
    add constraint FK_STUDY_CONFIGURATION_STUDY_ID foreign key (STUDY_ID) references STUDY (ID);

    