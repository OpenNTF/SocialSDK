
require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
		
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        var blogHandle;
        var blogPostId;
        
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
				blogPostId = createdPost.getBlogPostUuid();
				return blogService.recommendPost(blogPostId, blogHandle);
            }
    	).then(
			function() {
				var unRecommendPromise = blogService.unrecommendPost(blogPostId, blogHandle);
				unRecommendPromise.then(
				function(){
	    			var returnOjject = {};
	    			returnOjject.status = unRecommendPromise.response.status;
	    			dom.setText("json", json.jsonBeanStringify(returnOjject));
				},
                function(error) {
                    dom.setText("json", json.jsonBeanStringify(error));
                }
    			);
            }
		);
	
	}

);
