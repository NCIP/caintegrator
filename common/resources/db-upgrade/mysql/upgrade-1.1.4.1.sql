ALTER TABLE GENOMIC_DATA_SOURCE_CONFIGURATION DROP COLUMN COPY_NUMBER_SINGLE_DATA_FILE;
ALTER TABLE GENOMIC_DATA_SOURCE_CONFIGURATION
   ADD COLUMN COPY_NUMBER_SINGLE_DATA_FILE tinyint DEFAULT 0
   AFTER COPY_NUMBER_RANDOM_NUMBER_SEED;