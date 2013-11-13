require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("connections");
	    
	    var options = { 
	        method : "GET", 
	        handleAs : "text",
	        query : {
	        	query : "%{name=sample.displayName1}",
	        	scope : "Profiles"
	        }
	    };
	        
	    endpoint.request("/search/atom/mysearch", options).then(
	        function(response) {
	            dom.setText("xml", response);
	        },
	        function(error){
	        	dom.setText("xml", error);
	        }
	    );
	}
);