require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 	
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        var comment = blogService.newComment();
        comment.setContent("Comment Content at " + now.getTime());
        var blogHandle;
        
    	blogService.getBlogs({ ps: 1 }).then(   //getting first blog by setting page size to 1
            function(blogs){
            	return blogs[0].getHandle();
            }
    	).then(
			function(firstBlogHandle){
				blogHandle = firstBlogHandle;
				return blogService.createPost(post, blogHandle); // returning newly created blog post
            }
    	).then(
			function(createdPost){
				return blogService.deletePost(blogHandle, createdPost.getBlogPostUuid()); // returning newly created comment
            }
    	).then(
			function(deletedPostId) {
				dom.setText("json", json.jsonBeanStringify({ deletedPostId : deletedPostId }));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	}
);