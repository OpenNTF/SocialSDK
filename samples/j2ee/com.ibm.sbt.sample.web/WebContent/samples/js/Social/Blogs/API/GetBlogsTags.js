require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        
    	blogService.getBlogsTags().then(
			function(tags) {
                dom.setText("json", json.jsonBeanStringify(tags));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
    	);
					        
					        
    }
);