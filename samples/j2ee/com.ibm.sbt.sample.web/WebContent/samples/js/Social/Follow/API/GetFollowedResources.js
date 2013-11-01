require(["sbt/connections/FollowService", "sbt/connections/FollowConstants", "sbt/dom", "sbt/json"], 
    function(FollowService, consts, dom, json) {
        var followService = new FollowService();  
        followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, { ps: 5 }).then( //getting 5 community resources
       		function(followedCommunityResources) {
                dom.setText("json", json.jsonBeanStringify(followedCommunityResources));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
       ); 
	}
);