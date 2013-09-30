require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var now = new Date();
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    var blog = blogs[0];
                    blog.setTitle("Blog Updated at " + now.getTime());
			        var promise = blogService.updateBlog(blog);
			        promise.then(
		        		function(updatedBlog) {
		                    dom.setText("json", json.jsonBeanStringify(updatedBlog.toJson()));
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