CREATE TABLE AUTHORIZED_QUERY (
    ID BIGINT(20) NOT NULL auto_increment,
    AUTHORIZED_STUDY_ELEMENTS_GROUP_ID BIGINT(20) default NULL,
    QUERY_ID BIGINT(20) default NULL,
    LIST_INDEX int(11) default NULL,
    PRIMARY KEY (ID),
    KEY IDX_AUTHORIZED_QUERY (AUTHORIZED_STUDY_ELEMENTS_GROUP_ID)
) ENGINE=InnoDB;

ALTER TABLE AUTHORIZED_QUERY ADD CONSTRAINT FK_AUTHORIZED_QUERY
    FOREIGN KEY (AUTHORIZED_STUDY_ELEMENTS_GROUP_ID) REFERENCES AUTHORIZED_STUDY_ELEMENTS_GROUP (ID);

DROP TABLE IF EXISTS AUTHORIZED_CRITERION;