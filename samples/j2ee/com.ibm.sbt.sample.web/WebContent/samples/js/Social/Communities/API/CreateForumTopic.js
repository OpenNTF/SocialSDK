require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var communityId = "%{sample.communityId}";
    var forumUuid = "%{sample.forumId}";
    var now = new Date();
    var forumTopic = {
    	forumUuid : forumUuid,
    	title : "%{name=CommunityService.topicTitle}",
    	content : "%{name=CommunityService.topicContent}"
    };
    var promise = communityService.createForumTopic(communityId, forumTopic);
    promise.then(
        function(forumTopic) {
           dom.setText("json", json.jsonBeanStringify(forumTopic.toJson()));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
