ALTER TABLE segment_data MODIFY COLUMN CALLS_VALUE INTEGER DEFAULT NULL;

ALTER TABLE copy_number_alteration_criterion ADD COLUMN COPY_NUMBER_CRITERION_TYPE VARCHAR(45) AFTER PLATFORM_NAME;

update copy_number_alteration_criterion 
    set COPY_NUMBER_CRITERION_TYPE="SEGMENT_VALUE";
    
CREATE TABLE CALLS_VALUES_FOR_CRITERION (
  COPY_NUMBER_ALTERATION_ID BIGINT NOT NULL,
  CALLS_VALUE INTEGER NOT NULL,
  PRIMARY KEY (`COPY_NUMBER_ALTERATION_ID`, `CALLS_VALUE`)
) ENGINE=InnoDB;

ALTER TABLE CALLS_VALUES_FOR_CRITERION ADD CONSTRAINT FK_CALLS_VALUES_FOR_CRITERION_1 
    FOREIGN KEY (COPY_NUMBER_ALTERATION_ID) REFERENCES copy_number_alteration_criterion (ID);