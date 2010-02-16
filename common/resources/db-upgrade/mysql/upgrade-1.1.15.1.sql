DROP PROCEDURE IF EXISTS create_afd_for_identifier_cols;

CREATE PROCEDURE create_afd_for_identifier_cols()
BEGIN
DECLARE cur_end INT;
DECLARE fc_id, afd_id LONG;
DECLARE fc_name VARCHAR(255);
DECLARE cur_1 CURSOR FOR SELECT f.id, f.name FROM file_column f, annotation_file af where  af.identifier_column_id = f.id;
DECLARE CONTINUE HANDLER FOR NOT FOUND
  SET cur_end = 1;

open cur_1;
fc_loop : LOOP
  FETCH cur_1 INTO fc_id, fc_name;

  IF cur_end = 1 THEN
    LEAVE fc_loop;
  END IF;

  INSERT into ANNOTATION_FIELD_DESCRIPTOR (name,type,shown_in_browse) VALUES (fc_name, 'IDENTIFIER', '1');
  UPDATE file_column SET ANNOTATION_FIELD_DESCRIPTOR_ID = (select max(id) from annotation_field_descriptor) where ID = fc_id;

END LOOP fc_loop;
CLOSE cur_1;

END;

CALL create_afd_for_identifier_cols();