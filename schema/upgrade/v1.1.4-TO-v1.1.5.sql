-- Upgrade from version 1.1.4 to version 1.1.5

-- This file contains sql for Oracle and MySQL

------------
-- Oracle --
------------
ALTER SESSION set NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';


alter table MERCHANT_USER_INFORMATION add SECURITY_QUESTION_1 VARCHAR2(64 CHAR);
alter table MERCHANT_USER_INFORMATION add SECURITY_QUESTION_2 VARCHAR2(64 CHAR);
alter table MERCHANT_USER_INFORMATION add SECURITY_QUESTION_3 VARCHAR2(64 CHAR);
alter table MERCHANT_USER_INFORMATION add SECURITY_ANSWER_1 VARCHAR2(64 CHAR);
alter table MERCHANT_USER_INFORMATION add SECURITY_ANSWER_2 VARCHAR2(64 CHAR);
alter table MERCHANT_USER_INFORMATION add SECURITY_ANSWER_3 VARCHAR2(64 CHAR);

delete from MERCHANT_USER_ROLE_DEF where role_code='seller';

INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (3, 'store', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (4, 'catalog', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (5, 'checkout', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (6, 'order', 'Store operator');

alter table CENTRAL_FUNCTION add ROLE VARCHAR2(32 CHAR);

update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=1;
update CENTRAL_FUNCTION set ROLE='catalog' where CENTRAL_GROUP_CODE='CAT';
update CENTRAL_FUNCTION set ROLE='checkout' where CENTRAL_GROUP_CODE='CHK';
update CENTRAL_FUNCTION set ROLE='order' where CENTRAL_GROUP_CODE='ORD';
update CENTRAL_FUNCTION set ROLE='checkout' where CENTRAL_GROUP_CODE='CHK';
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_GROUP_CODE='CAR';
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=40;
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=41;



-----------------------------
---DATA (Oracle and MySQL)---
-----------------------------


alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_QUESTION_1 VARCHAR(64) DEFAULT NULL;
alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_QUESTION_2 VARCHAR(64) DEFAULT NULL;
alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_QUESTION_3 VARCHAR(64) DEFAULT NULL;
alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_ANSWER_1 VARCHAR(64) DEFAULT NULL;
alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_ANSWER_2 VARCHAR(64) DEFAULT NULL;
alter table MERCHANT_USER_INFORMATION add COLUMN SECURITY_ANSWER_3 VARCHAR(64) DEFAULT NULL;

delete from MERCHANT_USER_ROLE_DEF where role_code='seller';

INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (3, 'store', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (4, 'catalog', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (5, 'checkout', 'Store operator');
INSERT INTO MERCHANT_USER_ROLE_DEF(roleid , role_code, role_description) VALUES (6, 'order', 'Store operator');

alter table CENTRAL_FUNCTION add ROLE VARCHAR(32) DEFAULT NULL;

update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=1;
update CENTRAL_FUNCTION set ROLE='catalog' where CENTRAL_GROUP_CODE='CAT';
update CENTRAL_FUNCTION set ROLE='checkout' where CENTRAL_GROUP_CODE='CHK';
update CENTRAL_FUNCTION set ROLE='order' where CENTRAL_GROUP_CODE='ORD';
update CENTRAL_FUNCTION set ROLE='checkout' where CENTRAL_GROUP_CODE='CHK';
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_GROUP_CODE='CAR';
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=40;
update CENTRAL_FUNCTION set ROLE='store' where CENTRAL_FUNCTION_ID=41;


