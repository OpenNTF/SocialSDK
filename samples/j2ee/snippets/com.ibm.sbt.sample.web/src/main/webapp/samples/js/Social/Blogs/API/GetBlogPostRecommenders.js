require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
    	blogService.createBlog(blog).then(
            function(blog){
            	post.setBlogHandle(blog.getHandle());
				return blogService.createPost(post);
            }
        ).then(
			function(createdPost){
				return blogService.recommendPost(createdPost);
            }
    	).then(
    		function(){
				return blogService.getRecommendedPosts({ ps: 1 });
            }	
        ).then(
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