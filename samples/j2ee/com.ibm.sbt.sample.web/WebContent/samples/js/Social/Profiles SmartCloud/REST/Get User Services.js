require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("smartcloud");
	
	    var options = { 
	        method : "GET", 
	        handleAs : "text"
	    };
	        
	    endpoint.request("/manage/oauth/getUserServices", options).then(
	        function(response) {
	            dom.setText("content", response);
	        },
	        function(error){
	        	dom.setText("content", error);
	        }
	    );
	}
);