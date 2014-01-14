
require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
		
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
    	blogService.getAllBlogs({ ps: 1 }).then(
			function(blogs){
            	post.setBlogHandle(blogs[0].getHandle());
				return blogService.createPost(post);
            }
    	).then(
			function(createdPost){
				return blogService.recommendPost(createdPost);
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
