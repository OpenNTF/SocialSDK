require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        //var communityUuid = "%{name=sample.communityId|helpSnippetId=Social_Communities_Get_My_Communities}";
        //var id = "%{name=sample.id2|helpSnippetId=Social_Profiles_Get_Profile}";
        var communityUuid = "ba3c4f7d-af60-430c-a4e6-c674bedb908f";
        var id = "E05C606D-7622-281F-4825-7A700025B79C";
        var promise = communityService.declineInvite(communityUuid, id);
        promise.then(
            function(communityUuid) {
            	dom.setText("json", "Invite for community id " + communityUuid + " deleted.");
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
