require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
    	blogService.getBlogs({ ps: 1 }).then(
            function(blogs){
            	return blogService.deleteBlog(blogs[0].getBlogUuid());
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