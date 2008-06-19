create table STUDY_CONFIGURATION (
    ID bigint not null auto_increment, 
    VISIBILITY integer, 
    STATUS integer, 
    STUDY_ID bigint unique, 
    primary key (ID)
) ENGINE=InnoDB;


create table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION (
    ID bigint not null auto_increment, 
    ANNOTATION_FIELD_DESCRIPTOR_ID bigint unique, 
    STUDY_CONFIGURATION_ID bigint unique, 
    primary key (ID)
) ENGINE=InnoDB;


create table ANNOTATION_FIELD_DESCRIPTOR (
    ID bigint not null auto_increment, 
    NAME varchar(255), 
    KEYWORDS varchar(255), 
    TYPE varchar(255), 
    ANNOTATION_DEFINITION_ID bigint unique, 
    primary key (ID)
) ENGINE=InnoDB;


alter table STUDY_CONFIGURATION 
    add index FK_STUDY_CONFIGURATION_STUDY (study_ID), 
    add constraint FK_STUDY_CONFIGURATION_STUDY foreign key (study_ID) references STUDY (ID);

alter table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION 
    add index FKBAE4F601DDCA2BE1 (ANNOTATION_FIELD_DESCRIPTOR_ID), 
    add constraint FKBAE4F601DDCA2BE1 foreign key (ANNOTATION_FIELD_DESCRIPTOR_ID) references ANNOTATION_FIELD_DESCRIPTOR (ID);
alter table ABSTRACT_CLINICAL_SOURCE_CONFIGURATION 
    add index FKBAE4F601C0ADCD6 (STUDY_CONFIGURATION_ID), 
    add constraint FKBAE4F601C0ADCD6 foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);
    
alter table ANNOTATION_FIELD_DESCRIPTOR 
    add index FK5D1F2F04AF56BCDA (ANNOTATION_DEFINITION_ID), 
    add constraint FK5D1F2F04AF56BCDA foreign key (ANNOTATION_DEFINITION_ID) references ANNOTATION_DEFINITION (ID);

    