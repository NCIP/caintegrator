update genomic_data_source_configuration set PLATFORM_VENDOR = "AFFYMETRIX"
    where PLATFORM_VENDOR = "Affymetrix";
update genomic_data_source_configuration set PLATFORM_VENDOR = "AGILENT"
    where PLATFORM_VENDOR = "Agilent";

ALTER TABLE genomic_data_source_configuration ADD COLUMN LOADING_TYPE VARCHAR(50) AFTER PLATFORM_NAME;

update genomic_data_source_configuration set LOADING_TYPE = "PARSED_DATA"
    where USE_SUPPLEMENTAL_FILES = "0" and DATA_TYPE = "EXPRESSION" and PLATFORM_VENDOR = "AFFYMETRIX";
update genomic_data_source_configuration set LOADING_TYPE = "CNCHP"
    where USE_SUPPLEMENTAL_FILES = "1" and DATA_TYPE = "COPY_NUMBER" and PLATFORM_VENDOR = "AFFYMETRIX";
update genomic_data_source_configuration set LOADING_TYPE = "CHP"
    where USE_SUPPLEMENTAL_FILES = "1" and DATA_TYPE = "SNP" and PLATFORM_VENDOR = "AFFYMETRIX";
update genomic_data_source_configuration set LOADING_TYPE = "SINGLE_SAMPLE_PER_FILE"
    where USE_SUPPLEMENTAL_FILES = "1" and SINGLE_DATA_FILE = "0" and PLATFORM_VENDOR = "AGILENT";
update genomic_data_source_configuration set LOADING_TYPE = "MULTI_SAMPLE_PER_FILE"
    where USE_SUPPLEMENTAL_FILES = "1" and SINGLE_DATA_FILE = "1" and PLATFORM_VENDOR = "AGILENT";

ALTER TABLE genomic_data_source_configuration DROP COLUMN USE_SUPPLEMENTAL_FILES;
ALTER TABLE genomic_data_source_configuration DROP COLUMN SINGLE_DATA_FILE;