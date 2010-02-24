ALTER TABLE ANNOTATION_FIELD_DESCRIPTOR ADD COLUMN ENTITY_TYPE VARCHAR(30) AFTER TYPE;

UPDATE ANNOTATION_FIELD_DESCRIPTOR
SET ENTITY_TYPE='IMAGESERIES'
WHERE ID in
  (SELECT distinct fc.annotation_field_descriptor_id
  FROM file_column fc
  WHERE fc.annotation_field_descriptor_id IS NOT NULL
    and fc.annotation_file_id in
    (select af.id
    from annotation_file af
    where af.id in
      (SELECT annotation_file_id FROM image_annotation_configuration i)));
      
UPDATE ANNOTATION_FIELD_DESCRIPTOR
SET ENTITY_TYPE='SUBJECT'
WHERE ID in
  (SELECT distinct fc.annotation_field_descriptor_id
  FROM file_column fc
  WHERE fc.annotation_field_descriptor_id IS NOT NULL
    and fc.annotation_file_id in
    (select af.id
    from annotation_file af
    where af.id in
      (SELECT annotation_file_id FROM delimited_text_clinical_source_configuration d)));
      
ALTER TABLE ANNOTATION_GROUP DROP COLUMN ANNOTATION_ENTITY_TYPE;