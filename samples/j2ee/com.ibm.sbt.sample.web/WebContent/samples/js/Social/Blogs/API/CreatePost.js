require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var post = blogService.newPost(); 
        var now = new Date();
        post.setTitle("Post at " + now.getTime());
        post.setContent("Post Content at " + now.getTime());
//        blog.setContent("Test blog created: " + now);
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    var blog = blogs[0];
                    var firstBlogHandle = blog.getHandle();
			        var promise = blogService.createPost(post, firstBlogHandle);
			        promise.then(
		        		function(post) {
		                    dom.setText("json", json.jsonBeanStringify(post.toJson()));
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