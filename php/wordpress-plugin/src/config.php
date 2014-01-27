<?php
	if ( ! defined('SBT_SDK')) exit('Access denied.');

	//SmartCloud OAuth 1.0 Endpoint Parameters
	$config['smartcloud_name']			= "Default IBM SmartCloud";
 	$config['name']						= "Default IBM SmartCloud";
 	$config['url'] 						= 'https://apps.na.collabserv.com';
 	$config['consumer_key'] 			= 'b43c48f51c498717c8547aee0590abc2';
 	$config['consumer_secret']			= 'd46e0237d5de41feb4b446089a0575f3';
 	$config['request_token_url']		= 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
 	$config['authorization_url']		= 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
 	$config['access_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
 	$config['authentication_method'] 	= "oauth1"; //basic or oauth1
	
	// Connections Endpoint Parameters
//  	$config['connections_name']			= "Default IBM Connections";
// 	$config['name']						= "Default IBM Connections";
// 	$config['url'] 						= 'https://qs.renovations.com:444';
// 	$config['consumer_key'] 			= '';
// 	$config['consumer_secret']			= '';
// 	$config['request_token_url']		= '';
// 	$config['authorization_url']		= '';
// 	$config['access_token_url']			= '';
// 	$config['authentication_method'] 	= "basic"; 
// 	$config['basic_auth_method']		= 'global'; // prompt or global
	
	
	//defaults to integrated sdk
 	$config['sdk_deploy_url'] 			=  "http://localhost/core/src/system/libs/js-sdk";

 	$config['basic_auth_username']		= "fadams";
 	$config['basic_auth_password']		= "passw0rd";
 	
 	$config['session_name'] = 'MoodleSession';
 	
 	$config['credentialStoreType'] = 'session'; // Must be either "session" or "cookie"
 	
 	
 	//SmartCloud OAuth 1.0 Endpoint Parameters
 	$config['wp_endpoint_2_name']						= "IBM SmartCloud for Social Business";
 	$config['wp_endpoint_2_url'] 						= 'https://apps.na.collabserv.com';
 	$config['wp_endpoint_2_consumer_key'] 				= 'b43c48f51c498717c8547aee0590abc2';
 	$config['wp_endpoint_2_consumer_secret']			= 'd46e0237d5de41feb4b446089a0575f3';
 	$config['wp_endpoint_2_request_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
 	$config['wp_endpoint_2_authorization_url']			= 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
 	$config['wp_endpoint_2_access_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
 	$config['wp_endpoint_2_authentication_method'] 		= "oauth1"; //basic or oauth1
 	$config['wp_endpoint_2_basic_auth_method']			= 'global'; // prompt or global
 	$config['wp_endpoint_2_basic_auth_username']		= "fadams";
 	$config['wp_endpoint_2_basic_auth_password']		= "passw0rd";
 	
 	// Connections Endpoint Parameters
 	$config['wp_endpoint_1_name']						= "IBM Connections on Premises";
 	$config['wp_endpoint_1_url'] 						= 'https://qs.renovations.com:444';
 	$config['wp_endpoint_1_consumer_key'] 				= '';
 	$config['wp_endpoint_1_consumer_secret']			= '';
 	$config['wp_endpoint_1_request_token_url']			= '';
 	$config['wp_endpoint_1_authorization_url']			= '';
 	$config['wp_endpoint_1_access_token_url']			= '';
 	$config['wp_endpoint_1_authentication_method'] 		= "basic";
 	$config['wp_endpoint_1_basic_auth_method']			= 'global'; // prompt or global
 	$config['wp_endpoint_1_basic_auth_username']		= "fadams";
 	$config['wp_endpoint_1_basic_auth_password']		= "passw0rd";
//  	wp_endpoint_1
 	
	//session bridging
	//require_once ('sbridge.php');
	
