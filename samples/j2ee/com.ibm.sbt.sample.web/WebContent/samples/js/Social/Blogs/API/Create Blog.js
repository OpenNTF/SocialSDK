require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(BlogService, dom, json) {
        var blogService = new BlogService();  
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
//        blog.setContent("Test blog created: " + now);
        var promise = blogService.createBlog(blog);
        promise.then(
        		function(blog) {
                    dom.setText("json", json.jsonBeanStringify(blog.toJson()));
                },
                function(error) {
                    dom.setText("json", json.jsonBeanStringify(error));
                }
        );
    }
);