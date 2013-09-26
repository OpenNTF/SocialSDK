require(["sbt/connections/BlogService", "sbt/dom"], 
    function(BlogService, dom) {
	    var createRow = function(post) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.innerHTML = post.getTitle();
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.innerHTML = post.getPostUuid();
	        tr.appendChild(td);
	    };
    
    	var blogService = new BlogService();
    	blogService.getBlogs({ ps: 1 }).then(
                function(blogs){
                    if (blogs.length == 0) {
                        text = "All blogs returned no results.";
                        dom.setText("content", text);
                    } else {
                    	
                        for(var i=0; i<blogs.length; i++){
                            var blog = blogs[i];
                            var firstBlogHandle = blog.getHandle();
                        	blogService.getBlogPosts(firstBlogHandle, { ps: 5 }).then(
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
                    }
                },
                function(error){
                    dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
                }       
        	);
    }
);