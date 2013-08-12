require([ "sbt/config", "sbt/dom" ], 
	function(config, dom) {
	    var endpoint = config.findEndpoint("connections");
	    
	    
	    
	    var options = { 
	        method : "GET", 
	        handleAs : "text",
	        // Query parameters
	        // component : Limits the search results to include items from a specific application e.g. activities|blogs|communities|dogear|files|forums|profiles|wikis 
	        // components : Limits the search results to include items from a specific applications e.g. activities,blogs,communities,dogear,files,forums,profiles,wikis
	        // email : Limits the results to only those items associated with the person who has the specified email address
	        // lang : Specifies a language in which to search for a string
	        // query : Text to search for. Returns a list of results with the specified text in the title, description, or content.
	        // tag : Tag to search for. Returns a list of results with the specified tag.
	        // userid : Limits the results to only those items associated with the person who has the specified user ID.
	        // page :	Page number. Specifies the page to be returned. The default is 1.
	        // ps : Page size. Specify the number of entries to return per page.
	        // start : Defines an offset from the first result in the set. This parameter is ignored if a page parameter is provided. This starts from 0. The default is 0.
	        query : {
	        	query : "%{sample.displayName1}"
	        }
	    };
	        
	    endpoint.request("/search/atom/search/results", options).then(
	        function(response) {
	            dom.setText("xml", response);
	        },
	        function(error){
	        	dom.setText("xml", error);
	        }
	    );
	}
);