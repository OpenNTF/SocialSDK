require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("connections");
	    
	    var options = { 
	        method : "GET", 
	        handleAs : "text",
	        query : {
	        	query : "Test",
        		pageSize : "0",
	        	facet : "{\"id\": \"Person\"}"
	        }
	    };
	        
	    endpoint.request("/search/atom/search", options).then(
	        function(response) {
	            dom.setText("xml", response);
	        },
	        function(error){
	        	dom.setText("xml", error);
	        }
	    );
	}
);