require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newPost();
        post.setTitle("Post at " + now.getTime());
        post.setContent("Post Content at " + now.getTime());
        var comment = blogService.newComment();
        comment.setContent("Comment Content at " + now.getTime());
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                        var firstBlogHandle = blogs[0].getHandle();
				        var promise = blogService.createPost(post, firstBlogHandle);
				        promise.then(
			        		function(post) {
			        			var createdPostId = post.getPostUuid();
			        			if (createdPostId) {
			        				var commentPromise = blogService.createComment(comment, firstBlogHandle, createdPostId);
			                        commentPromise.then(
		    			        		function(comment) {
		    			                    dom.setText("json", json.jsonBeanStringify(comment.toJson()));
		    			                },
		    			                function(error) {
		    			                    dom.setText("json", json.jsonBeanStringify(error));
		    			                }
		    				        );
			                    } else {
			                        text = "Post Id not found.";
			                        dom.setText("content", text);
			                    }
			                    //dom.setText("json", json.jsonBeanStringify(post.toJson()));
			                },
			                function(error) {
			                    dom.setText("json", json.jsonBeanStringify(error));
			                }
				        );
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }   
    	);
					        
					        
    }
);