require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newPost();
        post.setTitle("Post at " + now.getTime());
        post.setContent("Post Content at " + now.getTime());
        var blog = blogService.newBlog(); 
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
        var comment = blogService.newComment();
        comment.setContent("Comment Content at " + now.getTime());
        
    	blogService.createBlog(blog).then(
            function(createdBlog){
                var blogHandle = createdBlog.getHandle();
		        var promise = blogService.createPost(post, blogHandle);
		        promise.then(
	        		function(createdPost) {
	        			var createdPostId = createdPost.getPostUuid();
	        			if (createdPostId) {
	        				var commentPromise = blogService.createComment(comment, blogHandle, createdPostId);
	                        commentPromise.then(
	                        		function(comment) {
	    			        			var createdCommentId = comment.getCommentUuid();
	    			        			if (createdCommentId) {
	    			        				var commentsPromise = blogService.getBlogComments(blogHandle,{ps:5});
	    			        				commentsPromise.then(
	    		    			        		function(comments) {
	    		    			                    dom.setText("json", json.jsonBeanStringify(comments));
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
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }   
    	);
					        
					        
    }
);