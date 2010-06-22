CREATE TABLE ABSTRACT_ANNOTATION_MASK 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    ANNOTATION_FIELD_DESCRIPTOR_ID BIGINT,
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE MAX_NUMBER_MASK 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    MAX_NUMBER double precision, 
    primary key (ID)
) TYPE=InnoDB;

CREATE TABLE NUMERIC_RANGE_MASK 
(
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NUMERIC_RANGE integer, 
    primary key (ID)
) TYPE=InnoDB;

ALTER TABLE ABSTRACT_ANNOTATION_MASK ADD CONSTRAINT FK_ABSTRACT_ANNOTATION_MASK_ANNOTATION_FIELD_DESCRIPTOR
    FOREIGN KEY (ANNOTATION_FIELD_DESCRIPTOR_ID) references ANNOTATION_FIELD_DESCRIPTOR (ID);