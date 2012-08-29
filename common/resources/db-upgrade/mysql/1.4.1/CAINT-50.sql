CREATE PROCEDURE assignCai2AdminGroups()
BEGIN
    DECLARE group_count int;
    select count(*) into group_count from csm_user_group where user_id = (select user_id from csm_user where login_name = 'cai2admin');
    IF (group_count = 0) THEN
        insert into csm_user_group (user_id, group_id) values
            ((select user_id from csm_user where login_name = 'cai2admin'), 
            (select group_id from csm_group where group_name = 'Platform Manager Group'));
    END IF;
END;
