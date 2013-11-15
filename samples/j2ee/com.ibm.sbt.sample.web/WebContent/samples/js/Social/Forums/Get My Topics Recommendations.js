require(["sbt/connections/ForumService", "sbt/dom"], 
    function(ForumService,dom) {
        var createRow = function(recommendation) {
            var table = dom.byId("recommendationsTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(dom.createTextNode(recommendation.getPostUuid()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(dom.createTextNode(recommendation.getAuthor().name));
            tr.appendChild(td);
        };

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
                                createRow(recommendation);
                            }
                    	},
                    	function(error) {
                    		dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
                    	}
                    );
                }
        	},
        	function(error) {
        		dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
        	}
        );
    }
);