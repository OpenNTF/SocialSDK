require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
        
    	blogService.createBlog(blog).then(
            function(blog){
		        var promise = blogService.getBlogs({ ps: 5 });
		        promise.then(
		        		function(blogs) {
		                    dom.setText("json", json.jsonBeanStringify(blogs));
		                },
		                function(error) {
		                    dom.setText("json", json.jsonBeanStringify(error));
		                }
		        );
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }   
    	);
					        
					        
    }
);