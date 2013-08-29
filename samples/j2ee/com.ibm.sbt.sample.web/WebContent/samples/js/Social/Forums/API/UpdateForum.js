require([ "sbt/connections/ForumService", "sbt/connections/ForumConstants", "sbt/dom", "sbt/json" ], function(ForumService,consts,dom,json) {
    try {
        var forumService = new ForumService();
        var forum = forumService.newForum();
        forum.setForumUuid("%{ForumService.forumUuid}").setTitle("%{ForumService.forumTitle}").setContent("%{ForumService.forumContent}");
        var promise = forumService.updateForum(forum);
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
