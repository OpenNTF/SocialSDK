require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        var communityUuid = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var inviteeUuid = "%{name=CommunityService.inviteeUuid|helpSnippetId=Social_Communities_Get_My_Invites}"; 
        var invite = communityService.newInvite();
        invite.setCommunityUuid(communityUuid);
        invite.setInviteeUuid(inviteeUuid);
        var promise = communityService.removeInvite(invite);
        promise.then(
            function(inviteUuid) {
            	dom.setText("json", json.jsonBeanStringify({ "inviteUuid" : inviteUuid }));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
