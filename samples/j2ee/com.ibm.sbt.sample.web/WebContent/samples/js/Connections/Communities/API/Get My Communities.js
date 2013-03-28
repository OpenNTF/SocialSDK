require([ "sbt/connections/CommunityService", "sbt/dom", "sbt/json" ], function(CommunityService,dom,json) {
    var communityService = new CommunityService();
    communityService.getMyCommunities({
        parameters : {
            ps : 5
        },
        load : function(communities) {
            dom.setText("json", json.jsonBeanStringify(communities));
        },
        error : function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    });
});