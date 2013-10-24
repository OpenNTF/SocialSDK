require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
    	blogService.createBlog(blog).then(
            function(blog){
            	return blogService.deleteBlog(blog.getBlogUuid());
            }
        ).then(
    		function(deletedBlogId) {
                dom.setText("json", json.jsonBeanStringify({ deletedBlogId : deletedBlogId }));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );			        
    }
);