require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
        var communityService = new CommunityService();
        var promise = communityService.getMyCommunities({
                asc : true,
                page : 1,
                ps : 2
                //since : "2009-01-04T20:32:31.171Z",
                //sortField  : "lastmod" | "name" | "count",
                // search : "",
                // tag : "tag1",
                // userid : "%{sample.userid1}"
            });
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
