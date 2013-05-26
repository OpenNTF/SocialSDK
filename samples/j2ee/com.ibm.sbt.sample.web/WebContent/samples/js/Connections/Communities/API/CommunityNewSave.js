require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
        var communityService = new CommunityService();  
        var community = communityService.newCommunity(); 
        var now = new Date();
        community.setTitle("Test Community " + now.getTime());
        community.setContent("Test community created: " + now);
        var promise = community.save();
        promise.then(
            function(community) {
                community.load().then(
                    function(community) {
                        dom.setText("json", json.jsonBeanStringify(community));
                    },
                    function(error) {
                        dom.setText("json", json.jsonBeanStringify(error));
                    }
                );
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);