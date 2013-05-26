require(["sbt/dom", "sbt/json", "sbt/connections/CommunityService"], function(dom,json,CommunityService) {
    try {
        var communityService = new CommunityService();
        var communityId = "%{sample.communityId}";
        var memberId = "%{sample.id1}";
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