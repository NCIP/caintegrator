alter table ABSTRACT_LIST change VISIBILITY VISIBILITY varchar(255);
update ABSTRACT_LIST set VISIBILITY = "PRIVATE";
alter table ABSTRACT_LIST add column STUDY_CONFIGURATION_ID bigint after VISIBILITY;
alter table ABSTRACT_LIST add constraint FK_ABSTRACT_LIST_STUDY_CONFIGURATION
    foreign key (STUDY_CONFIGURATION_ID) references STUDY_CONFIGURATION (ID);