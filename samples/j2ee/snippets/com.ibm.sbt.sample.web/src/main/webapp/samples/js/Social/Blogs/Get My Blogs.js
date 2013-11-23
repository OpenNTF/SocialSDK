require(["sbt/connections/BlogService", "sbt/dom"], 
    function(BlogService, dom) {
	    var createRow = function(blog) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, blog.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, blog.getBlogUuid());
	        tr.appendChild(td);
	    };
    
    	var blogService = new BlogService();
    	blogService.getMyBlogs({ ps: 5 }).then(
            function(blogs){
                if (blogs.length == 0) {
                    text = "My blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<blogs.length; i++){
                        var blog = blogs[i];
                        createRow(blog);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);