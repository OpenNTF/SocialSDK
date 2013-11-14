require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
    function(BlogService, dom, json) {
		var createRow = function(post) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, post.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, post.getBlogPostUuid());
	        tr.appendChild(td);
	    };
	    var blogService = new BlogService(); 
		blogService.getBlogs({ ps: 1 }).then(   //getting first blog by setting page size to 1
	        function(blogs){
	        	return blogs[0].getHandle();
	        }
		).then(
			function(firstBlogHandle){
				return blogService.getBlogPosts(firstBlogHandle, { ps: 5 });
	        }
		).then(
            function(posts){
                if (posts.length == 0) {
                    text = "All blogs posts returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var j=0; j<posts.length; j++){
                        var post = posts[j];
                        createRow(post);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
	}
);