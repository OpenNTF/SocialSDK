require([ "sbt/connections/ForumService", "sbt/connections/ForumConstants", "sbt/dom", "sbt/json" ], function(ForumService,consts,dom,json) {
    try {
        var forumService = new ForumService();
        var forumReply = forumService.newForumReply();
        forumReply.setReplyUuid("%{name=ForumService.replyUuid}").setTitle("%{name=ForumService.replyTitle}").setContent("%{name=ForumService.replyContent}");
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
