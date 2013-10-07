require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
        var forumService = new ForumService();  
    	var replyUuid = "%{name=ForumService.replyUuid}";	
        var promise = forumService.getForumReply(replyUuid);
        promise.then(
            function(forumReply) {
               dom.setText("json", json.jsonBeanStringify(forumReply));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);