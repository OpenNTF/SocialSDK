require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        var communityUuid = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var userid = "%{name=sample.id2|helpSnippetId=Social_Profiles_Get_Profile}"; 
        var invite = communityService.newInvite();
        invite.setCommunityUuid(communityUuid);
        invite.setUserid(userid);
        var promise = communityService.createInvite(invite);
        promise.then(
            function(invite) {
            	dom.setText("json", json.jsonBeanStringify(invite));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
