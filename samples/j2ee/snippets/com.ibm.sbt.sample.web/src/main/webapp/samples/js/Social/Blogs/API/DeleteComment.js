require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 	
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        var comment = blogService.newComment();
        comment.setContent("Comment Content at " + now.getTime());
        
        blogService.getAllBlogs({ ps: 1 }).then(   //getting first blog by setting page size to 1
            function(blogs){
            	post.setBlogHandle(blogs[0].getHandle());
            	return blogService.createPost(post); // returning newly created blog post
            }
    	).then(
			function(createdPost){
				comment.setBlogHandle(createdPost.getBlogHandle());
				comment.setPostUuid(createdPost.getPostUuid());
				return blogService.createComment(comment); // returning newly created comment
            }
    	).then(
			function(createdComment){
				return blogService.deleteComment(createdComment.getBlogHandle(), createdComment.getCommentUuid());
            }
    	).then(
			function(deletedComment) {
				dom.setText("json", json.jsonBeanStringify({ deletedComment : deletedComment }));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	}
);