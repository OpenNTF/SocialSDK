
require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
		
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newPost();
        post.setTitle("Post at " + now.getTime());
        post.setContent("Post Content at " + now.getTime());
        var blogHandle;
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
            	return blogs[0].getHandle();
            }
    	).then(
			function(firstBlogHandle){
				blogHandle = firstBlogHandle;
				return blogService.createPost(post, blogHandle);
            }
    	).then(
			function(createdPost){
				return blogService.recommendPost(createdPost.getPostUuid(), blogHandle);
            }
    	).then(
    			function(recommendedPost) {
                    dom.setText("json", json.jsonBeanStringify(recommendedPost.toJson()));
                },
                function(error) {
                    dom.setText("json", json.jsonBeanStringify(error));
                }
         );
	
	}

);
