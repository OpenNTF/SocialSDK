require([ "sbt/config", "sbt/connections/BlogService", "sbt/dom", "sbt/json" ],
    function(config, BlogService, dom, json) {
	
		var postCount = 0;
	
		var postToString = function(post) {
			return (!post) ? "" : post.getTitle() + 
					" [author=" + post.getAuthor().name + 
					", published=" + post.getPublished() +
					", updated=" + post.getUpdated() +
					", tags=" + post.getTags() +
					", recommendations=" + post.getRecommendationsCount() +
					", comments=" + post.getCommentCount() +
					", hits=" + post.getHitCount() + 
					", summary=" + post.getSummary() + "]";
		};
	
		var commentToString = function(comment) {
			return (!comment) ? "" : comment.getSummary() + 
					" [author=" + comment.getAuthor().name + 
					", published=" + comment.getPublished() + "]";
		};
	
		var addComment = function(comment, ul) {
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(commentToString(comment)));
            ul.appendChild(li);
		};
		
		var addPost = function(post) {
        	var ul = dom.byId("posts");
            var li = document.createElement("li");
            li.appendChild(dom.createTextNode(postToString(post)));
            ul.appendChild(li);
            
            ul = document.createElement("ul");
            li.appendChild(ul);
            
            if (post.getCommentCount() > 0) {
            	readComments(post, ul, 0, post.getCommentCount());
            }
		};
			
		var readComments = function(post, ul, start, size) {
		    var promise = post.getComments();
			promise.then(
				function(comments) {
					if (comments.length != 0) {
		                for(var j=0; j<comments.length; j++){
		                    var comment = comments[j];
		                    addComment(comment, ul);
		                }
		            }
				}
			);
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