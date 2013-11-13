require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
    	var forumService = new ForumService();	
    	var topicUuid = "%{name=ForumService.topicUuid}";	
        var promise = forumService.deleteForumTopic(topicUuid);
        promise.then(
            function(topicUuid) {
                dom.setText("json", json.jsonBeanStringify({ topicUuid : topicUuid }));
            }, function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
