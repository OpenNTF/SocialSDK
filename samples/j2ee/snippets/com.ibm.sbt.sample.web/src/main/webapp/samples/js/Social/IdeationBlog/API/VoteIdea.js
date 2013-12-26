
require(["sbt/connections/IdeationBlogService", "sbt/connections/BlogService", "sbt/dom", "sbt/json"], 
	
    function(IdeationBlogService, BlogService, dom, json) {
        var ideationBlogService = new IdeationBlogService();
        var blogService = new BlogService();
        var now = new Date();
        var post = blogService.newBlogPost(); // assumes that atleast one community has Ideation blog widget added to it. 
        post.setTitle("BlogPost at " + now.getTime());
        post.setContent("BlogPost Content at " + now.getTime());
        blogService.getBlogs({ ps: 1, blogType: "ideationblog" }).then(
			function(IdeationBlogs) {
				post.setBlogHandle(IdeationBlogs[0].getHandle());
				return ideationBlogService.contributeIdea(post);//test case assumes that there is at-least 1 voted idea existing
			}
		).then(
			function(post){
				var votePromise = ideationBlogService.voteIdea(post);//test case assumes that there is at-least 1 voted idea existing
				votePromise.then(
					function(){
		    			var results = {};
		    			results.status = votePromise.response.status;
		    			dom.setText("json", json.jsonBeanStringify(results));
					},
	                function(error) {
	                    dom.setText("json", json.jsonBeanStringify(error));
	                }
    			);
			}
		);
	
	}

);
