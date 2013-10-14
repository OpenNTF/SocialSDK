require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newPost();
        post.setTitle("Post at " + now.getTime());
        post.setContent("Post Content at " + now.getTime());
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "No Blogs found.";
                    dom.setText("content", text);
                } else {
                        var firstBlogHandle = blogs[0].getHandle();
				        var promise = blogService.createPost(post, firstBlogHandle);
				        promise.then(
			        		function(post) {
			        			var postId = post.getPostUuid();
		        				var updatePromise = blogService.recommendPost(postId, firstBlogHandle);
		        				updatePromise.then(
	    			        		function(recommendedPost) {
	    			                    dom.setText("json", json.jsonBeanStringify(recommendedPost.toJson()));
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