require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        var communityUuid = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var id = "%{name=sample.id2|helpSnippetId=Social_Profiles_Get_Profile}";
        var promise = communityService.declineInvite(communityUuid, id);
        promise.then(
            function(communityUuid) {
            	dom.setText("json", "Invite for community id " + communityUuid + " declined.");
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
