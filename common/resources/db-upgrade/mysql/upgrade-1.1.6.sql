create table EXTERNAL_LINK_LIST (
    ID bigint not null auto_increment, 
    STUDY_CONFIGURATION_ID bigint, 
    LIST_INDEX integer,
    NAME varchar(255),
    DESCRIPTION varchar(500),
    FILE_NAME varchar(100),
    primary key (ID)
) ENGINE=InnoDB;

create table EXTERNAL_LINK (
    ID bigint not null auto_increment, 
    EXTERNAL_LINK_LIST_ID bigint, 
    LIST_INDEX integer,
    NAME varchar(255),
    URL varchar(255),
    primary key (ID)
) ENGINE=InnoDB;

ALTER TABLE EXTERNAL_LINK_LIST ADD CONSTRAINT FK_EXTERNAL_LINK_LIST_STUDY_CONFIGURATION 
    FOREIGN KEY (STUDY_CONFIGURATION_ID) REFERENCES STUDY_CONFIGURATION (ID);
    
ALTER TABLE EXTERNAL_LINK ADD CONSTRAINT FK_EXTERNAL_LINK_EXTERNAL_LINK_LIST 
    FOREIGN KEY (EXTERNAL_LINK_LIST_ID) REFERENCES EXTERNAL_LINK_LIST (ID);