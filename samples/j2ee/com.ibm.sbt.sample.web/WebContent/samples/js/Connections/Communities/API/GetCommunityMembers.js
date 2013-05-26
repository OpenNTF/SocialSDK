require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], 
    function(dom,json,CommunityService) {

        var communityService = new CommunityService();
        var promise = communityService.getMembers("%{sample.communityId}");
        promise.then(
            function(communities) {
                dom.setText("json", json.jsonBeanStringify(communities));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
        
    }
);
