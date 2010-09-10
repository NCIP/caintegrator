ALTER TABLE abstract_clinical_source_configuration ADD COLUMN STATUS VARCHAR(255) AFTER LAST_MODIFIED_DATE,
 ADD COLUMN STATUS_DESCRIPTION VARCHAR(255) AFTER STATUS;

update abstract_clinical_source_configuration, delimited_text_clinical_source_configuration, annotation_file
set abstract_clinical_source_configuration.STATUS = "LOADED"
where abstract_clinical_source_configuration.id = delimited_text_clinical_source_configuration.id
and delimited_text_clinical_source_configuration.annotation_file_id = annotation_file.id
and annotation_file.currently_loaded like 'true';

update abstract_clinical_source_configuration
set STATUS = "NOT_LOADED"
where STATUS is null;