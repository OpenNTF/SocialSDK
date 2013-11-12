require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService,dom,json) {
		var results = [];
		
        var forumService = new ForumService();
        forumService.getMyTopics().then(
        	function(topics) {
        		if (topics.length == 0) {
        			dom.setText("content", "You have not created any topics yet!");
        		}
        		for(var i=0; i<topics.length; i++) {
                    var topic = topics[i];
                    var topicUuid = topic.getTopicUuid();
                    forumService.getForumRecommendations(topicUuid).then(
                    	function(recommendations) {
                    		for(var i=0; i<recommendations.length; i++) {
                                var recommendation = recommendations[i];
                                results.push({
                                	postUuid: recommendation.getPostUuid(),
                                	recommendationUuid : recommendation.getRecommendationUuid(),
                                	author : recommendation.getAuthor()
                                });
                                dom.setText("json", json.jsonBeanStringify(results));
                            }
                    	},
                    	function(error) {
                    		dom.setText("json", json.jsonBeanStringify(error));
                    	}
                    );
                }
        	},
        	function(error) {
        		dom.setText("json", json.jsonBeanStringify(error));
        	}
        );
    }
);