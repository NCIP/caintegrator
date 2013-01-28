update CSM_APPLICATION set APPLICATION_NAME = "caintegrator", APPLICATION_DESCRIPTION = "caIntegrator Data Portal" 
    where APPLICATION_NAME = "caintegrator2";
update CSM_PROTECTION_ELEMENT set PROTECTION_ELEMENT_NAME = "caintegrator", PROTECTION_ELEMENT_DESCRIPTION = "caIntegrator Data Portal" 
    where PROTECTION_ELEMENT_NAME = "caintegrator2";
    
update CSM_FILTER_CLAUSE set CLASS_NAME = 'gov.nih.nci.caintegrator.domain.translational.Study', 
    FILTER_CHAIN = 'gov.nih.nci.caintegrator.domain.translational.Study',
    TARGET_CLASS_NAME = 'gov.nih.nci.caintegrator.domain.translational.Study - self',
    GENERATED_SQL_USER = 'ID in (select table_name_csm_.ID
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
                                       and pe.object_id= ''gov.nih.nci.caintegrator.domain.translational.Study''
                                       and pe.attribute=''id''
                                       and p.privilege_name=''READ''
                                       and u.login_name=:USER_NAME
                                       and pe.application_id=:APPLICATION_ID))',
    GENERATED_SQL_GROUP = 'ID in (select table_name_csm_.ID
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
                                      AND pe.object_id= ''gov.nih.nci.caintegrator.domain.translational.Study''
                                      AND p.privilege_name=''READ''
                                      AND g.group_name IN
                                          (:GROUP_NAMES )
                                      AND pe.application_id=:APPLICATION_ID))'
    where FILTER_CLAUSE_ID = 1;
    
update CSM_FILTER_CLAUSE set CLASS_NAME = 'gov.nih.nci.caintegrator.domain.application.StudySubscription', 
    TARGET_CLASS_NAME = 'gov.nih.nci.caintegrator.domain.translational.Study - study', 
    GENERATED_SQL_USER = 'ID in (select table_name_csm_.ID
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
                                      and pe.object_id = ''gov.nih.nci.caintegrator.domain.translational.Study''
                                      and pe.attribute=''id''
                                      and p.privilege_name=''READ''
                                      and u.login_name=:USER_NAME
                                      and pe.application_id=:APPLICATION_ID))',
    GENERATED_SQL_GROUP = 'ID in (select table_name_csm_.ID
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
                               AND pe.object_id= ''gov.nih.nci.caintegrator.domain.translational.Study''
                               AND p.privilege_name=''READ''
                               AND g.group_name IN (:GROUP_NAMES )
                               AND pe.application_id=:APPLICATION_ID))'
    where FILTER_CLAUSE_ID = 2;
    
update CSM_FILTER_CLAUSE set CLASS_NAME = 'gov.nih.nci.caintegrator.application.study.StudyConfiguration', 
    TARGET_CLASS_NAME = 'gov.nih.nci.caintegrator.domain.translational.Study - study', 
    GENERATED_SQL_USER = 'ID in (select table_name_csm_.ID
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
                      and pe.object_id= ''gov.nih.nci.caintegrator.domain.translational.Study''
                      and pe.attribute=''id''
                      and p.privilege_name=''READ''
                      and u.login_name=:USER_NAME
                      and pe.application_id=:APPLICATION_ID))',
    GENERATED_SQL_GROUP = 'ID in (select table_name_csm_.ID
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
                       AND pe.object_id= ''gov.nih.nci.caintegrator.domain.translational.Study''
                       AND p.privilege_name=''READ''
                       AND g.group_name IN (:GROUP_NAMES )
                       AND pe.application_id=:APPLICATION_ID))'
    where FILTER_CLAUSE_ID = 3;
    
update CSM_FILTER_CLAUSE set CLASS_NAME = 'gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup', 
    FILTER_CHAIN = 'gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup',
    TARGET_CLASS_NAME = 'gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup - self', 
    GENERATED_SQL_USER = 'ID in (select table_name_csm_.ID
              from AUTHORIZED_STUDY_ELEMENTS_GROUP table_name_csm_
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
                                       and pe.object_id= ''gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup''
                                       and pe.attribute=''id''
                                       and p.privilege_name=''READ''
                                       and u.login_name=:USER_NAME
                                       and pe.application_id=:APPLICATION_ID))',
    GENERATED_SQL_GROUP = 'ID in (select table_name_csm_.ID
              from AUTHORIZED_STUDY_ELEMENTS_GROUP table_name_csm_
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
                                      AND pe.object_id= ''gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup''
                                      AND p.privilege_name=''READ''
                                      AND g.group_name IN
                                          (:GROUP_NAMES )
                                      AND pe.application_id=:APPLICATION_ID))'
    where FILTER_CLAUSE_ID = 4;
