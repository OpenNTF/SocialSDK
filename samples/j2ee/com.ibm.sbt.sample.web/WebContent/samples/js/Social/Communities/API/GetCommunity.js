require(["sbt/dom", "sbt/json", "sbt/connections/CommunityService"], function(dom,json,CommunityService) {
    try {
        var communityService = new CommunityService();
        var communityId = "%{sample.communityId}";

        var results = [];
        
        var promise = communityService.getCommunity(communityId);
        promise.then(
            function(community) {
                results.push(community);
                results.push({ entityId : community.dataHandler.getEntityId() });
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