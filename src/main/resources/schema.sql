CREATE TABLE ENGINE (
  ENGINE_ID VARCHAR2(20) NOT NULL,
  SERVER_ID VARCHAR2(20) NOT NULL,
  MEMORY_MB_MAX_VALUE NUMBER,
  MEMORY_MB_MIN_VALUE NUMBER,
  EXE_FILE_PATH VARCHAR2(100),
  CONFIG_FILE_PATH VARCHAR2(100),
  API_PORT NUMBER,
  IS_DELETED CHAR(1) DEFAULT 'N' NOT NULL,
  CONSTRAINT PK_ENGINE PRIMARY KEY (ENGINE_ID)
);

CREATE TABLE SERVER (
  SERVER_ID VARCHAR2(20) NOT NULL,
  HOST_ADDRESS VARCHAR2(15) NOT NULL,
  API_PORT NUMBER,
  OS_TYPE VARCHAR2(10),
  CLIENT_CONNECT_TYPE CHAR(1),
  OUT_HOST_ADDRESS VARCHAR2(15),
  OUT_API_PORT NUMBER,
  IS_DELETED CHAR(1) DEFAULT 'N' NOT NULL,
  START_DATE DATE,
  END_DATE DATE,
  CONSTRAINT PK_SERVER PRIMARY KEY (SERVER_ID)
);

CREATE TABLE ENGINE_CONFIG (
  ENGINE_ID VARCHAR2(20) NOT NULL,
  CONFIG_KEY VARCHAR2(100) NOT NULL,
  CONFIG_VALUE VARCHAR2(100) NOT NULL,
  IS_DELETED CHAR(1) NOT NULL DEFAULT 'N',
  LAST_UPDATE_TIME DATE,
  CONSTRAINT PK_ENGINE_CONFIG PRIMARY KEY (ENGINE_ID, CONFIG_KEY)
);

CREATE TABLE SERVER_CONFIG (
  SERVER_ID VARCHAR2(20) NOT NULL,
  CONFIG_KEY VARCHAR2(100) NOT NULL,
  CONFIG_VALUE VARCHAR2(100) NOT NULL,
  IS_DELETED CHAR(1) NOT NULL DEFAULT 'N',
  LAST_UPDATE_TIME DATE,
  CONSTRAINT PK_SERVER_CONFIG PRIMARY KEY (SERVER_ID, CONFIG_KEY)
);