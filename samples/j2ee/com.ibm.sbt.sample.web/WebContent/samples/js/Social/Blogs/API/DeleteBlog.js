require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    var blog = blogs[0];
                    blogId = blog.getBlogUuid();
			        var promise = blogService.deleteBlog(blogId);
			        promise.then(
		        		function(deletedBlogId) {
		                    dom.setText("json", json.jsonBeanStringify({ deletedBlogId : deletedBlogId }));
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