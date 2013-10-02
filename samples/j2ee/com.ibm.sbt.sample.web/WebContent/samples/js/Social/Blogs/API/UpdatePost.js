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
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                        var firstBlogHandle = blogs[0].getHandle();
				        var promise = blogService.createPost(post, firstBlogHandle);
				        promise.then(
			        		function(post) {
			        			post.setTitle("Post Updated at " + now.getTime());
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