/**
* =================System user table================================
*/



/**
* =================System user Role==================================
*/




/**
* ================System user role authority==========================
*/





/**
* ===============Spring Oauth2 tables script=========================
*/
/*第三方应用客户端登记表*/
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY NOT NULL COMMENT '客户端编号',
  client_secret VARCHAR(256) NOT NULL COMMENT '客户端密码',
  resource_ids VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity int,
  refresh_token_validity int,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256) COMMENT 'true|false'
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='第三方应用客户端登录信息表';

delete from oauth_client_details

insert into oauth_client_details values('qq','123456',null,'read,write','authorization_code,client_credentials,password,refresh_token,implicit','http://example.com','ADMIN','3600','3600',null,'true,false');


/*第三方应用客户端审批权限(scope)，默认存储1个月*/
create table oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);


create table oauth_client_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONGVARBINARY,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication LONGVARBINARY
);

create table oauth_code (
  code VARCHAR(256), authentication LONGVARBINARY
);

-- customized oauth_client_details table
create table ClientDetails (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);




