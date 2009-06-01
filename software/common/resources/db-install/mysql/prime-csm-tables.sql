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

insert into CSM_USER (LOGIN_NAME,FIRST_NAME,LAST_NAME,PASSWORD,UPDATE_DATE)
values ("admin","UPT","Administrator","zJPWCwDeSgG8j2uyHEABIQ==",sysdate());
 
insert into CSM_PROTECTION_ELEMENT(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("csmupt","CSM UPT Super Admin Application Protection Element","csmupt",1,sysdate());

insert into CSM_USER_PE(PROTECTION_ELEMENT_ID,USER_ID)
values(1,1);

# 
# The following entry is for your application. 
# Replace <<application_context_name>> with your application name.
#

INSERT INTO CSM_APPLICATION(APPLICATION_NAME,APPLICATION_DESCRIPTION,DECLARATIVE_FLAG,ACTIVE_FLAG,UPDATE_DATE)
VALUES ("caintegrator2","caIntegrator2 Data Portal",0,0,sysdate());

insert into CSM_USER (LOGIN_NAME,FIRST_NAME,LAST_NAME,PASSWORD,UPDATE_DATE)
values ("cai2admin","cai2","Admin","SRsSeN2nfbO9DFmxlyl4eg==",sysdate());

insert into CSM_PROTECTION_ELEMENT(PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,APPLICATION_ID,UPDATE_DATE)
values("caintegrator2","caintegrator2 Data Portal","caintegrator2",2,sysdate());

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
VALUES (3,'manager','Study','Manager','','','','','7u06TntO8s8=','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE) 
VALUES (4,'investigator','Research','Investigator','','','','','OiDRud3e8kHh1x9awEGfTg==','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE) 
VALUES (5,'manager2','Study','Manager2','','','','','bo+k4jG9UNS8j2uyHEABIQ==','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE) 
VALUES (6,'gumanager','Georgetown',' Study Manager','','','','','uBEgxXq3a1/eRIv4iO8hcA==','',null,null,sysdate());
INSERT INTO CSM_USER (USER_ID,LOGIN_NAME,FIRST_NAME,LAST_NAME,ORGANIZATION,DEPARTMENT,TITLE,PHONE_NUMBER,PASSWORD,EMAIL_ID,START_DATE,END_DATE,UPDATE_DATE) 
VALUES (7,'nbiamanager','NBIA','Study Manager','','','','','VrUWGf/kLKgxHild/O+jbg==','',null,null,sysdate());


INSERT INTO CSM_PROTECTION_ELEMENT (PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,ATTRIBUTE,ATTRIBUTE_VALUE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE) 
VALUES (3,'MODIFY_STUDY','This is to make modification of the Study object protected. (Remove after user story 21385 is completed)','gov.nih.nci.caintegrator2.domain.translational.Study','','','',2,sysdate());

INSERT INTO CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID) 
VALUES (1,'STUDY_MODIFICATION','This has to do with all elements dealing with Study Modification (Remove after user story 21385 is completed)',2,0,sysdate(),null);
INSERT INTO CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID) 
VALUES (2,'NCI Protected Studies','Studies that are protected for the NCI Groups.',2,0,sysdate(),null);

INSERT INTO CSM_PG_PE (PG_PE_ID,PROTECTION_GROUP_ID,PROTECTION_ELEMENT_ID,UPDATE_DATE) 
VALUES (1,1,3,sysdate());

INSERT INTO CSM_ROLE (ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) 
VALUES (1,'STUDY_MANAGER_ROLE','This is the role of the study manager.',2,1,sysdate());
INSERT INTO CSM_ROLE (ROLE_ID,ROLE_NAME,ROLE_DESCRIPTION,APPLICATION_ID,ACTIVE_FLAG,UPDATE_DATE) 
VALUES (2,'STUDY_INVESTIGATOR_ROLE','The role for Study Investigators',2,1,sysdate());

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

INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID) 
VALUES (1,'Study Manager Group','This group is for managing studies (Remove after user story 21385 is completed)',sysdate(),2);
INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID) 
VALUES (2,'NCI Study Managers','Study Managers who can create/modify any NCI studies.',sysdate(),2);
INSERT INTO CSM_GROUP (GROUP_ID,GROUP_NAME,GROUP_DESC,UPDATE_DATE,APPLICATION_ID) 
VALUES (3,'NCI Study Investigators','Study investigators for the NCI studies.',sysdate(),2);


INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID) 
VALUES (1,3,1);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID) 
VALUES (2,5,1);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID) 
VALUES (3,6,1);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID) 
VALUES (4,7,1);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)  
VALUES (5,3,2);
INSERT INTO CSM_USER_GROUP (USER_GROUP_ID,USER_ID,GROUP_ID)  
VALUES (6,4,3);

INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) 
VALUES (1,null,1,1,1,sysdate());
INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) 
VALUES (2,null,2,1,2,sysdate());
INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,USER_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) 
VALUES (3,null,3,2,2,sysdate());

COMMIT;