require([ "sbt/config", "sbt/dom", "sbt/stringUtil" ], 
	function(config, dom, stringUtil) {
	    var endpoint = config.findEndpoint("smartcloud");
	
	    var options = { 
	        method : "GET", 
	        handleAs : "text",
		    headers : { "Content-Type" : "application/json" }
	    };
	    
	    var url = "/lotuslive-shindig-server/social/rest/people/lotuslive:user:{subscriberId}/@self";
	    url = stringUtil.replace(url, { subscriberId : "%{sample.smartcloud.subscriberId}" });
	    
	    endpoint.request(url, options).then(
	        function(response) {
	            dom.setText("content", response);
	        },
	        function(error){
	        	dom.setText("content", error);
	        }
	    );
	}
);