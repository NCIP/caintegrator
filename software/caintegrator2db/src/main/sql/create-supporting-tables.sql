create table STUDY_CONFIGURATION (
    id bigint not null auto_increment, 
    visibility integer, 
    study_ID bigint, 
    primary key (id)
) ENGINE=InnoDB;

alter table STUDY_CONFIGURATION 
    add index FK_STUDY_CONFIGURATION_STUDY (study_ID), 
    add constraint FK_STUDY_CONFIGURATION_STUDY foreign key (study_ID) references STUDY (ID);
