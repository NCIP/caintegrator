/* 
address
*/
USE test;

CREATE TABLE `address` (
  `id` int(8) NOT NULL default '0',
  `zip` VARCHAR(50)  DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `address` VALUES ('1','Zip1');
COMMIT;
INSERT INTO `address` VALUES ('2','Zip2');
COMMIT;
INSERT INTO `address` VALUES ('3','Zip3');
COMMIT;
INSERT INTO `address` VALUES ('4','Zip4');
COMMIT;
INSERT INTO `address` VALUES ('5','Zip5');
COMMIT;

/* 
album
*/
CREATE TABLE `album` (
  `id` decimal(8,2) NOT NULL default '0.00',
  `title` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
album_song
*/
CREATE TABLE `album_song` (
  `album_id` int(8) NOT NULL default '0',
  `song_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`album_id`,`song_id`),
  UNIQUE KEY `uq_album_song_album_id` (`album_id`)
) ;


/* 
all_data_type
*/
CREATE TABLE `all_data_type` (
  `id` int(8) NOT NULL default '0',
  `int_value` int(8) default NULL,
  `string_value` VARCHAR(50) DEFAULT NULL,
  `double_value` decimal(8,2) default NULL,
  `float_value` decimal(8,2) default NULL,
  `date_value` datetime default NULL,
  `boolean_value` CHAR(1) DEFAULT NULL,
  `clob_value` LONGTEXT,
  `character_value` CHAR(1) DEFAULT NULL,
  `long_value` decimal(38,0) default NULL,
  `double_primitive_value` decimal(8,2) default NULL,
  `int_primitive_value` int(8) default NULL,
  `date_primitive_value` datetime default NULL,
  `string_primitive_value` VARCHAR(50) DEFAULT NULL,
  `float_primitive_value` decimal(8,2) default NULL,
  `boolean_primitive_value` CHAR(1) DEFAULT NULL,
  `character_primitive_value` CHAR(1) DEFAULT NULL,
  `long_primitive_value` decimal(38,0) default NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `all_data_type` VALUES ('1','-1',',./-+/*&&()||==\'"%"!\\\'','-1.10','1.10','20111111 00:00:00','1','0123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340112340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012','','','','','','','','','','');
COMMIT;
INSERT INTO `all_data_type` VALUES ('2','0','\'Steve\'s Test\'','0.00','222.22','20121212 00:00:00','0','0123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340112340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012','','','','','','','','','','');
COMMIT;
INSERT INTO `all_data_type` VALUES ('3','1','~!@#$%^&*()_+-={}|:"<>?[]\\\';\',./-+/*&&()||==\'"%"!\\\'\'','1.10','333.33','20030303 00:00:00','1','0123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340112340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012','','','','','','','','','','');
COMMIT;
INSERT INTO `all_data_type` VALUES ('4','10000','01234567890123456789012345678901234567890123456789','10000.00','444.44','20040404 00:00:00','0','0123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340112340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012','','','','','','','','','','');
COMMIT;
INSERT INTO `all_data_type` VALUES ('5','5','String_Value5','555.55','555.55','20050505 00:00:00','1','0123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340112340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012340123456789012','','','','','','','','','','');
COMMIT;

/* 
all_data_type_string_coll
*/
CREATE TABLE `all_data_type_string_coll` (
  `all_data_type_id` int(8) NOT NULL default '0',
  `string_value` VARCHAR(50) DEFAULT NULL,
  KEY `fk_all_data_type_all_data_type` (`all_data_type_id`),
  CONSTRAINT `fk_all_data_type_all_data_type` FOREIGN KEY (`all_data_type_id`) REFERENCES `all_data_type` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ;


/* 
assistant
*/
CREATE TABLE `assistant` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `professor_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_assistant_professor` (`professor_id`),
  CONSTRAINT `fk_assistant_professor` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `assistant` VALUES ('1','Assistant_Name1','1');
COMMIT;
INSERT INTO `assistant` VALUES ('2','Assistant_Name2','2');
COMMIT;
INSERT INTO `assistant` VALUES ('3','Assistant_Name3','2');
COMMIT;
INSERT INTO `assistant` VALUES ('4','Assistant_Name4','6');
COMMIT;
INSERT INTO `assistant` VALUES ('5','Assistant_Name5','7');
COMMIT;
INSERT INTO `assistant` VALUES ('6','Assistant_Name6','7');
COMMIT;
INSERT INTO `assistant` VALUES ('7','Assistant_Name7','11');
COMMIT;
INSERT INTO `assistant` VALUES ('8','Assistant_Name8','12');
COMMIT;
INSERT INTO `assistant` VALUES ('9','Assistant_Name9','12');
COMMIT;

/* 
assistant_professor
*/
CREATE TABLE `assistant_professor` (
  `professor_id` int(4) NOT NULL default '0',
  `joining_year` int(4) default NULL,
  PRIMARY KEY  (`professor_id`),
  CONSTRAINT `fk_assistant_profess_professor` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `assistant_professor` VALUES ('11','11');
COMMIT;
INSERT INTO `assistant_professor` VALUES ('12','12');
COMMIT;
INSERT INTO `assistant_professor` VALUES ('13','13');
COMMIT;
INSERT INTO `assistant_professor` VALUES ('14','14');
COMMIT;
INSERT INTO `assistant_professor` VALUES ('15','15');
COMMIT;

/* 
associate_professor
*/
CREATE TABLE `associate_professor` (
  `professor_id` int(8) NOT NULL default '0',
  `years_served` int(4) default NULL,
  PRIMARY KEY  (`professor_id`),
  CONSTRAINT `fk_associate_profess_professor` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `associate_professor` VALUES ('6','6');
COMMIT;
INSERT INTO `associate_professor` VALUES ('7','7');
COMMIT;
INSERT INTO `associate_professor` VALUES ('8','8');
COMMIT;
INSERT INTO `associate_professor` VALUES ('9','9');
COMMIT;
INSERT INTO `associate_professor` VALUES ('10','10');
COMMIT;

/* 
author
*/
CREATE TABLE `author` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `author` VALUES ('1','Author1');
COMMIT;
INSERT INTO `author` VALUES ('2','Author2');
COMMIT;
INSERT INTO `author` VALUES ('3','Author3');
COMMIT;
INSERT INTO `author` VALUES ('4','Author4');
COMMIT;
INSERT INTO `author` VALUES ('5','Author5');
COMMIT;

/* 
author_book
*/
CREATE TABLE `author_book` (
  `author_id` int(8) NOT NULL default '0',
  `book_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`author_id`,`book_id`),
  KEY `fk_author_book_book` (`book_id`),
  CONSTRAINT `fk_author_book_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_author_book_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `author_book` VALUES ('1','1');
COMMIT;
INSERT INTO `author_book` VALUES ('2','2');
COMMIT;
INSERT INTO `author_book` VALUES ('2','3');
COMMIT;

/* 
bag
*/
CREATE TABLE `bag` (
  `id` int(8) NOT NULL default '0',
  `style` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
bag_handle
*/
CREATE TABLE `bag_handle` (
  `bag_id` int(8) NOT NULL default '0',
  `handle_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`bag_id`,`handle_id`),
  UNIQUE KEY `uq_bag_handle_bag_id` (`bag_id`),
  UNIQUE KEY `uq_bag_handle_handle_id` (`handle_id`),
  CONSTRAINT `fk_bag_handle_bag` FOREIGN KEY (`bag_id`) REFERENCES `bag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bag_handle_handle` FOREIGN KEY (`handle_id`) REFERENCES `handle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
bank
*/
CREATE TABLE `bank` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `bank` VALUES ('1','Bank1');
COMMIT;
INSERT INTO `bank` VALUES ('2','Bank2');
COMMIT;
INSERT INTO `bank` VALUES ('3','Bank3');
COMMIT;
INSERT INTO `bank` VALUES ('4','Bank4');
COMMIT;

/* 
book
*/
CREATE TABLE `book` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `book` VALUES ('1','Book1');
COMMIT;
INSERT INTO `book` VALUES ('2','Book2');
COMMIT;
INSERT INTO `book` VALUES ('3','Book3');
COMMIT;
INSERT INTO `book` VALUES ('4','Book4');
COMMIT;
INSERT INTO `book` VALUES ('5','Book5');
COMMIT;

/* 
bride
*/
CREATE TABLE `bride` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
bride_father_in_law
*/
CREATE TABLE `bride_father_in_law` (
  `bride_id` int(8) NOT NULL default '0',
  `in_law_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`bride_id`,`in_law_id`),
  UNIQUE KEY `uq_bride_father_in_l_bride_id` (`bride_id`),
  UNIQUE KEY `uq_bride_father_in__in_law_id` (`in_law_id`),
  CONSTRAINT `fk_bride_father_in_law_bride` FOREIGN KEY (`bride_id`) REFERENCES `bride` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bride_father_in_law_in_law` FOREIGN KEY (`in_law_id`) REFERENCES `in_law` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
bride_mother_in_law
*/
CREATE TABLE `bride_mother_in_law` (
  `bride_d` int(8) NOT NULL default '0',
  `in_law_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`bride_d`,`in_law_id`),
  UNIQUE KEY `uq_bride_mother_in_la_bride_d` (`bride_d`),
  UNIQUE KEY `uq_bride_mother_in__in_law_id` (`in_law_id`),
  CONSTRAINT `fk_bride_mother_in_law_bride` FOREIGN KEY (`bride_d`) REFERENCES `bride` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bride_mother_in_law_in_law` FOREIGN KEY (`in_law_id`) REFERENCES `in_law` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
button
*/
CREATE TABLE `button` (
  `id` int(8) NOT NULL default '0',
  `holes` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
calculator
*/
CREATE TABLE `calculator` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `brand` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
card
*/
CREATE TABLE `card` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `suit_id` int(8) default NULL,
  `image` LONGTEXT,
  PRIMARY KEY  (`id`),
  KEY `fk_card_suit` (`suit_id`),
  CONSTRAINT `fk_card_suit` FOREIGN KEY (`suit_id`) REFERENCES `suit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `card` VALUES ('1','Ace','1','My Ace');
COMMIT;
INSERT INTO `card` VALUES ('2','Two','1','');
COMMIT;
INSERT INTO `card` VALUES ('3','Three','1','');
COMMIT;
INSERT INTO `card` VALUES ('4','Four','1','');
COMMIT;
INSERT INTO `card` VALUES ('5','Five','1','');
COMMIT;
INSERT INTO `card` VALUES ('6','Six','1','');
COMMIT;
INSERT INTO `card` VALUES ('7','Seven','1','');
COMMIT;
INSERT INTO `card` VALUES ('8','Eight','1','');
COMMIT;
INSERT INTO `card` VALUES ('9','Nine','1','');
COMMIT;
INSERT INTO `card` VALUES ('10','Ten','1','');
COMMIT;
INSERT INTO `card` VALUES ('11','Jack','1','');
COMMIT;
INSERT INTO `card` VALUES ('12','Queen','1','');
COMMIT;
INSERT INTO `card` VALUES ('13','King','1','');
COMMIT;
INSERT INTO `card` VALUES ('14','Ace','2','');
COMMIT;
INSERT INTO `card` VALUES ('15','Two','2','');
COMMIT;
INSERT INTO `card` VALUES ('16','Three','2','');
COMMIT;
INSERT INTO `card` VALUES ('17','Four','2','');
COMMIT;
INSERT INTO `card` VALUES ('18','Five','2','');
COMMIT;
INSERT INTO `card` VALUES ('19','Six','2','');
COMMIT;
INSERT INTO `card` VALUES ('20','Seven','2','');
COMMIT;
INSERT INTO `card` VALUES ('21','Eight','2','');
COMMIT;
INSERT INTO `card` VALUES ('22','Nine','2','');
COMMIT;
INSERT INTO `card` VALUES ('23','Ten','2','');
COMMIT;
INSERT INTO `card` VALUES ('24','Jack','2','');
COMMIT;
INSERT INTO `card` VALUES ('25','Queen','2','');
COMMIT;
INSERT INTO `card` VALUES ('26','King','2','');
COMMIT;
INSERT INTO `card` VALUES ('27','Ace','3','');
COMMIT;
INSERT INTO `card` VALUES ('28','Two','3','');
COMMIT;
INSERT INTO `card` VALUES ('29','Three','3','');
COMMIT;
INSERT INTO `card` VALUES ('30','Four','3','');
COMMIT;
INSERT INTO `card` VALUES ('31','Five','3','');
COMMIT;
INSERT INTO `card` VALUES ('32','Six','3','');
COMMIT;
INSERT INTO `card` VALUES ('33','Seven','3','');
COMMIT;
INSERT INTO `card` VALUES ('34','Eight','3','');
COMMIT;
INSERT INTO `card` VALUES ('35','Nine','3','');
COMMIT;
INSERT INTO `card` VALUES ('36','Ten','3','');
COMMIT;
INSERT INTO `card` VALUES ('37','Jack','3','');
COMMIT;
INSERT INTO `card` VALUES ('38','Queen','3','');
COMMIT;
INSERT INTO `card` VALUES ('39','King','3','');
COMMIT;
INSERT INTO `card` VALUES ('40','Ace','4','');
COMMIT;
INSERT INTO `card` VALUES ('41','Two','4','');
COMMIT;
INSERT INTO `card` VALUES ('42','Three','4','');
COMMIT;
INSERT INTO `card` VALUES ('43','Four','4','');
COMMIT;
INSERT INTO `card` VALUES ('44','Five','4','');
COMMIT;
INSERT INTO `card` VALUES ('45','Six','4','');
COMMIT;
INSERT INTO `card` VALUES ('46','Seven','4','');
COMMIT;
INSERT INTO `card` VALUES ('47','Eight','4','');
COMMIT;
INSERT INTO `card` VALUES ('48','Nine','4','');
COMMIT;
INSERT INTO `card` VALUES ('49','Ten','4','');
COMMIT;
INSERT INTO `card` VALUES ('50','Jack','4','');
COMMIT;
INSERT INTO `card` VALUES ('51','Queen','4','');
COMMIT;
INSERT INTO `card` VALUES ('52','King','4','');
COMMIT;
INSERT INTO `card` VALUES ('53','Joker','','');
COMMIT;

/* 
cash
*/
CREATE TABLE `cash` (
  `payment_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`payment_id`),
  CONSTRAINT `fk_cash_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `cash` VALUES ('1');
COMMIT;
INSERT INTO `cash` VALUES ('2');
COMMIT;

/* 
chain
*/
CREATE TABLE `chain` (
  `id` int(8) NOT NULL default '0',
  `metal` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
chain_pendant
*/
CREATE TABLE `chain_pendant` (
  `chain_id` int(8) NOT NULL default '0',
  `pendant_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`chain_id`,`pendant_id`),
  UNIQUE KEY `uq_chain_pendant_chain_id` (`chain_id`),
  UNIQUE KEY `uq_chain_pendant_pendant_id` (`pendant_id`),
  CONSTRAINT `fk_chain_pendant_chain` FOREIGN KEY (`chain_id`) REFERENCES `chain` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_chain_pendant_pendant` FOREIGN KEY (`pendant_id`) REFERENCES `pendant` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
character_key
*/
CREATE TABLE `character_key` (
  `id` CHAR(1) NOT NULL DEFAULT '',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
character_primitive_key
*/
CREATE TABLE `character_primitive_key` (
  `id` CHAR(1) NOT NULL DEFAULT '',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
chef
*/
CREATE TABLE `chef` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `restaurant_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_chef_restaurant` (`restaurant_id`),
  CONSTRAINT `fk_chef_restaurant` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `chef` VALUES ('1','Chef1','1');
COMMIT;
INSERT INTO `chef` VALUES ('2','Chef2','2');
COMMIT;
INSERT INTO `chef` VALUES ('3','Chef3','2');
COMMIT;
INSERT INTO `chef` VALUES ('4','Chef4','');
COMMIT;

/* 
child
*/
CREATE TABLE `child` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `father_id` int(8) default NULL,
  `mother_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `fk_child_parent` FOREIGN KEY (`father_id`) REFERENCES `parent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_child_parent_m` FOREIGN KEY (`mother_id`) REFERENCES `parent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `child` VALUES ('1','Child_Name1','1','2');
COMMIT;
INSERT INTO `child` VALUES ('2','Child_Name2','3','4');
COMMIT;
INSERT INTO `child` VALUES ('3','Child_Name3','5','');
COMMIT;
INSERT INTO `child` VALUES ('4','Child_Name4','','6');
COMMIT;
INSERT INTO `child` VALUES ('5','Child_Name5','','');
COMMIT;

/* 
computer
*/
CREATE TABLE `computer` (
  `id` int(8) NOT NULL default '0',
  `TYPE` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `computer` VALUES ('1','Computer_Type1');
COMMIT;
INSERT INTO `computer` VALUES ('2','Computer_Type2');
COMMIT;
INSERT INTO `computer` VALUES ('3','Computer_Type3');
COMMIT;
INSERT INTO `computer` VALUES ('4','Computer_Type4');
COMMIT;
INSERT INTO `computer` VALUES ('5','Computer_Type5');
COMMIT;

/* 
credit
*/
CREATE TABLE `credit` (
  `payment_id` int(8) NOT NULL default '0',
  `card_number` VARCHAR(50) DEFAULT NULL,
  `bank_id` int(8) default NULL,
  PRIMARY KEY  (`payment_id`),
  KEY `fk_credit_bank` (`bank_id`),
  CONSTRAINT `fk_credit_bank` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_credit_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `credit` VALUES ('3','3','3');
COMMIT;
INSERT INTO `credit` VALUES ('4','4','4');
COMMIT;

/* 
crt_monitor
*/
CREATE TABLE `crt_monitor` (
  `monitor_id` int(8) NOT NULL default '0',
  `refresh_rate` int(8) default NULL,
  PRIMARY KEY  (`monitor_id`),
  CONSTRAINT `fk_crt_monitor_monitor` FOREIGN KEY (`monitor_id`) REFERENCES `monitor` (`display_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `crt_monitor` VALUES ('1','45');
COMMIT;

/* 
csm_application
*/
CREATE TABLE `csm_application` (
  `APPLICATION_ID` bigint(20) NOT NULL auto_increment,
  `APPLICATION_NAME` varchar(255) NOT NULL default '',
  `APPLICATION_DESCRIPTION` varchar(200) NOT NULL default '',
  `DECLARATIVE_FLAG` tinyint(1) NOT NULL default '0',
  `ACTIVE_FLAG` tinyint(1) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `DATABASE_URL` varchar(100) default NULL,
  `DATABASE_USER_NAME` varchar(100) default NULL,
  `DATABASE_PASSWORD` varchar(100) default NULL,
  `DATABASE_DIALECT` varchar(100) default NULL,
  `DATABASE_DRIVER` varchar(100) default NULL,
  PRIMARY KEY  (`APPLICATION_ID`),
  UNIQUE KEY `UQ_APPLICATION_NAME` (`APPLICATION_NAME`)
) ;

INSERT INTO `csm_application` VALUES ('1','csmupt','UPT Super Admin Application','0','0','20070228','','','','','');
COMMIT;
INSERT INTO `csm_application` VALUES ('2','sdk','sdk','1','1','20070301','jdbc:mysql://localhost/test','username','encryptedPassword','org.hibernate.dialect.MySQLDialect','org.gjt.mm.mysql.Driver');
COMMIT;

/* 
csm_filter_clause
*/
CREATE TABLE `csm_filter_clause` (
  `FILTER_CLAUSE_ID` bigint(20) NOT NULL auto_increment,
  `CLASS_NAME` varchar(100) NOT NULL default '',
  `FILTER_CHAIN` text NOT NULL,
  `TARGET_CLASS_NAME` varchar(100) NOT NULL default '',
  `TARGET_CLASS_ATTRIBUTE_NAME` varchar(100) NOT NULL default '',
  `TARGET_CLASS_ATTRIBUTE_TYPE` varchar(100) NOT NULL default '',
  `TARGET_CLASS_ALIAS` varchar(100) default NULL,
  `TARGET_CLASS_ATTRIBUTE_ALIAS` varchar(100) default NULL,
  `GENERATED_SQL` text NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`FILTER_CLAUSE_ID`)
) ;


/* 
csm_group
*/
CREATE TABLE `csm_group` (
  `GROUP_ID` bigint(20) NOT NULL auto_increment,
  `GROUP_NAME` varchar(255) NOT NULL default '',
  `GROUP_DESC` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `APPLICATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`GROUP_ID`),
  UNIQUE KEY `UQ_GROUP_GROUP_NAME` (`APPLICATION_ID`,`GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_GROUP` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ;


/* 
csm_pg_pe
*/
CREATE TABLE `csm_pg_pe` (
  `PG_PE_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL default '0',
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PG_PE_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_ELEMENT_PROTECTION_GROUP_ID` (`PROTECTION_ELEMENT_ID`,`PROTECTION_GROUP_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PROTECTION_GROUP_PROTECTION_ELEMENT` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_ELEMENT_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_pg_pe` VALUES ('57','5','56','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('58','5','53','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('59','5','54','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('60','5','55','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('61','5','51','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('62','5','52','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('63','5','50','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('64','1','29','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('65','1','20','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('66','1','26','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('67','1','40','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('68','1','34','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('69','1','15','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('70','1','36','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('71','1','6','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('72','1','44','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('73','1','24','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('74','1','7','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('75','1','23','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('76','1','45','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('77','1','4','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('78','1','17','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('79','1','14','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('80','1','18','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('81','1','30','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('82','1','11','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('83','1','22','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('84','1','21','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('85','1','32','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('86','1','16','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('87','1','35','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('88','1','42','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('89','1','9','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('90','1','5','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('91','1','27','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('92','1','41','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('93','1','13','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('94','1','10','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('95','1','39','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('96','1','43','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('97','1','38','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('98','1','19','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('99','1','25','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('100','1','33','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('101','1','37','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('102','1','28','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('103','1','8','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('104','1','31','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('105','1','12','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('106','1','3','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('107','2','11','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('108','2','24','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('109','2','3','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('110','2','8','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('111','3','37','20070717');
COMMIT;
INSERT INTO `csm_pg_pe` VALUES ('112','4','24','20070717');
COMMIT;

/* 
csm_privilege
*/
CREATE TABLE `csm_privilege` (
  `PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `PRIVILEGE_NAME` varchar(100) NOT NULL default '',
  `PRIVILEGE_DESCRIPTION` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PRIVILEGE_ID`),
  UNIQUE KEY `UQ_PRIVILEGE_NAME` (`PRIVILEGE_NAME`)
) ;

INSERT INTO `csm_privilege` VALUES ('1','CREATE','This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('2','ACCESS','This privilege allows a user to access a particular resource.','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('3','READ','This privilege permits the user to read data from a file, URL, socket, database, or an object.','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('4','WRITE','This privilege allows a user to write data to a file, URL, socket, database, or object.','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('5','UPDATE','This privilege grants permission at an entity level and signifies that the user is allowed to update and modify data for a particular entity.','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('6','DELETE','This privilege permits a user to delete a logical entity.','20070228');
COMMIT;
INSERT INTO `csm_privilege` VALUES ('7','EXECUTE','This privilege allows a user to execute a particular resource.','20070228');
COMMIT;

/* 
csm_protection_element
*/
CREATE TABLE `csm_protection_element` (
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_NAME` varchar(100) NOT NULL default '',
  `PROTECTION_ELEMENT_DESCRIPTION` varchar(200) default NULL,
  `OBJECT_ID` varchar(100) NOT NULL default '',
  `ATTRIBUTE` varchar(100) default NULL,
  `PROTECTION_ELEMENT_TYPE` varchar(100) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `ATTRIBUTE_VALUE` varchar(100) default NULL,
  PRIMARY KEY  (`PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_PE_PE_NAME_ATTRIBUTE_VALUE_APP_ID` (`OBJECT_ID`,`ATTRIBUTE`,`ATTRIBUTE_VALUE`,`APPLICATION_ID`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_PE_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_protection_element` VALUES ('1','csmupt','UPT Super Admin Application','csmupt','','','1','20070228','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('2','sdk','sdk Application','sdk','','','1','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('3','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('4','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('5','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('6','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment','gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('7','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.GraduateStudent','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.GraduateStudent','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.GraduateStudent','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('8','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('9','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.UndergraduateStudent','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.UndergraduateStudent','gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.UndergraduateStudent','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('10','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Human','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Human','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Human','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('11','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Mammal','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Mammal','gov.nih.nci.cacoresdk.domain.inheritance.onechild.Mammal','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('12','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Assistant','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Assistant','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Assistant','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('13','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssistantProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssistantProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssistantProfessor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('14','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssociateProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssociateProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.AssociateProfessor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('15','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Professor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Professor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.Professor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('16','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.TenuredProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.TenuredProfessor','gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.TenuredProfessor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('17','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.CRTMonitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.CRTMonitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.CRTMonitor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('18','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Display','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Display','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Display','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('19','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.LCDMonitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.LCDMonitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.LCDMonitor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('20','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Monitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Monitor','gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.Monitor','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('21','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Employee','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Employee','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Employee','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('22','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Project','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Project','gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Project','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('23','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Author','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Author','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Author','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('24','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Book','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Book','gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.Book','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('25','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Chef','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Chef','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Chef','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('26','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Restaurant','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Restaurant','gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.Restaurant','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('27','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.Computer','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.Computer','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.Computer','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('28','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.HardDrive','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.HardDrive','gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.HardDrive','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('29','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.Key','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.Key','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.Key','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('30','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.KeyChain','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.KeyChain','gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.KeyChain','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('31','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('32','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product','gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('33','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Child','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Child','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Child','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('34','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Parent','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Parent','gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.Parent','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('35','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Address','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Address','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Address','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('36','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Person','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Person','gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.Person','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('37','gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType','gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType','gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('38','gov.nih.nci.cacoresdk.domain.other.levelassociation.Card','gov.nih.nci.cacoresdk.domain.other.levelassociation.Card','gov.nih.nci.cacoresdk.domain.other.levelassociation.Card','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('39','gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck','gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck','gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('40','gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand','gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand','gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('41','gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit','gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit','gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('42','gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey','gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey','gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('43','gov.nih.nci.cacoresdk.domain.other.primarykey.FloatKey','gov.nih.nci.cacoresdk.domain.other.primarykey.FloatKey','gov.nih.nci.cacoresdk.domain.other.primarykey.FloatKey','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('44','gov.nih.nci.cacoresdk.domain.other.primarykey.IntegerKey','gov.nih.nci.cacoresdk.domain.other.primarykey.IntegerKey','gov.nih.nci.cacoresdk.domain.other.primarykey.IntegerKey','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('45','gov.nih.nci.cacoresdk.domain.other.primarykey.StringKey','gov.nih.nci.cacoresdk.domain.other.primarykey.StringKey','gov.nih.nci.cacoresdk.domain.other.primarykey.StringKey','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('47','This is a test','This is a test','This is a test','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('49','This is a test again','This is a test again','This is a test again','','','2','20070301','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('50','gov.nih.nci.cabio.domain.Clone','','gov.nih.nci.cabio.domain.Clone','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('51','gov.nih.nci.cabio.domain.Chromosome','','gov.nih.nci.cabio.domain.Chromosome','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('52','gov.nih.nci.cabio.domain.Taxon','','gov.nih.nci.cabio.domain.Taxon','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('53','gov.nih.nci.cabio.domain.Target','','gov.nih.nci.cabio.domain.Target','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('54','gov.nih.nci.cabio.domain.Gene','','gov.nih.nci.cabio.domain.Gene','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('55','gov.nih.nci.cabio.domain.Sequence','','gov.nih.nci.cabio.domain.Sequence','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('56','gov.nih.nci.cabio.domain.Library','','gov.nih.nci.cabio.domain.Library','','','2','20070413','');
COMMIT;
INSERT INTO `csm_protection_element` VALUES ('58','gov.nih.nci.cabio.domain.Taxon.commonName','','gov.nih.nci.cabio.domain.Taxon','commonName','','2','20070824','');
COMMIT;

/* 
csm_protection_group
*/
CREATE TABLE `csm_protection_group` (
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_NAME` varchar(100) NOT NULL default '',
  `PROTECTION_GROUP_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL default '0',
  `LARGE_ELEMENT_COUNT_FLAG` tinyint(1) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `PARENT_PROTECTION_GROUP_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PROTECTION_GROUP_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_GROUP_NAME` (`APPLICATION_ID`,`PROTECTION_GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  KEY `idx_PARENT_PROTECTION_GROUP_ID` (`PARENT_PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PROTECTION_GROUP` FOREIGN KEY (`PARENT_PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PG_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_protection_group` VALUES ('1','All PEs','contains all of the PEs in the entire test sdk system','2','0','20070322','');
COMMIT;
INSERT INTO `csm_protection_group` VALUES ('2','Bank','','2','0','20070301','');
COMMIT;
INSERT INTO `csm_protection_group` VALUES ('3','AllDataType','','2','0','20070323','');
COMMIT;
INSERT INTO `csm_protection_group` VALUES ('4','Book','','2','0','20070323','');
COMMIT;
INSERT INTO `csm_protection_group` VALUES ('5','gov.nih.nci.cabio.domain','','2','0','20070413','');
COMMIT;

/* 
csm_role
*/
CREATE TABLE `csm_role` (
  `ROLE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_NAME` varchar(100) NOT NULL default '',
  `ROLE_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL default '0',
  `ACTIVE_FLAG` tinyint(1) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`ROLE_ID`),
  UNIQUE KEY `UQ_ROLE_ROLE_NAME` (`APPLICATION_ID`,`ROLE_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_ROLE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_role` VALUES ('1','SuperAdmin','SuperAdmin','2','1','20070301');
COMMIT;
INSERT INTO `csm_role` VALUES ('2','Read','','2','1','20070301');
COMMIT;
INSERT INTO `csm_role` VALUES ('3','Create','','2','1','20070323');
COMMIT;

/* 
csm_role_privilege
*/
CREATE TABLE `csm_role_privilege` (
  `ROLE_PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_ID` bigint(20) NOT NULL default '0',
  `PRIVILEGE_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`ROLE_PRIVILEGE_ID`),
  UNIQUE KEY `UQ_ROLE_PRIVILEGE_ROLE_ID` (`PRIVILEGE_ID`,`ROLE_ID`),
  KEY `idx_PRIVILEGE_ID` (`PRIVILEGE_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PRIVILEGE_ROLE` FOREIGN KEY (`PRIVILEGE_ID`) REFERENCES `csm_privilege` (`PRIVILEGE_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_role_privilege` VALUES ('1','2','3','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('2','1','5','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('3','1','1','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('4','1','2','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('5','1','7','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('6','1','4','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('7','1','3','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('8','1','6','20070717');
COMMIT;
INSERT INTO `csm_role_privilege` VALUES ('9','3','1','20070717');
COMMIT;

/* 
csm_user
*/
CREATE TABLE `csm_user` (
  `USER_ID` bigint(20) NOT NULL auto_increment,
  `LOGIN_NAME` varchar(100) NOT NULL default '',
  `FIRST_NAME` varchar(100) NOT NULL default '',
  `LAST_NAME` varchar(100) NOT NULL default '',
  `ORGANIZATION` varchar(100) default NULL,
  `DEPARTMENT` varchar(100) default NULL,
  `TITLE` varchar(100) default NULL,
  `PHONE_NUMBER` varchar(15) default NULL,
  `PASSWORD` varchar(100) default NULL,
  `EMAIL_ID` varchar(100) default NULL,
  `START_DATE` date default NULL,
  `END_DATE` date default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`USER_ID`),
  UNIQUE KEY `UQ_LOGIN_NAME` (`LOGIN_NAME`)
) ;

INSERT INTO `csm_user` VALUES ('1','SuperAdmin','Super','Admin','','','','','XdRz2L8CN+ygmHldOE8ejA==','',NULL,NULL,'20070323');
COMMIT;
INSERT INTO `csm_user` VALUES ('2','User1','User1','User1','','','','','KAr8eYrMgvs=','',NULL,NULL,'20070326');
COMMIT;
INSERT INTO `csm_user` VALUES ('3','asdf','asdf','asdf','','','','','4JsrPs5x4Hc=','',NULL,NULL,'20070302');
COMMIT;
INSERT INTO `csm_user` VALUES ('4','qwer','qwer','qwer','','','','','vY9TSSEXu94=','',NULL,NULL,'20070302');
COMMIT;
INSERT INTO `csm_user` VALUES ('5','modik','Kunal','Modi','','','','','','',NULL,NULL,'20070322');
COMMIT;
INSERT INTO `csm_user` VALUES ('6','User2','User2','User2','','','','','pW6D5p9+yRA=','',NULL,NULL,'20070326');
COMMIT;
INSERT INTO `csm_user` VALUES ('7','User3','User3','User3','','','','','9nljHyRzdjY=','',NULL,NULL,'20070326');
COMMIT;
INSERT INTO `csm_user` VALUES ('8','hunterst','asdf','asdf','','','','','','',NULL,NULL,'20070323');
COMMIT;
INSERT INTO `csm_user` VALUES ('9','patelsat','Satish','Patel','','','','','','',NULL,NULL,'20070323');
COMMIT;
INSERT INTO `csm_user` VALUES ('11','yusuf','Yusuf','Abdulla','','','','','UIpz2URaRaM=','',NULL,NULL,'20070823');
COMMIT;

/* 
csm_user_group
*/
CREATE TABLE `csm_user_group` (
  `USER_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) NOT NULL default '0',
  `GROUP_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`USER_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `FK_UG_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ;


/* 
csm_user_group_role_pg
*/
CREATE TABLE `csm_user_group_role_pg` (
  `USER_GROUP_ROLE_PG_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) default NULL,
  `GROUP_ID` bigint(20) default NULL,
  `ROLE_ID` bigint(20) NOT NULL default '0',
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`USER_GROUP_ROLE_PG_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_GROUPS` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_user_group_role_pg` VALUES ('1','1','','1','1','20070301');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('4','5','','2','1','20070322');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('5','2','','2','3','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('6','2','','2','4','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('7','7','','3','4','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('8','6','','2','4','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('9','8','','1','1','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('10','9','','1','1','20070323');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('11','5','','1','5','20070413');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('12','2','','1','5','20070413');
COMMIT;
INSERT INTO `csm_user_group_role_pg` VALUES ('14','2','','2','1','20070808');
COMMIT;

/* 
csm_user_pe
*/
CREATE TABLE `csm_user_pe` (
  `USER_PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL default '0',
  `USER_ID` bigint(20) NOT NULL default '0',
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`USER_PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_USER_PROTECTION_ELEMENT_PROTECTION_ELEMENT_ID` (`USER_ID`,`PROTECTION_ELEMENT_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  CONSTRAINT `FK_PROTECTION_ELEMENT_USER` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ;

INSERT INTO `csm_user_pe` VALUES ('1','2','2','20070717');
COMMIT;
INSERT INTO `csm_user_pe` VALUES ('2','2','1','20070717');
COMMIT;
INSERT INTO `csm_user_pe` VALUES ('3','1','1','20070717');
COMMIT;

/* 
currency
*/
CREATE TABLE `currency` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `country` VARCHAR(50) DEFAULT NULL,
  `value` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
deck
*/
CREATE TABLE `deck` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `deck` VALUES ('1','My Deck 1');
COMMIT;

/* 
designer
*/
CREATE TABLE `designer` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
display
*/
CREATE TABLE `display` (
  `id` int(8) NOT NULL default '0',
  `width` int(8) default NULL,
  `height` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `display` VALUES ('1','1','1');
COMMIT;
INSERT INTO `display` VALUES ('2','2','2');
COMMIT;
INSERT INTO `display` VALUES ('3','3','3');
COMMIT;
INSERT INTO `display` VALUES ('4','4','4');
COMMIT;
INSERT INTO `display` VALUES ('5','5','5');
COMMIT;

/* 
double_key
*/
CREATE TABLE `double_key` (
  `id` decimal(8,2) NOT NULL default '0.00',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `double_key` VALUES ('1.10','Double_Key_Name1.1');
COMMIT;
INSERT INTO `double_key` VALUES ('2.20','Double_Key_Name2.2');
COMMIT;
INSERT INTO `double_key` VALUES ('3.30','Double_Key_Name3.3');
COMMIT;
INSERT INTO `double_key` VALUES ('4.40','Double_Key_Name4.4');
COMMIT;
INSERT INTO `double_key` VALUES ('5.50','Double_Key_Name5.5');
COMMIT;

/* 
double_primitive_key
*/
CREATE TABLE `double_primitive_key` (
  `id` decimal(8,2) NOT NULL default '0.00',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
element
*/
CREATE TABLE `element` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `parent_element_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_element_parent_element_id` (`parent_element_id`),
  CONSTRAINT `fk_element_element` FOREIGN KEY (`parent_element_id`) REFERENCES `element` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `element` VALUES ('1','Name1','');
COMMIT;
INSERT INTO `element` VALUES ('2','Name2','1');
COMMIT;

/* 
employee
*/
CREATE TABLE `employee` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `employee` VALUES ('1','Employee_Name1');
COMMIT;
INSERT INTO `employee` VALUES ('2','Employee_Name2');
COMMIT;
INSERT INTO `employee` VALUES ('3','Employee_Name3');
COMMIT;
INSERT INTO `employee` VALUES ('4','Employee_Name4');
COMMIT;
INSERT INTO `employee` VALUES ('5','Employee_Name5');
COMMIT;
INSERT INTO `employee` VALUES ('6','Employee_Name6');
COMMIT;
INSERT INTO `employee` VALUES ('7','Employee_Name7');
COMMIT;
INSERT INTO `employee` VALUES ('8','Employee_Name8');
COMMIT;
INSERT INTO `employee` VALUES ('9','Employee_Name9');
COMMIT;
INSERT INTO `employee` VALUES ('10','Employee_Name10');
COMMIT;

/* 
employee_project
*/
CREATE TABLE `employee_project` (
  `employee_id` int(8) NOT NULL default '0',
  `project_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`employee_id`,`project_id`),
  KEY `fk_employee_project_project` (`project_id`),
  CONSTRAINT `fk_employee_project_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employee_project_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `employee_project` VALUES ('1','1');
COMMIT;
INSERT INTO `employee_project` VALUES ('2','2');
COMMIT;
INSERT INTO `employee_project` VALUES ('3','2');
COMMIT;
INSERT INTO `employee_project` VALUES ('4','4');
COMMIT;
INSERT INTO `employee_project` VALUES ('4','5');
COMMIT;
INSERT INTO `employee_project` VALUES ('6','5');
COMMIT;

/* 
flight
*/
CREATE TABLE `flight` (
  `id` int(8) NOT NULL default '0',
  `destination` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
flight_passanger
*/
CREATE TABLE `flight_passanger` (
  `flight_id` int(8) NOT NULL default '0',
  `passanger_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`flight_id`,`passanger_id`),
  KEY `fk_flight_passanger_passanger` (`passanger_id`),
  CONSTRAINT `fk_flight_passanger_flight` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_flight_passanger_passanger` FOREIGN KEY (`passanger_id`) REFERENCES `passanger` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
float_key
*/
CREATE TABLE `float_key` (
  `id` decimal(8,2) NOT NULL default '0.00',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `float_key` VALUES ('1.10','Float_Key_Name1.1');
COMMIT;
INSERT INTO `float_key` VALUES ('2.20','Float_Key_Name2.2');
COMMIT;
INSERT INTO `float_key` VALUES ('3.30','Float_Key_Name3.3');
COMMIT;
INSERT INTO `float_key` VALUES ('4.40','Float_Key_Name4.4');
COMMIT;
INSERT INTO `float_key` VALUES ('5.50','Float_Key_Name5.5');
COMMIT;

/* 
float_primitive_key
*/
CREATE TABLE `float_primitive_key` (
  `id` decimal(8,2) NOT NULL default '0.00',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
goverment
*/
CREATE TABLE `goverment` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `country` VARCHAR(50) DEFAULT NULL,
  `prime_minister` VARCHAR(50) DEFAULT NULL,
  `president` VARCHAR(50) DEFAULT NULL,
  `democratic_discriminator` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
graduate_student
*/
CREATE TABLE `graduate_student` (
  `student_id` int(8) NOT NULL default '0',
  `project_name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`student_id`),
  CONSTRAINT `fk_graduate_student_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `graduate_student` VALUES ('6','Project_Name6');
COMMIT;
INSERT INTO `graduate_student` VALUES ('7','Project_Name7');
COMMIT;
INSERT INTO `graduate_student` VALUES ('8','Project_Name8');
COMMIT;
INSERT INTO `graduate_student` VALUES ('9','Project_Name9');
COMMIT;
INSERT INTO `graduate_student` VALUES ('10','Project_Name10');
COMMIT;

/* 
graphic_calculator
*/
CREATE TABLE `graphic_calculator` (
  `calculator_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`calculator_id`),
  CONSTRAINT `fk_graphic_calculat_calculator` FOREIGN KEY (`calculator_id`) REFERENCES `calculator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
hand
*/
CREATE TABLE `hand` (
  `id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `hand` VALUES ('1');
COMMIT;
INSERT INTO `hand` VALUES ('2');
COMMIT;
INSERT INTO `hand` VALUES ('3');
COMMIT;
INSERT INTO `hand` VALUES ('4');
COMMIT;
INSERT INTO `hand` VALUES ('5');
COMMIT;

/* 
hand_card
*/
CREATE TABLE `hand_card` (
  `hand_id` int(8) NOT NULL default '0',
  `card_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`hand_id`,`card_id`),
  KEY `fk_hand_card_card` (`card_id`),
  CONSTRAINT `fk_hand_card_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_hand_card_hand` FOREIGN KEY (`hand_id`) REFERENCES `hand` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `hand_card` VALUES ('1','1');
COMMIT;
INSERT INTO `hand_card` VALUES ('2','2');
COMMIT;
INSERT INTO `hand_card` VALUES ('2','3');
COMMIT;
INSERT INTO `hand_card` VALUES ('2','5');
COMMIT;
INSERT INTO `hand_card` VALUES ('3','6');
COMMIT;
INSERT INTO `hand_card` VALUES ('3','14');
COMMIT;
INSERT INTO `hand_card` VALUES ('3','15');
COMMIT;
INSERT INTO `hand_card` VALUES ('1','25');
COMMIT;
INSERT INTO `hand_card` VALUES ('4','26');
COMMIT;
INSERT INTO `hand_card` VALUES ('4','27');
COMMIT;
INSERT INTO `hand_card` VALUES ('4','30');
COMMIT;
INSERT INTO `hand_card` VALUES ('5','39');
COMMIT;
INSERT INTO `hand_card` VALUES ('5','40');
COMMIT;
INSERT INTO `hand_card` VALUES ('5','41');
COMMIT;
INSERT INTO `hand_card` VALUES ('1','52');
COMMIT;

/* 
handle
*/
CREATE TABLE `handle` (
  `id` int(8) NOT NULL default '0',
  `color` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
hard_drive
*/
CREATE TABLE `hard_drive` (
  `id` int(8) NOT NULL default '0',
  `drive_size` int(8) default NULL,
  `computer_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_hard_drive_computer` (`computer_id`),
  CONSTRAINT `fk_hard_drive_computer` FOREIGN KEY (`computer_id`) REFERENCES `computer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `hard_drive` VALUES ('1','1','1');
COMMIT;
INSERT INTO `hard_drive` VALUES ('2','2','2');
COMMIT;
INSERT INTO `hard_drive` VALUES ('3','3','2');
COMMIT;

/* 
human
*/
CREATE TABLE `human` (
  `mammal_id` int(8) NOT NULL default '0',
  `diet` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`mammal_id`),
  CONSTRAINT `fk_human_mammal` FOREIGN KEY (`mammal_id`) REFERENCES `mammal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `human` VALUES ('1','DIET1');
COMMIT;
INSERT INTO `human` VALUES ('2','DIET2');
COMMIT;
INSERT INTO `human` VALUES ('3','DIET3');
COMMIT;
INSERT INTO `human` VALUES ('4','DIET4');
COMMIT;

/* 
in_law
*/
CREATE TABLE `in_law` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
integer_key
*/
CREATE TABLE `integer_key` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `integer_key` VALUES ('1','Integer_Key_Name1');
COMMIT;
INSERT INTO `integer_key` VALUES ('2','Integer_Key_Name2');
COMMIT;
INSERT INTO `integer_key` VALUES ('3','Integer_Key_Name3');
COMMIT;
INSERT INTO `integer_key` VALUES ('4','Integer_Key_Name4');
COMMIT;
INSERT INTO `integer_key` VALUES ('5','Integer_Key_Name5');
COMMIT;

/* 
integer_primitive_key
*/
CREATE TABLE `integer_primitive_key` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
key
*/
CREATE TABLE `key` (
  `id` int(8) NOT NULL default '0',
  `TYPE` VARCHAR(50) DEFAULT NULL,
  `keychain_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_key_keychain` (`keychain_id`),
  CONSTRAINT `fk_key_keychain` FOREIGN KEY (`keychain_id`) REFERENCES `keychain` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `key` VALUES ('1','Key_Type1','1');
COMMIT;
INSERT INTO `key` VALUES ('2','Key_Type2','2');
COMMIT;
INSERT INTO `key` VALUES ('3','Key_Type3','2');
COMMIT;

/* 
keychain
*/
CREATE TABLE `keychain` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `keychain` VALUES ('1','Keychain_Name1');
COMMIT;
INSERT INTO `keychain` VALUES ('2','Keychain_Name2');
COMMIT;
INSERT INTO `keychain` VALUES ('3','Keychain_Name3');
COMMIT;
INSERT INTO `keychain` VALUES ('4','Keychain_Name4');
COMMIT;
INSERT INTO `keychain` VALUES ('5','Keychain_Name5');
COMMIT;

/* 
lcd_monitor
*/
CREATE TABLE `lcd_monitor` (
  `monitor_id` int(8) NOT NULL default '0',
  `dpi_supported` int(8) default NULL,
  PRIMARY KEY  (`monitor_id`),
  CONSTRAINT `fk_lcd_monitor_monitor` FOREIGN KEY (`monitor_id`) REFERENCES `monitor` (`display_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `lcd_monitor` VALUES ('2','2323');
COMMIT;
INSERT INTO `lcd_monitor` VALUES ('3','1212');
COMMIT;


/* 
long_key
*/
CREATE TABLE `long_key` (
  `id` decimal(38,0) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
long_primitive_key
*/
CREATE TABLE `long_primitive_key` (
  `id` decimal(38,0) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
luggage
*/
CREATE TABLE `luggage` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `capacity` int(8) default NULL,
  `key_code` int(8) default NULL,
  `expandable` CHAR(1) DEFAULT NULL,
  `wheel_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_luggage_wheel` (`wheel_id`),
  CONSTRAINT `fk_luggage_wheel` FOREIGN KEY (`wheel_id`) REFERENCES `wheel` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
mammal
*/
CREATE TABLE `mammal` (
  `id` int(8) NOT NULL default '0',
  `hair_color` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `mammal` VALUES ('1','Hair_Color1');
COMMIT;
INSERT INTO `mammal` VALUES ('2','Hair_Color2');
COMMIT;
INSERT INTO `mammal` VALUES ('3','Hair_Color3');
COMMIT;
INSERT INTO `mammal` VALUES ('4','Hair_Color4');
COMMIT;
INSERT INTO `mammal` VALUES ('5','Hair_Color5');
COMMIT;

/* 
monitor
*/
CREATE TABLE `monitor` (
  `display_id` int(8) NOT NULL default '0',
  `brand` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`display_id`),
  CONSTRAINT `fk_monitor_display` FOREIGN KEY (`display_id`) REFERENCES `display` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `monitor` VALUES ('1','A');
COMMIT;
INSERT INTO `monitor` VALUES ('2','B');
COMMIT;
INSERT INTO `monitor` VALUES ('3','C');
COMMIT;
INSERT INTO `monitor` VALUES ('4','D');
COMMIT;

/* 
no_id_key
*/
CREATE TABLE `no_id_key` (
  `my_key` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`my_key`)
) ;


/* 
orderline
*/
CREATE TABLE `orderline` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `orderline` VALUES ('1','Orderline_Name1');
COMMIT;
INSERT INTO `orderline` VALUES ('2','Orderline_Name2');
COMMIT;
INSERT INTO `orderline` VALUES ('3','Orderline_Name3');
COMMIT;
INSERT INTO `orderline` VALUES ('4','Orderline_Name4');
COMMIT;
INSERT INTO `orderline` VALUES ('5','Orderline_Name5');
COMMIT;

/* 
organization
*/
CREATE TABLE `organization` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(50) DEFAULT NULL,
  `agency_budget` int(8) default NULL,
  `ceo` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
parent
*/
CREATE TABLE `parent` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `parent` VALUES ('1','Parent_Name1');
COMMIT;
INSERT INTO `parent` VALUES ('2','Parent_Name2');
COMMIT;
INSERT INTO `parent` VALUES ('3','Parent_Name3');
COMMIT;
INSERT INTO `parent` VALUES ('4','Parent_Name4');
COMMIT;
INSERT INTO `parent` VALUES ('5','Parent_Name5');
COMMIT;
INSERT INTO `parent` VALUES ('6','Parent_Name6');
COMMIT;
INSERT INTO `parent` VALUES ('7','Parent_Name7');
COMMIT;
INSERT INTO `parent` VALUES ('8','Parent_Name8');
COMMIT;
INSERT INTO `parent` VALUES ('9','Parent_Name9');
COMMIT;
INSERT INTO `parent` VALUES ('10','Parent_Name10');
COMMIT;

/* 
passanger
*/
CREATE TABLE `passanger` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
payment
*/
CREATE TABLE `payment` (
  `id` int(8) NOT NULL default '0',
  `amount` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `payment` VALUES ('1','1');
COMMIT;
INSERT INTO `payment` VALUES ('2','2');
COMMIT;
INSERT INTO `payment` VALUES ('3','3');
COMMIT;
INSERT INTO `payment` VALUES ('4','4');
COMMIT;
INSERT INTO `payment` VALUES ('5','5');
COMMIT;

/* 
pendant
*/
CREATE TABLE `pendant` (
  `id` int(8) NOT NULL default '0',
  `shape` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
person
*/
CREATE TABLE `person` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `address_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `fk_person_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `person` VALUES ('1','Person_Name1','1');
COMMIT;
INSERT INTO `person` VALUES ('2','Person_Name2','2');
COMMIT;
INSERT INTO `person` VALUES ('3','Person_Name3','3');
COMMIT;
INSERT INTO `person` VALUES ('4','Person_Name4','');
COMMIT;
INSERT INTO `person` VALUES ('5','Person_Name5','');
COMMIT;

/* 
product
*/
CREATE TABLE `product` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `orderline_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uq_product_orderline_id` (`orderline_id`),
  CONSTRAINT `fk_product_orderline` FOREIGN KEY (`orderline_id`) REFERENCES `orderline` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `product` VALUES ('1','Product_Name1','1');
COMMIT;
INSERT INTO `product` VALUES ('2','Product_Name2','2');
COMMIT;
INSERT INTO `product` VALUES ('3','Product_Name3','');
COMMIT;

/* 
professor
*/
CREATE TABLE `professor` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `professor` VALUES ('1','Professor_Name1');
COMMIT;
INSERT INTO `professor` VALUES ('2','Professor_Name2');
COMMIT;
INSERT INTO `professor` VALUES ('3','Professor_Name3');
COMMIT;
INSERT INTO `professor` VALUES ('4','Professor_Name4');
COMMIT;
INSERT INTO `professor` VALUES ('5','Professor_Name5');
COMMIT;
INSERT INTO `professor` VALUES ('6','Professor_Name6');
COMMIT;
INSERT INTO `professor` VALUES ('7','Professor_Name7');
COMMIT;
INSERT INTO `professor` VALUES ('8','Professor_Name8');
COMMIT;
INSERT INTO `professor` VALUES ('9','Professor_Name9');
COMMIT;
INSERT INTO `professor` VALUES ('10','Professor_Name10');
COMMIT;
INSERT INTO `professor` VALUES ('11','Professor_Name11');
COMMIT;
INSERT INTO `professor` VALUES ('12','Professor_Name12');
COMMIT;
INSERT INTO `professor` VALUES ('13','Professor_Name13');
COMMIT;
INSERT INTO `professor` VALUES ('14','Professor_Name14');
COMMIT;
INSERT INTO `professor` VALUES ('15','Professor_Name15');
COMMIT;

/* 
project
*/
CREATE TABLE `project` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `project` VALUES ('1','Project_Name1');
COMMIT;
INSERT INTO `project` VALUES ('2','Project_Name2');
COMMIT;
INSERT INTO `project` VALUES ('3','Project_Name3');
COMMIT;
INSERT INTO `project` VALUES ('4','Project_Name4');
COMMIT;
INSERT INTO `project` VALUES ('5','Project_Name5');
COMMIT;
INSERT INTO `project` VALUES ('6','Project_Name6');
COMMIT;
INSERT INTO `project` VALUES ('7','Project_Name7');
COMMIT;
INSERT INTO `project` VALUES ('8','Project_Name8');
COMMIT;
INSERT INTO `project` VALUES ('9','Project_Name9');
COMMIT;
INSERT INTO `project` VALUES ('10','Project_Name10');
COMMIT;

/* 
restaurant
*/
CREATE TABLE `restaurant` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `restaurant` VALUES ('1','Rest1');
COMMIT;
INSERT INTO `restaurant` VALUES ('2','Rest2');
COMMIT;
INSERT INTO `restaurant` VALUES ('3','Rest3');
COMMIT;
INSERT INTO `restaurant` VALUES ('4','Rest4');
COMMIT;
INSERT INTO `restaurant` VALUES ('5','Rest5');
COMMIT;

/* 
shirt
*/
CREATE TABLE `shirt` (
  `id` int(8) NOT NULL default '0',
  `style` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
shirt_button
*/
CREATE TABLE `shirt_button` (
  `shirt_id` int(8) NOT NULL default '0',
  `button_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`shirt_id`,`button_id`),
  UNIQUE KEY `uq_shirt_button_shirt_id` (`shirt_id`),
  KEY `fk_shirt_button_button` (`button_id`),
  CONSTRAINT `fk_shirt_button_button` FOREIGN KEY (`button_id`) REFERENCES `button` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_shirt_button_shirt` FOREIGN KEY (`shirt_id`) REFERENCES `shirt` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
shoes
*/
CREATE TABLE `shoes` (
  `id` int(8) NOT NULL default '0',
  `discriminator` VARCHAR(50) DEFAULT NULL,
  `color` VARCHAR(50) DEFAULT NULL,
  `sports_type` VARCHAR(50) DEFAULT NULL,
  `designer_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_shoes_designer` (`designer_id`),
  CONSTRAINT `fk_shoes_designer` FOREIGN KEY (`designer_id`) REFERENCES `designer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;


/* 
song
*/
CREATE TABLE `song` (
  `id` int(8) NOT NULL default '0',
  `title` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
string_key
*/
CREATE TABLE `string_key` (
  `id` VARCHAR(50) NOT NULL DEFAULT '',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `string_key` VALUES ('ID1','String_Key_Name1');
COMMIT;
INSERT INTO `string_key` VALUES ('ID2','String_Key_Name2');
COMMIT;
INSERT INTO `string_key` VALUES ('ID3','String_Key_Name3');
COMMIT;
INSERT INTO `string_key` VALUES ('ID4','String_Key_Name4');
COMMIT;
INSERT INTO `string_key` VALUES ('ID5','String_Key_Name5');
COMMIT;

/* 
string_primitive_key
*/
CREATE TABLE `string_primitive_key` (
  `id` VARCHAR(50) NOT NULL DEFAULT '',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;


/* 
student
*/
CREATE TABLE `student` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ;

INSERT INTO `student` VALUES ('1','Student_Name1');
COMMIT;
INSERT INTO `student` VALUES ('2','Student_Name2');
COMMIT;
INSERT INTO `student` VALUES ('3','Student_Name3');
COMMIT;
INSERT INTO `student` VALUES ('4','Student_Name4');
COMMIT;
INSERT INTO `student` VALUES ('5','Student_Name5');
COMMIT;
INSERT INTO `student` VALUES ('6','Student_Name6');
COMMIT;
INSERT INTO `student` VALUES ('7','Student_Name7');
COMMIT;
INSERT INTO `student` VALUES ('8','Student_Name8');
COMMIT;
INSERT INTO `student` VALUES ('9','Student_Name9');
COMMIT;
INSERT INTO `student` VALUES ('10','Student_Name10');
COMMIT;

/* 
suit
*/
CREATE TABLE `suit` (
  `id` int(8) NOT NULL default '0',
  `name` VARCHAR(50) DEFAULT NULL,
  `deck_id` int(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_suit_deck` (`deck_id`),
  CONSTRAINT `fk_suit_deck` FOREIGN KEY (`deck_id`) REFERENCES `deck` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `suit` VALUES ('1','Spade','1');
COMMIT;
INSERT INTO `suit` VALUES ('2','Flower','1');
COMMIT;
INSERT INTO `suit` VALUES ('3','Diamond','1');
COMMIT;
INSERT INTO `suit` VALUES ('4','Heart','1');
COMMIT;

/* 
tenured_professor
*/
CREATE TABLE `tenured_professor` (
  `professor_id` int(8) NOT NULL default '0',
  `tenured_year` int(4) default NULL,
  PRIMARY KEY  (`professor_id`),
  CONSTRAINT `fk_tenured_professor_professor` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `tenured_professor` VALUES ('1','1');
COMMIT;
INSERT INTO `tenured_professor` VALUES ('2','2');
COMMIT;
INSERT INTO `tenured_professor` VALUES ('3','3');
COMMIT;
INSERT INTO `tenured_professor` VALUES ('4','4');
COMMIT;
INSERT INTO `tenured_professor` VALUES ('5','5');
COMMIT;

/* 
undergraduate_student
*/
CREATE TABLE `undergraduate_student` (
  `student_id` int(8) NOT NULL default '0',
  PRIMARY KEY  (`student_id`),
  CONSTRAINT `fk_undergraduate_stude_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

INSERT INTO `undergraduate_student` VALUES ('1');
COMMIT;
INSERT INTO `undergraduate_student` VALUES ('2');
COMMIT;
INSERT INTO `undergraduate_student` VALUES ('3');
COMMIT;
INSERT INTO `undergraduate_student` VALUES ('4');
COMMIT;
INSERT INTO `undergraduate_student` VALUES ('5');
COMMIT;

/* 
wheel
*/
CREATE TABLE `wheel` (
  `id` int(8) NOT NULL default '0',
  `radius` int(8) default NULL,
  PRIMARY KEY  (`id`)
) ;
