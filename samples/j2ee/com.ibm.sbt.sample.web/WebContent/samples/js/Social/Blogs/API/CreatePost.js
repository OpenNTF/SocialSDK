require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var post = blogService.newBlogPost(); 
        var now = new Date();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
//        blog.setContent("Test blog created: " + now);
        
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                return blogs[0].getHandle();
            }
        ).then(
            function(firstBlogHandle){
                return blogService.createPost(post, firstBlogHandle)
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