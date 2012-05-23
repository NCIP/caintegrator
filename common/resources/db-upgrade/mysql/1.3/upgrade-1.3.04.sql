CREATE TABLE EXPRESSION_LEVEL_CRITERION 
(
    ID BIGINT NOT NULL AUTO_INCREMENT, 
    LOWER_LIMIT FLOAT, 
    UPPER_LIMIT FLOAT, 
    CONTROL_SAMPLE_SET_NAME varchar(255),
    GENE_SYMBOL varchar(6000),
    PLATFORM_NAME varchar(255),
    RANGE_TYPE varchar(255), 
    primary key (ID)
) Engine=InnoDB;