require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("smartcloud");
	
	    var options = { 
	        method : "GET", 
	        handleAs : "text"
	    };
	        
	    endpoint.request("/lotuslive-shindig-server/social/rest/people/@me/@self?format=json", options).then(
	        function(response) {
	            dom.setText("content", response);
	        },
	        function(error){
	        	dom.setText("content", error);
	        }
	    );
	}
);