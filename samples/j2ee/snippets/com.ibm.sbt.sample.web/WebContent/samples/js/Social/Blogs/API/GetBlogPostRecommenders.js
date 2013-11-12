require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
    	blogService.getRecommendedPosts({ ps: 1 }).then(
			function(posts){
				return blogService.getBlogPostRecommenders(posts[0],{ ps: 1 }); // returning newly created comment
            }
    	).then(
			function(recommenders) {
                dom.setText("json", json.jsonBeanStringify(recommenders));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	}
);