<?php
	if ( ! defined('SBT_SDK')) exit('Access denied.');

	//SmartCloud OAuth 1.0 Endpoint Parameters
 	$config['name']						= "Default IBM SmartCloud";
 	$config['url'] 						= 'https://apps.na.collabserv.com';
 	$config['consumer_key'] 			= 'b43c48f51c498717c8547aee0590abc2';
 	$config['consumer_secret']			= 'd46e0237d5de41feb4b446089a0575f3';
 	$config['request_token_url']		= 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
 	$config['authorization_url']		= 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
 	$config['access_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
 	$config['authentication_method'] 	= "oauth1"; //basic or oauth1
	
	// Connections Endpoint Parameters
// 	$config['name']						= "Default IBM Connections";
// 	$config['url'] 						= 'https://qs.renovations.com:444';
// 	$config['consumer_key'] 			= '';
// 	$config['consumer_secret']			= '';
// 	$config['request_token_url']		= '';
// 	$config['authorization_url']		= '';
// 	$config['access_token_url']			= '';
// 	$config['authentication_method'] 	= "basic"; 
	
	//defaults to integrated sdk
 	$config['sdk_deploy_url'] 			=  "http://localhost/core/src/system/libs/js-sdk";

 	$config['basic_auth_username']		= "fadams";
 	$config['basic_auth_password']		= "passw0rd";
 	
 	$config['session_name'] = 'IBM_SBT_Wordpress_Session';
 	
 	$config['credentialStoreType'] = 'session'; // Must be either "session" or "cookie"