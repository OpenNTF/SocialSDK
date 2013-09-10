require([ "sbt/connections/ForumService", "sbt/connections/ForumConstants", "sbt/dom", "sbt/json" ], function(ForumService,consts,dom,json) {
    try {
        var forumService = new ForumService();
        var forumTopic = forumService.newForumTopic();
        forumTopic.setTopicUuid("%{name=ForumService.topicUuid}").setTitle("%{name=ForumService.topicTitle}").setContent("%{name=ForumService.topicContent}");
        var promise = forumService.updateForumTopic(forumTopic);
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
