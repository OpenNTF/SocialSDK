require(["sbt/connections/ForumService", "sbt/dom", "sbt/json"], 
    function(ForumService, dom, json) {
    	var topicUuid = "%{name=ForumService.topicUuid|helpSnippetId=Social_Forums_Get_My_Forum_Entries}";
        var replyTitle = "%{name=ForumService.replyTitle|required=false}" || "Test forum reply " + (new Date()).getTime();
        var replyContent = "%{name=ForumService.replyContent|required=false}" || "Test forum reply content " + (new Date()).getTime();

    	var forumService = new ForumService();  
        var forumReply = forumService.newForumReply(); 
        forumReply.setTopicUuid(topicUuid);
        forumReply.setTitle(replyTitle);
        forumReply.setContent(replyContent);
        var promise = forumService.createForumReply(forumReply);
        promise.then(
            function(forumReply) {
               dom.setText("json", json.jsonBeanStringify(forumReply.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);