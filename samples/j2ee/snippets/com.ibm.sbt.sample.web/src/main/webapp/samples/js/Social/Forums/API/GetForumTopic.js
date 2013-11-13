require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
    	var topicUuid = "%{name=ForumService.topicUuid}";	
        var promise = forumService.getForumTopic(topicUuid);
        promise.then(
            function(forumTopic) {
               dom.setText("json", json.jsonBeanStringify(forumTopic));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);