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
    primary key (ID)
) ENGINE=InnoDB;

create table ANNOTATION_FIELD_DESCRIPTOR_KEYWORDS (
    descriptor_id bigint not null, 
    keyword varchar(255)
) ENGINE=InnoDB;

create table ANNOTATION_FILE (
    ID bigint not null auto_increment, 
    PATH varchar(255), 
    IDENTIFIER_COLUMN_ID bigint, 
    TIMEPOINT_COLUMN_ID bigint, 
    primary key (ID)
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

create table STUDY_CONFIGURATION (
    ID bigint not null auto_increment, 
    VISIBILITY varchar(255), 
    STATUS varchar(255), 
    STUDY_ID bigint, 
    primary key (ID)
) ENGINE=InnoDB;


alter table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION 
    add index FKBAE4F601C0ADCD6 (STUDY_CONFIGURATION_ID), 
    add constraint FKBAE4F601C0ADCD6 foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);


alter table ANNOTATION_FIELD_DESCRIPTOR 
    add index FK5D1F2F04AF56BCDA (ANNOTATION_DEFINITION_ID), 
    add constraint FK5D1F2F04AF56BCDA foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

alter table ANNOTATION_FIELD_DESCRIPTOR_KEYWORDS 
    add index FK95E53685D02FEB76 (descriptor_id), 
    add constraint FK95E53685D02FEB76 foreign key (descriptor_id) references ANNOTATION_FIELD_DESCRIPTOR (ID);

alter table ANNOTATION_FILE 
    add index FKB2E3F48C2F7719A9 (TIMEPOINT_COLUMN_ID), 
    add constraint FKB2E3F48C2F7719A9 foreign key (TIMEPOINT_COLUMN_ID) references FILE_COLUMN (ID);

alter table ANNOTATION_FILE 
    add index FKB2E3F48CF74C0BEF (IDENTIFIER_COLUMN_ID), 
    add constraint FKB2E3F48CF74C0BEF foreign key (IDENTIFIER_COLUMN_ID) references FILE_COLUMN (ID);

    
alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION 
    add index FKCA9C89F268A868E8 (ANNOTATION_FILE_ID), 
    add constraint FKCA9C89F268A868E8 foreign key (ANNOTATION_FILE_ID) references ANNOTATION_FILE (ID);

alter table DELIMITED_TEXT_CLINICAL_SOURCE_CONFIGURATION 
    add index FKCA9C89F25F5B0F92 (ID), 
    add constraint FKCA9C89F25F5B0F92 foreign key (ID) references ABSTRACT_CLINICAL_SOURCE_CONFIGURATION (ID);

alter table FILE_COLUMN 
    add index FK7BBDF379DDCA2BE1 (ANNOTATION_FIELD_DESCRIPTOR_ID), 
    add constraint FK7BBDF379DDCA2BE1 foreign key (ANNOTATION_FIELD_DESCRIPTOR_ID) references ANNOTATION_FIELD_DESCRIPTOR (ID);

alter table FILE_COLUMN 
    add index FK7BBDF37968A868E8 (ANNOTATION_FILE_ID), 
    add constraint FK7BBDF37968A868E8 foreign key (ANNOTATION_FILE_ID) references ANNOTATION_FILE (ID);
    
alter table STUDY_CONFIGURATION 
    add index FK1CC8D8C0A0CEA12A (STUDY_ID), 
    add constraint FK1CC8D8C0A0CEA12A foreign key (STUDY_ID) references STUDY (ID);

    