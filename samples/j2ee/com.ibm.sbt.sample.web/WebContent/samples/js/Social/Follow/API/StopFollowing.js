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
				followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, { ps:1 }).then(  // getting followed Community resources
					function(followedResources) {
						followService.stopFollowing(followedResources[0]).then( // stop following the first community resource
							function(followedProfileResource) {
				                dom.setText("json", json.jsonBeanStringify({ stoppedFollowingResource : followedProfileResource }));
				            },
				            function(error) {
				                dom.setText("json", json.jsonBeanStringify(error));
				            }
				         );
					}
	    		);
			}
    	)
	}

);
