require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var promise = blogService.getBlogsTags();
    	promise.then(
			function(tags) {
				dom.setText("json", json.jsonBeanStringify(promise.response));
                dom.setText("response", json.jsonBeanStringify(tags));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
    	);
					        
					        
    }
);