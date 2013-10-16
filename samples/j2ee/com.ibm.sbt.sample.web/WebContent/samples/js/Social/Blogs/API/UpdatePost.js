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
				return blogService.updatePost(createdPost, blogHandle);
            }
    	).then(
    		function(updatedPost) {
                dom.setText("json", json.jsonBeanStringify(updatedPost.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
	}
);




require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        
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
			        			post.setTitle("BlogPost Updated at " + now.getTime());
		        				var updatePromise = blogService.updatePost(post, firstBlogHandle);
		        				updatePromise.then(
	    			        		function(updatedPost) {
	    			                    dom.setText("json", json.jsonBeanStringify(updatedPost.toJson()));
	    			                },
	    			                function(error) {
	    			                    dom.setText("json", json.jsonBeanStringify(error));
	    			                }
	    				        );
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