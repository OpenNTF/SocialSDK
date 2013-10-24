
require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
		
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
            	post.setBlogHandle(blogs[0].getHandle());
            	return blogService.createPost(post);
            }
    	).then(
			function(){
				return blogService.getRecommendedPosts();
            }
    	).then(
			function(recommendedPosts) {
				try{
				var unRecommendPromise = blogService.unrecommendPost(recommendedPosts[0]);
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
				}catch(e){
					console.log("Exxc : "+e);
				}
            }
		);
	
	}

);
