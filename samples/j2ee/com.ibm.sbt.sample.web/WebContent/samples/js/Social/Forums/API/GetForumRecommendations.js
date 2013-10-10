require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getForumRecommendations("%{name=ForumService.postUuid|helpSnippetId=Social_Forums_Get_My_Topics}");
        promise.then(
            function(recomendations) {
                dom.setText("json", json.jsonBeanStringify(recomendations));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
