require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
    var promise = communityService.getForumTopics(communityId, {    
            since : "2009-01-04T20:32:31.171Z"
        });
    promise.then(
        function(topics) {
            dom.setText("json", json.jsonBeanStringify(topics));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
