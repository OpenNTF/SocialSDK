<?php
	

	//SmartCloud OAuth 1.0 Endpoint Parameters
	$config['smartcloud_name']			= "Default IBM SmartCloud";
 	$config['name']						= "Default IBM SmartCloud";
 	$config['url'] 						= 'https://apps.na.collabserv.com';
 	$config['consumer_key'] 			= '';
 	$config['consumer_secret']			= '';
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

 	//TODO: Enter username and password
 	$config['basic_auth_username']		= "";
 	$config['basic_auth_password']		= "";
 	
 	$config['session_name'] = 'MoodleSession';
 	
 	$config['credentialStoreType'] = 'session'; // Must be either "session" or "cookie"
 	
 	
 	//SmartCloud OAuth 1.0 Endpoint Parameters
 	$config['wp_endpoint_2_name']						= "IBM SmartCloud for Social Business";
 	$config['wp_endpoint_2_url'] 						= 'https://apps.na.collabserv.com';
 	
 	//TODO: Enter consumer key / secret
 	$config['wp_endpoint_2_consumer_key'] 				= '';
 	$config['wp_endpoint_2_consumer_secret']			= '';
 	$config['wp_endpoint_2_request_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
 	$config['wp_endpoint_2_authorization_url']			= 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
 	$config['wp_endpoint_2_access_token_url']			= 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
 	$config['wp_endpoint_2_authentication_method'] 		= "oauth1"; //basic or oauth1
 	$config['wp_endpoint_2_basic_auth_method']			= 'global'; // prompt or global
 	
 	//TODO: Enter username and password
 	$config['wp_endpoint_2_basic_auth_username']		= "";
 	$config['wp_endpoint_2_basic_auth_password']		= "";
 	
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
 	
 	//TODO: Enter username and password
 	$config['wp_endpoint_1_basic_auth_username']		= "";
 	$config['wp_endpoint_1_basic_auth_password']		= "";
 	
 	$config['js_library'] = 'Dojo Toolkit 1.4.3';
 	
 	// Widget mock data
 	$config['plugin_name'] = 'My mock widget';
 	$config['javascript'] = 'require(["sbt/connections/FileService", "sbt/dom"], 
    function(FileService,dom) {
	    var createRow = function(file) {
	        var table = dom.byId("filesTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        var a = document.createElement("a");
	        a.href = file.getDownloadUrl();
	        a.appendChild(document.createTextNode(file.getTitle()));
	        td.appendChild(a);
	        tr.appendChild(td);
	        td = document.createElement("td");
	        tr.appendChild(td);
	        td.appendChild(document.createTextNode(file.getFileId()));	       
	    };

        var fileService = new FileService();        
    	fileService.getMyFiles().then(
            function(files) {
                if (files.length == 0) {
                	 dom.setText("content","You are not an owner of any files.");
                } else {
                    for(var i=0; i<files.length; i++){
                        var file = files[i];
                        createRow(file);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);';
 	$config['html'] = '<div role="main">
	<table class="table table-bordered" id="filesTable">
		<tr class="label label-info">
			<th>Title</th>
			<th>Id</th>
		</tr>
	</table>
</div>';
 	
 	
 // Available JS libraries
 $config['js_libraries'] = array(
 		'Dojo Toolkit 1.4.3',
 		'Dojo Toolkit 1.5.2',
 		'Dojo Toolkit 1.6.1',
 		'Dojo Toolkit 1.7.4',
 		'Dojo Toolkit 1.8..4',
 		'Dojo Toolkit 1.9.0',
 		'JQuery 1.8.3'
 );
	