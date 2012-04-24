#
# The following entries creates a super admin application incase you decide
# to use this database to run UPT also. In that case you need to provide
# the project login id and name for the super admin.
# However in incase you are using this database just to host the application's
# authorization schema, these enteries are not used and hence they can be left as
# it is.
#

insert into CSM_APPLICATION(APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE)
values ("csmupt","CSM UPT Super Admin Application",0,0,sysdate());

insert into CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,PASSWORD,UPDATE_DATE)
values (1,"admin","UPT","Administrator","zJPWCwDeSgG8j2uyHEABIQ==",sysdate());

insert into CSM_PROTECTION_ELEMENT(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("csmupt","CSM UPT Super Admin Application Protection Element","csmupt",1,sysdate());

insert into CSM_USER_PE(PROTECTION_ELEMENT_ID,USER_ID)
values(1,1);

#
# The following entry is for your application.
# Replace <<application_context_name>> with your application name.
#

INSERT INTO CSM_APPLICATION(APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,CSM_VERSION,UPDATE_DATE)
VALUES ("caintegrator2","caIntegrator2 Data Portal",0,0,"4.2",sysdate());

insert into CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,PASSWORD,UPDATE_DATE)
values (2,"cai2admin","cai2","Admin","zJPWCwDeSgG8j2uyHEABIQ==",sysdate());

insert into CSM_PROTECTION_ELEMENT(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("caintegrator2","caintegrator2 Data Portal","caintegrator2",1,sysdate());

insert into CSM_USER_PE(PROTECTION_ELEMENT_ID,USER_ID)
values(2,2);

#
# The following entries are Common Set of Privileges
#

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("CREATE","This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("ACCESS","This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("READ","This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("WRITE","This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("UPDATE","This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("DELETE","This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc", sysdate());

INSERT INTO CSM_PRIVILEGE (privilege_name, privilege_description, update_date)
VALUES("EXECUTE","This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc", sysdate());

#
# The following entries are for inserting a generic StudyManager and ResearchInvestigator into
# the tables for development purposes
#

INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE)
VALUES (3,'ncimanager','Study','Manager','','','','','zJPWCwDeSgG8j2uyHEABIQ==','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE)
VALUES (4,'nciinvestigator','Research','Investigator','','','','','zJPWCwDeSgG8j2uyHEABIQ==','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE)
VALUES (5,'casuser','CAS','User','','','','','zJPWCwDeSgG8j2uyHEABIQ==','',null,null,sysdate());

INSERT INTO CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID)
VALUES (1,'Platforms','Secures the Platform object',2,0,sysdate(),null);
INSERT INTO CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID)
VALUES (2,'NCI Protected Studies','Studies that are protected for the NCI Groups.',2,0,sysdate(),null);


INSERT INTO CSM_ROLE (ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE)
VALUES (1,'STUDY_MANAGER_ROLE','This is the role of the study manager.',2,1,sysdate());
INSERT INTO CSM_ROLE (ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE)
VALUES (2,'STUDY_INVESTIGATOR_ROLE','The role for Study Investigators',2,1,sysdate());
INSERT INTO CSM_ROLE (ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE)
VALUES (3,'PLATFORM_MANAGER_ROLE','The role for the platform manager',2,1,sysdate());

INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (1,1,6);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (2,1,5);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (3,1,4);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (4,1,2);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (5,1,1);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (6,1,7);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (7,1,3);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (8,2,2);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (9,2,3);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (10,2,7);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (11,3,1);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (12,3,2);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (13,3,3);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (14,3,4);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (15,3,5);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (16,3,6);
INSERT INTO CSM_ROLE_PRIVILEGE (ROLE_PRIVILEGE_ID,ROLE_ID,PRIVILEGE_ID)
VALUES (17,3,7);

INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID)
VALUES (1,'Platform Manager Group','The platform manager group.',sysdate(),2);
INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID)
VALUES (2,'NCI Study Managers','Study Managers who can create/modify any NCI studies.',sysdate(),2);
INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID)
VALUES (3,'NCI Study Investigators','Study investigators for the NCI studies.',sysdate(),2);

INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)
VALUES (1,3,1);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)
VALUES (2,3,2);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)
VALUES (3,4,3);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)
VALUES (4,5,3);

INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE)
VALUES (1,null,1,3,1,sysdate());
INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE)
VALUES (2,null,2,1,2,sysdate());
INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE)
VALUES (3,null,3,2,2,sysdate());

INSERT INTO CSM_FILTER_CLAUSE (
    FILTER_CLAUSE_ID,
    CLASS_NAME,
    FILTER_CHAIN,
    TARGET_CLASS_NAME,
    TARGET_CLASS_ATTRIBUTE_NAME,
    TARGET_CLASS_ATTRIBUTE_TYPE,
    TARGET_CLASS_ALIAS,
    TARGET_CLASS_ATTRIBUTE_ALIAS,
    GENERATED_SQL_USER,
    GENERATED_SQL_GROUP,
    APPLICATION_ID,UPDATE_DATE)
VALUES (
    1,
    'gov.nih.nci.caintegrator2.domain.translational.Study',
    'gov.nih.nci.caintegrator2.domain.translational.Study',
    'gov.nih.nci.caintegrator2.domain.translational.Study - self',
    'id',
    'java.lang.Long',
    '',
    '',
    'ID in (select table_name_csm_.ID
              from STUDY table_name_csm_
             where table_name_csm_.ID in
                   (select pe.attribute_value
                      from csm_protection_group pg,
                           csm_protection_element pe,
                           csm_pg_pe pgpe,
                           csm_user_group_role_pg ugrpg,
                           csm_user u,
                           csm_role_privilege rp,
                           csm_role r,
                           csm_privilege p
                     where ugrpg.role_id = r.role_id
                       and ugrpg.user_id = u.user_id
                       and ugrpg.protection_group_id = ANY
                           (select pg1.protection_group_id
                              from csm_protection_group pg1
                             where pg1.protection_group_id = pg.protection_group_id
                                or pg1.protection_group_id =
                                   (select pg2.parent_protection_group_id
                                      from csm_protection_group pg2
                                     where pg2.protection_group_id = pg.protection_group_id))
                                       and pg.protection_group_id = pgpe.protection_group_id
                                       and pgpe.protection_element_id = pe.protection_element_id
                                       and r.role_id = rp.role_id
                                       and rp.privilege_id = p.privilege_id
                                       and pe.object_id= ''gov.nih.nci.caintegrator2.domain.translational.Study''
                                       and pe.attribute=''id''
                                       and p.privilege_name=''READ''
                                       and u.login_name=:USER_NAME
                                       and pe.application_id=:APPLICATION_ID))',
    'ID in (select table_name_csm_.ID
              from STUDY table_name_csm_
             where table_name_csm_.ID in
                   (SELECT Distinct pe.attribute_value
                      FROM CSM_PROTECTION_GROUP pg,
                           CSM_PROTECTION_ELEMENT pe,
                           CSM_PG_PE pgpe,
                           CSM_USER_GROUP_ROLE_PG ugrpg,
                           CSM_GROUP g,
                           CSM_ROLE_PRIVILEGE rp,
                           CSM_ROLE r,
                           CSM_PRIVILEGE p
                     WHERE ugrpg.role_id = r.role_id
                       AND ugrpg.group_id = g.group_id
                       AND ugrpg.protection_group_id = ANY
                          (select pg1.protection_group_id
                             from csm_protection_group pg1
                            where pg1.protection_group_id = pg.protection_group_id
                               OR pg1.protection_group_id =
                                  (select pg2.parent_protection_group_id
                                     from csm_protection_group pg2
                                    where pg2.protection_group_id = pg.protection_group_id) )
                                      AND pg.protection_group_id = pgpe.protection_group_id
                                      AND pgpe.protection_element_id = pe.protection_element_id
                                      AND r.role_id = rp.role_id
                                      AND rp.privilege_id = p.privilege_id
                                      AND pe.object_id= ''gov.nih.nci.caintegrator2.domain.translational.Study''
                                      AND p.privilege_name=''READ''
                                      AND g.group_name IN
                                          (:GROUP_NAMES )
                                      AND pe.application_id=:APPLICATION_ID))',2,sysdate());
INSERT INTO CSM_FILTER_CLAUSE (
    FILTER_CLAUSE_ID,
    CLASS_NAME,
    FILTER_CHAIN,
    TARGET_CLASS_NAME,
    TARGET_CLASS_ATTRIBUTE_NAME,
    TARGET_CLASS_ATTRIBUTE_TYPE,
    TARGET_CLASS_ALIAS,
    TARGET_CLASS_ATTRIBUTE_ALIAS,
    GENERATED_SQL_USER,
    GENERATED_SQL_GROUP,
    APPLICATION_ID,UPDATE_DATE)
VALUES (
    2,
    'gov.nih.nci.caintegrator2.domain.application.StudySubscription',
    'study',
    'gov.nih.nci.caintegrator2.domain.translational.Study - study',
    'id',
    'java.lang.Long',
    '',
    '',
    'ID in (select table_name_csm_.ID
              from STUDY_SUBSCRIPTION table_name_csm_ inner join STUDY study1_ on table_name_csm_.STUDY_ID=study1_.ID
             where study1_.ID in
                  (select pe.attribute_value
                     from csm_protection_group pg,
                          csm_protection_element pe,
                          csm_pg_pe pgpe,
                          csm_user_group_role_pg ugrpg,
                          csm_user u,
                          csm_role_privilege rp,
                          csm_role r,
                          csm_privilege p
                    where ugrpg.role_id = r.role_id
                      and ugrpg.user_id = u.user_id
                      and ugrpg.protection_group_id = ANY
                          (select pg1.protection_group_id
                             from csm_protection_group pg1
                            where pg1.protection_group_id = pg.protection_group_id
                               or pg1.protection_group_id =
                                  (select pg2.parent_protection_group_id
                                     from csm_protection_group pg2
                                    where pg2.protection_group_id = pg.protection_group_id))
                                      and pg.protection_group_id = pgpe.protection_group_id
                                      and pgpe.protection_element_id = pe.protection_element_id
                                      and r.role_id = rp.role_id
                                      and rp.privilege_id = p.privilege_id
                                      and pe.object_id = ''gov.nih.nci.caintegrator2.domain.translational.Study''
                                      and pe.attribute=''id''
                                      and p.privilege_name=''READ''
                                      and u.login_name=:USER_NAME
                                      and pe.application_id=:APPLICATION_ID))',
    'ID in (select table_name_csm_.ID
              from STUDY_SUBSCRIPTION table_name_csm_ inner join STUDY study1_ on table_name_csm_.STUDY_ID=study1_.ID
             where study1_.ID in
                   (SELECT Distinct pe.attribute_value
                      FROM CSM_PROTECTION_GROUP pg,
                           CSM_PROTECTION_ELEMENT pe,
                           CSM_PG_PE pgpe,
                           CSM_USER_GROUP_ROLE_PG ugrpg,
                           CSM_GROUP g,
                           CSM_ROLE_PRIVILEGE rp,
                           CSM_ROLE r,
                           CSM_PRIVILEGE p
                     WHERE ugrpg.role_id = r.role_id
                       AND ugrpg.group_id = g.group_id
                       AND ugrpg.protection_group_id = ANY
                           (select pg1.protection_group_id
                              from csm_protection_group pg1
                             where pg1.protection_group_id = pg.protection_group_id
                                OR pg1.protection_group_id =
                                   (select pg2.parent_protection_group_id
                                      from csm_protection_group pg2
                                     where pg2.protection_group_id = pg.protection_group_id) )
                               AND pg.protection_group_id = pgpe.protection_group_id
                               AND pgpe.protection_element_id = pe.protection_element_id
                               AND r.role_id = rp.role_id
                               AND rp.privilege_id = p.privilege_id
                               AND pe.object_id= ''gov.nih.nci.caintegrator2.domain.translational.Study''
                               AND p.privilege_name=''READ''
                               AND g.group_name IN (:GROUP_NAMES )
                               AND pe.application_id=:APPLICATION_ID))',2,sysdate());

INSERT INTO CSM_FILTER_CLAUSE (
    FILTER_CLAUSE_ID,
    CLASS_NAME,
    FILTER_CHAIN,
    TARGET_CLASS_NAME,
    TARGET_CLASS_ATTRIBUTE_NAME,
    TARGET_CLASS_ATTRIBUTE_TYPE,
    TARGET_CLASS_ALIAS,
    TARGET_CLASS_ATTRIBUTE_ALIAS,
    GENERATED_SQL_USER,
    GENERATED_SQL_GROUP,
    APPLICATION_ID,
    UPDATE_DATE)
VALUES (
    3,
    'gov.nih.nci.caintegrator2.application.study.StudyConfiguration',
    'study',
    'gov.nih.nci.caintegrator2.domain.translational.Study - study',
    'id',
    'java.lang.Long',
    '',
    '',
    'ID in (select table_name_csm_.ID
              from STUDY_CONFIGURATION table_name_csm_ inner join STUDY study1_ on table_name_csm_.STUDY_ID=study1_.ID
             where study1_.ID in
                  (select pe.attribute_value
                     from csm_protection_group pg,
                          csm_protection_element pe,
                          csm_pg_pe pgpe,
                          csm_user_group_role_pg ugrpg,
                          csm_user u,
                          csm_role_privilege rp,
                          csm_role r,
                          csm_privilege p
                    where ugrpg.role_id = r.role_id
                      and ugrpg.user_id = u.user_id
                      and ugrpg.protection_group_id = ANY
                          (select pg1.protection_group_id
                             from csm_protection_group pg1
                            where pg1.protection_group_id = pg.protection_group_id
                               or pg1.protection_group_id =
                                  (select pg2.parent_protection_group_id
                                     from csm_protection_group pg2
                                    where pg2.protection_group_id = pg.protection_group_id))
                      and pg.protection_group_id = pgpe.protection_group_id
                      and pgpe.protection_element_id = pe.protection_element_id
                      and r.role_id = rp.role_id
                      and rp.privilege_id = p.privilege_id
                      and pe.object_id= ''gov.nih.nci.caintegrator2.domain.translational.Study''
                      and pe.attribute=''id''
                      and p.privilege_name=''READ''
                      and u.login_name=:USER_NAME
                      and pe.application_id=:APPLICATION_ID))',
    'ID in (select table_name_csm_.ID
              from STUDY_CONFIGURATION table_name_csm_ inner join STUDY study1_ on table_name_csm_.STUDY_ID=study1_.ID
             where study1_.ID in
                   (SELECT Distinct pe.attribute_value
                      FROM CSM_PROTECTION_GROUP pg,
                           CSM_PROTECTION_ELEMENT pe,
                           CSM_PG_PE pgpe,
                           CSM_USER_GROUP_ROLE_PG ugrpg,
                           CSM_GROUP g,
                           CSM_ROLE_PRIVILEGE rp,
                           CSM_ROLE r,
                           CSM_PRIVILEGE p
                     WHERE ugrpg.role_id = r.role_id
                       AND ugrpg.group_id = g.group_id
                       AND ugrpg.protection_group_id = ANY
                           (select pg1.protection_group_id
                              from csm_protection_group pg1
                             where pg1.protection_group_id = pg.protection_group_id
                                OR pg1.protection_group_id =
                                   (select pg2.parent_protection_group_id
                                      from csm_protection_group pg2
                                     where pg2.protection_group_id = pg.protection_group_id) )
                       AND pg.protection_group_id = pgpe.protection_group_id
                       AND pgpe.protection_element_id = pe.protection_element_id
                       AND r.role_id = rp.role_id
                       AND rp.privilege_id = p.privilege_id
                       AND pe.object_id= ''gov.nih.nci.caintegrator2.domain.translational.Study''
                       AND p.privilege_name=''READ''
                       AND g.group_name IN (:GROUP_NAMES )
                       AND pe.application_id=:APPLICATION_ID))',2,sysdate());


COMMIT;
