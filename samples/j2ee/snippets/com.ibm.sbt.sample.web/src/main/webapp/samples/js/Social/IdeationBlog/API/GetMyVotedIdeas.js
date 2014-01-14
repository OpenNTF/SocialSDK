require(["sbt/connections/IdeationBlogService", "sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
    function(IdeationBlogService, BlogService, dom, json) {
        var ideationBlogService = new IdeationBlogService();
        var blogService = new BlogService();
        var now = new Date();
        var post = blogService.newBlogPost(); // assumes that atleast one community has Ideation blog widget added to it. 
        post.setTitle("Idea at " + now.getTime());
        post.setContent("Idea at " + now.getTime());
        blogService.getAllBlogs({ ps: 1, blogType: "ideationblog" }).then(
			function(IdeationBlogs) {
				post.setBlogHandle(IdeationBlogs[0].getHandle());
				return ideationBlogService.contributeIdea(post);
			}
		).then(
			function(post){
				ideationBlogService.voteIdea(post);//making sure there is atleast one voted Idea
			}
		).then(
			function(){
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
