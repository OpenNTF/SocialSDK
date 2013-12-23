
require(["sbt/connections/IdeationBlogService", "sbt/dom", "sbt/json"], 
    function(IdeationBlogService, dom, json) {
        var ideationBlogService = new IdeationBlogService();
        ideationBlogService.getMyVotedIdeas({ ps: 1 }).then(
			function(Ideas) {
				var unVotePromise = ideationBlogService.unvoteIdea(Ideas[0]);//test case assumes that there is at-least 1 voted idea existing
				unVotePromise.then(
					function(){
		    			var results = {};
		    			results.status = unVotePromise.response.status;
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
//require(["sbt/connections/IdeationBlogService", "sbt/dom", "sbt/json"], 
//	
//    function(IdeationBlogService, dom, json) {
//        var ideationBlogService = new IdeationBlogService();
//        ideationBlogService.getMyVotedIdeas({ ps: 1 }).then(
//			function(Ideas) {
//				var unVotePromise = ideationBlogService.unvoteIdea(Ideas[0]);//test case assumes that there is at-least 1 voted idea existing
//				unVotePromise.then(
//					function(){
//		    			var results = {};
//		    			results.status = unVotePromise.response.status;
//		    			dom.setText("json", json.jsonBeanStringify(results));
//					},
//	                function(error) {
//	                    dom.setText("json", json.jsonBeanStringify(error));
//	                }
//    			);
//            }
//		);
//	
//	}
//
//);
