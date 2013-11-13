require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
    	var forumService = new ForumService();	
    	var replyUuid = "%{name=ForumService.replyUuid}";	
        var promise = forumService.deleteForumReply(replyUuid);
        promise.then(
            function(replyUuid) {
                dom.setText("json", json.jsonBeanStringify({ replyUuid : replyUuid }));
            }, function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
