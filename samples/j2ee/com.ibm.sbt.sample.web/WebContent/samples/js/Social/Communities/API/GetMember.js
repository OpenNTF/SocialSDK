require(["sbt/dom", "sbt/json", "sbt/connections/CommunityService"], function(dom,json,CommunityService) {
    try {
        var communityService = new CommunityService();
        var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var memberId = "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}";
        var community = communityService.newCommunity(communityId);        
        var results = [];        
        var promise = community.getMember(memberId);
        promise.then(
            function(member) {
                results.push(member);
                results.push({ entityId : member.dataHandler.getEntityId() });
                dom.setText("json", json.jsonBeanStringify(results));
            }, function(error) {
                results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});