require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
		var topicUuid = "%{name=ForumService.topicUuid|helpSnippetId=Social_Forums_Get_My_Forum_Entries}";
	
    	var forumService = new ForumService();	
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
