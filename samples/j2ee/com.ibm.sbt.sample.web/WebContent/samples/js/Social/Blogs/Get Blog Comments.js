require(["sbt/connections/BlogService", "sbt/dom"], 
    function(BlogService, dom) {
	    var createRow = function(comment) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.innerHTML = comment.getTitle();
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.innerHTML = comment.getCommentUuid();
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
                        	blogService.getBlogComments(firstBlogHandle, { ps: 5 }).then(
                                    function(comments){
                                        if (comments.length == 0) {
                                            text = "All blogs posts returned no results.";
                                            dom.setText("content", text);
                                        } else {
                                            for(var j=0; j<comments.length; j++){
                                                var comment = comments[j];
                                                createRow(comment);
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