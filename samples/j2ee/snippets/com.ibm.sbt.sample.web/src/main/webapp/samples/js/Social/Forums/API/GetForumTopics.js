require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getForumTopics("%{name=ForumService.forumUuid}");
        promise.then(
            function(topics) {
                dom.setText("json", json.jsonBeanStringify(topics));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
