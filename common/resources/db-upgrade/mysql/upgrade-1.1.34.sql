ALTER TABLE GENOMIC_DATA_SOURCE_CONFIGURATION
    CHANGE DNA_ANALYSIS_SINGLE_DATA_FILE SINGLE_DATA_FILE tinyint(4) AFTER PLATFORM_NAME;

ALTER TABLE GENOMIC_DATA_SOURCE_CONFIGURATION
    ADD USE_SUPPLEMENTAL_FILES tinyint(4) AFTER PLATFORM_NAME;

UPDATE GENOMIC_DATA_SOURCE_CONFIGURATION SET USE_SUPPLEMENTAL_FILES = 1;
UPDATE GENOMIC_DATA_SOURCE_CONFIGURATION SET USE_SUPPLEMENTAL_FILES = 0 WHERE PLATFORM_VENDOR="Affymetrix" AND DATA_TYPE="EXPRESSION";