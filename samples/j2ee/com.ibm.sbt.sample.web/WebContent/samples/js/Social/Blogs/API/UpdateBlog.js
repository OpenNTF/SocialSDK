require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
		var now = new Date();
        var blogService = new BlogService();  
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
            	var blog = blogs[0];
                blog.setTitle("Blog Updated at " + now.getTime());
		        return blogService.updateBlog(blog);
            }
        ).then(
    		function(updatedBlog) {
                dom.setText("json", json.jsonBeanStringify(updatedBlog.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );		        
    }
);