require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
        blog.setTags(['testTag1', 'testTag2']);
    	blogService.createBlog(blog).then(
	    	function() {
	    		return blogService.getFeaturedBlogs({ ps: 5 });
	        }
	    ).then(
    		function(blogs) {
                dom.setText("json", json.jsonBeanStringify(blogs));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );		       					        
    }
);