DELETE FROM annotation_field_descriptor WHERE id NOT IN (SELECT fc.annotation_field_descriptor_id FROM file_column fc WHERE fc.annotation_field_descriptor_id IS NOT NULL);

UPDATE ANNOTATION_FIELD_DESCRIPTOR afd SET afd.TYPE = 'ANNOTATION';