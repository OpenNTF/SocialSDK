require([ "sbt/config", "sbt/connections/BlogService", "sbt/dom", "sbt/json" ],
    function(config, BlogService, dom, json) {
	
		var postCount = 0;
	
		var addPost = function(post) {
        	var ul = dom.byId("posts");
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(post.getPostUuid()));
            ul.appendChild(li);
            
            ul = document.createElement("ul");
            li.appendChild(ul);
		};
			
		var readPosts = function(blogService, blogHandle, page, pageSize) {
			var args = { page: page, ps: pageSize, sortBy: "modified", sortOrder: "asc" };
			
			var promise = blogService.getBlogPosts(blogHandle, args);
			promise.then(
				function(posts) {
					if (posts.length == 0) {
						if (postCount == 0) {
							dom.setText("content", "No posts in the specified Blog");
						}
		            } else {
		            	postCount += posts.length;
		                for(var j=0; j<posts.length; j++){
		                    var post = posts[j];
		                    addPost(post);
		                }
		                
		                // read the rest
		                /*
		                dom.setText("totalResults", "Read "+postCount+" of "+promise.summary.totalResults+" blog posts.");
		                if (postCount < promise.summary.totalResults) {
		                	readPosts(blogService, blogHandle, ++page, pageSize);
		                }
		                */
		            }	
				},
				function(error) {
					dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
				}
			);
		};
		
		var blogHandle = "%{name=BlogService.blogHandle|helpSnippetId=Social_Blogs_Get_My_Blogs}";
	    var blogService = new BlogService(); 
		readPosts(blogService, blogHandle, 0, 50);
	}
);