
require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("connections");
	    
	    var options = { 
	        method : "GET", 
	        handleAs : "text",
	        query : {
	        	query : "Test",
	        	scope : "profiles",
	        	facet : "{\"id\": \"Person\"}"
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