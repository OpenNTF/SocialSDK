require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("connections");
	    
	    var options = { 
	        method : "GET", 
	        handleAs : "text"
	    };
	        
	    endpoint.request("/search/atom/scopes", options).then(
	        function(response) {
	            dom.setText("xml", response);
	        },
	        function(error){
	        	dom.setText("xml", error);
	        }
	    );
	}
);