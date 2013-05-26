require(["sbt/dom", "sbt/json", "sbt/Endpoint", "sbt/connections/CommunityService"], function(dom,json,Endpoint,CommunityService) {
    try {
        var endpoint = Endpoint.find("connections");
        endpoint.authenticate();
        
        var communityService = new CommunityService();
        var communityId = "%{sample.communityId}";

        var results = [];
        
        var promise = communityService.getCommunity(communityId);
        promise.then(
            function(community) {
                results.push(community);
                dom.setText("json", json.jsonBeanStringify(results));
                
                // now save
                community.setContent("Test CommunityGetSave");
                var promise = community.save();
                promise.then(
                    function(community) {
                        results.push(community);
                        dom.setText("json", json.jsonBeanStringify(results));
                        community.load().then(
                            function(community) {
                                results.push(community);
                                dom.setText("json", json.jsonBeanStringify(results));
                            },
                            function(error) {
                                results.push(error);
                                dom.setText("json", json.jsonBeanStringify(results));
                            }
                        );
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