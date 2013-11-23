require([ "sbt/connections/ForumService", "sbt/connections/ForumConstants", "sbt/dom", "sbt/json" ], function(ForumService,consts,dom,json) {
    try {
        var replyUuid = "%{name=ForumService.replyUuid|helpSnippetId=Social_Forums_Get_My_Forum_Entries}";
        var replyTitle = "%{name=ForumService.replyTitle|required=false}" || "Updated forum reply " + (new Date()).getTime();
        var replyContent = "%{name=ForumService.replyContent|required=false}" || "Updated forum reply content " + (new Date()).getTime();
    	
        var forumService = new ForumService();
        var forumReply = forumService.newForumReply();
        forumReply.setReplyUuid(replyUuid).setTitle(replyTitle).setContent(replyContent);
        var promise = forumService.updateForumReply(forumReply);
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
