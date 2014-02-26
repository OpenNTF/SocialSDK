require(["sbt/connections/IdeationBlogService", "sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(IdeationBlogService, BlogService, dom, json) {
        var ideationBlogService = new IdeationBlogService();
        var blogService = new BlogService();
        var now = new Date();
        // Assumes that at least one community has ideation blog widget added to it.
        var post = blogService.newBlogPost();  
        post.setTitle("Idea at " + now.getTime());
        post.setContent("Idea at " + now.getTime());
        blogService.getAllBlogs({ ps: 1, blogType: "ideationblog" }).then(
			function(ideationBlogs) {
				if (ideationBlogs && ideationBlogs.length > 0) {
					post.setBlogHandle(ideationBlogs[0].getHandle());
					return ideationBlogService.contributeIdea(post);
				}
			}
		).then(
			function(post) {
				if (post) {
					// Make sure there is at least one voted Idea
					ideationBlogService.voteIdea(post);
				}
			}
		).then(
			function() {
				return ideationBlogService.getMyVotedIdeas();
			}
		).then(
    		function(ideas) {
                dom.setText("json", json.jsonBeanStringify(ideas));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );		
	}

);
