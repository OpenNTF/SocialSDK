require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 	
    function(BlogService, dom, json) {
        var blogService = new BlogService(); 
        console.log("blogService blogService "+blogService);
        try{
    	blogService.getAllBlogs({ ps: 1 }).then(   //getting first blog by setting page size to 1
            function(blogs){
            	return blogService.getBlog(blogs[0].getBlogUuid());
            }
    	).then(
			function(blog) {
				dom.setText("json", json.jsonBeanStringify(blog.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
        }catch(e){
        	console.log("exp "+e);
        }
	}
);