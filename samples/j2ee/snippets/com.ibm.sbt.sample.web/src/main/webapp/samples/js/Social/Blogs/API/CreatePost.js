require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var post = blogService.newBlogPost(); 
        var now = new Date();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
//        blog.setContent("Test blog created: " + now);
        
    	blogService.getAllBlogs({ ps: 1 }).then(
            function(blogs){
            	post.setBlogHandle(blogs[0].getHandle());
                return blogService.createPost(post);
            }
        ).then(
    			function(post) {
                    dom.setText("json", json.jsonBeanStringify(post.toJson()));
                },
                function(error) {
                    dom.setText("json", json.jsonBeanStringify(error));
                }
         );
	}
);