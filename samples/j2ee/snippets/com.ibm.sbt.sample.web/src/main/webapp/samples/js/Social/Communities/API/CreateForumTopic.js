require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var forumUuid = "%{name=CommunityService.forumUuid|helpSnippetId=Social_Communities_API_GetForums}";
    var forumTopic = {
    	forumUuid : forumUuid,
    	title : "%{name=CommunityService.topicTitle}",
    	content : "%{name=CommunityService.topicContent}"
    };
    var promise = communityService.createForumTopic(forumTopic);
    promise.then(
        function(forumTopic) {
           dom.setText("json", json.jsonBeanStringify(forumTopic.toJson()));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
