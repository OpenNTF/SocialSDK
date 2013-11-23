require(["sbt/dom", "sbt/json", "sbt/config", "sbt/connections/CommunityService"], function(dom,json,config,CommunityService) {
    try {
        var endpoint = config.findEndpoint("connections");
        endpoint.authenticate();
        
        var communityService = new CommunityService();
        var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";

        var results = [];
        
        var promise = communityService.getCommunity(communityId);
        promise.then(
            function(community) {
                results.push({ "Community Title" : community.getTitle() });
                dom.setText("json", json.jsonBeanStringify(results));
                
                // now get associated forums
                var promise = community.getForums();
                promise.then(
                    function(forums) {
                        results.push(forums);
                        dom.setText("json", json.jsonBeanStringify(results));
                    },
                    function(error) {
                        results.push(error);
                        dom.setText("json", json.jsonBeanStringify(results));
                    }
                );                
            }, function(error) {
                results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        );
    } catch (err) {
        results.push(err);
        dom.setText("json", json.jsonBeanStringify(results));
    }
});