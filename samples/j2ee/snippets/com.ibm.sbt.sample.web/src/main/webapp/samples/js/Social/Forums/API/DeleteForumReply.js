require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
		var replyUuid = "%{name=ForumService.replyUuid|helpSnippetId=Social_Forums_Get_My_Forum_Entries}";
	
    	var forumService = new ForumService();	
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
