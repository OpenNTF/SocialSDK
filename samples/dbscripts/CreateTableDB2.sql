CREATE TABLE SBTKREP
(
	APPID VARCHAR(100) NOT NULL, 
	SERVICENAME VARCHAR(100) NOT NULL, 
	TYPE VARCHAR(100), 
	USERID VARCHAR(100) NOT NULL, 
	CREDENTIALTOKEN blob(1M) NOT NULL, 
	constraint PK_SBTKCONST primary key (APPID, SERVICENAME, USERID)
); 