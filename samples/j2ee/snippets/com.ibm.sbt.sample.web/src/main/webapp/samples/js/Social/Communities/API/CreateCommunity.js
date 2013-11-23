require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
        var communityService = new CommunityService();  
        var community = communityService.newCommunity(); 
        var now = new Date();
        community.setTitle("%{name=CommunityService.title}");
        community.setContent("%{name=CommunityService.content}");
        var promise = communityService.createCommunity(community);
        promise.then(
            function(community) {
                // community must be loaded if access to
                // the full data is required
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