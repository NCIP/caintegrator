ALTER TABLE QUERY ADD COLUMN COPY_NUMBER_CRITERION_TYPE VARCHAR(45) AFTER REPORTER_TYPE;

update QUERY 
    set COPY_NUMBER_CRITERION_TYPE="SEGMENT_VALUE";