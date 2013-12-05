require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
    function(BlogService, dom, json) {
		var createRow = function(recommender) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, recommender.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, recommender.getRecommenderUuid());
	        tr.appendChild(td);
	    };
        var blogService = new BlogService(); 
        var now = new Date();
        var post = blogService.newBlogPost();
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        var blog = blogService.newBlog(); 
        var now = new Date();
        blog.setTitle("Blog at " + now.getTime());
        blog.setHandle("BlogHandle " + now.getTime());
    	blogService.createBlog(blog).then(
            function(blog){
            	post.setBlogHandle(blog.getHandle());
				return blogService.createPost(post);
            }
        ).then(
			function(createdPost){
				return blogService.recommendPost(createdPost);
            }
    	).then(
    		function(){
				return blogService.getRecommendedPosts({ ps: 1 });
            }	
        ).then(
    		function(posts){
    			return blogService.getBlogPostRecommenders(posts[0],{ ps: 1 }); // returning newly created comment
    		}
		).then(
            function(recommenders){
                if (recommenders.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<recommenders.length; i++){
                        var recommender = recommenders[i];
                        createRow(recommender);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
	}
);