require(["sbt/connections/FollowService", "sbt/connections/CommunityService", "sbt/connections/FollowConstants", "sbt/dom", "sbt/json"], 
    function(FollowService, CommunityService, consts, dom, json) {
        var followService = new FollowService();
        var communityService = new CommunityService();  
        var community = communityService.newCommunity(); 
        var now = new Date();
        community.setTitle("CreateCommunity.js " + now.getTime());
        community.setContent("Test community created: " + now);
        communityService.createCommunity(community).then(  // creating a community, so that getFollowedResources() returns atleast 1 result
    		function() {
				return followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, { ps:1 }); // getting followed Community resources
    		}
    	).then(
			function(followedResources) {
				return followService.startFollowing(followedResources[0]); // start following the first community resource
			}	
    	).then(
			function(followedResource) {
				return followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, { resource: followedResource.getResourceId()});
			}
		).then(
			function(resources) {
				if(resources.length == 0){
					dom.setText("json", json.jsonBeanStringify({ startFollowMessage : "unsuccessful" }));
				}else{
					dom.setText("json", json.jsonBeanStringify({ startFollowMessage : "successful" }));
				}
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
		);
					
	}

);
