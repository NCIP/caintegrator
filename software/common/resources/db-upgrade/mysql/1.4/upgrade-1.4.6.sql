ALTER TABLE GENOMIC_DATA_SOURCE_CONFIGURATION ADD COLUMN IS_DATA_REFRESHED TINYINT;
UPDATE GENOMIC_DATA_SOURCE_CONFIGURATION SET IS_DATA_REFRESHED = 0;

ALTER TABLE STUDY_CONFIGURATION ADD COLUMN IS_DATA_REFRESHED TINYINT;
UPDATE STUDY_CONFIGURATION SET IS_DATA_REFRESHED = 0;