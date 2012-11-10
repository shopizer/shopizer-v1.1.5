-- Upgrade from version 1.1 to version 1.1.1

-- For oracle ALTER SESSION set NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
INSERT INTO CENTRAL_FUNCTION VALUES(24, 'SFCTSC01', 'CAR', 'Store front content section', 1, 1, '/merchantstore/pageContentList.action', 0, '2010-01-01 00:00:00', '2010-01-01 00:00:00');
INSERT INTO CENTRAL_REG_ASSOCIATION VALUES(57, 1, ' ', 'SFCTSC01', 0, '2010-01-01 00:00:00');
INSERT INTO CENTRAL_REG_ASSOCIATION VALUES(58, 2, ' ', 'SFCTSC01', 0, '2010-01-01 00:00:00');

INSERT INTO MODULE_CONFIGURATION (
  CONFIGURATION_MODULE,
  CONFIGURATION_KEY,
  COUNTRY_ISO_CODE_2,
  CONFIGURATION_VALUE
) VALUES
('decotemplate','content-decotemplate-product','XX','100');


INSERT INTO CORE_MODULES_SERVICES VALUES(83,2,'beanstream','CA',1,'Beanstream','/payment/beanstream.gif',1,1,0,'','https','www.beanstream.com','443','/scripts/process_transaction.asp','https','www.beanstream.com','443','/scripts/process_transaction.asp',1);
INSERT INTO CORE_MODULES_SERVICES VALUES(84,2,'beanstream','US',1,'Beanstream','/payment/beanstream.gif',1,1,0,'','https','www.beanstream.com','443','/scripts/process_transaction.asp','https','www.beanstream.com','443','/scripts/process_transaction.asp',1);
