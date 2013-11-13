require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,ForumService) {
        var communityService = new CommunityService();
        var promise = communityService.getForumTopics("%{name=sample.communityId|helpSnippetId=Social_Communities_Get_My_Communities}");
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
