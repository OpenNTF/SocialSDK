require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        var communityUuid = "%{name=sample.communityId|helpSnippetId=Social_Communities_Get_My_Communities}";
        var id = "%{name=sample.id2|helpSnippetId=Social_Profiles_Get_Profile}";        
        var promise = communityService.createInvite(communityUuid, id);
        promise.then(
            function(invite) {
            	dom.setText("json", "Invite Id for the new invite is " + invite.getId());
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
