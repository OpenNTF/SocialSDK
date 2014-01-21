require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 	
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
    	blogService.getAllBlogPosts({ ps: 1 }).then(   //getting first blog by setting page size to 1
            function(blogPosts){
            	return blogService.getBlogPost(blogPosts[0].getPostUuid());
            }
    	).then(
			function(blogPost) {
				dom.setText("json", json.jsonBeanStringify(blogPost.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	}
);