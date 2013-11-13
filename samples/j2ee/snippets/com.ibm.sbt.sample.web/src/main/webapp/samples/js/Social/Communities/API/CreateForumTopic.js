require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var communityId = "%{sample.communityId}";
    var forumUuid = "%{sample.forumId}";
    forumTopic.setForumUuid(forumUuid);
    var now = new Date();
    var forumTopic = {
    	forumUuid : forumUuid,
    	title : "Test forum topic " + now.getTime(),
    	content : "Test forum topic created: " + now
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
