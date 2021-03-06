ALTER TABLE IMAGE_ANNOTATION_CONFIGURATION ADD COLUMN ANNOTATION_UPLOAD_TYPE VARCHAR(50) AFTER ANNOTATION_FILE_ID;

ALTER TABLE IMAGE_ANNOTATION_CONFIGURATION ADD COLUMN AIM_SERVER_CONNECTION_PROFILE_ID bigint AFTER ANNOTATION_FILE_ID;
    
ALTER TABLE IMAGE_ANNOTATION_CONFIGURATION 
    ADD CONSTRAINT FK_IMAGE_ANNOTATION_AIM_SERVER_CONNECTION FOREIGN KEY (AIM_SERVER_CONNECTION_PROFILE_ID) REFERENCES SERVER_CONNECTION_PROFILE (ID);

UPDATE IMAGE_ANNOTATION_CONFIGURATION set ANNOTATION_UPLOAD_TYPE="FILE";

INSERT INTO CONFIGURATION_PARAMETER (PARAMETER, RAW_VALUE) VALUES
    ("AIM_URL", "http://node01.cci.emory.edu/wsrf/services/cagrid/AIMTCGADataService");