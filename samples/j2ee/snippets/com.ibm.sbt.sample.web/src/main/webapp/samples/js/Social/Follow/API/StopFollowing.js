require(["sbt/connections/FollowService", "sbt/connections/CommunityService", "sbt/connections/FollowConstants", "sbt/dom", "sbt/json"], 
    function(FollowService, CommunityService, consts, dom, json) {
        var followService = new FollowService();
        var communityService = new CommunityService();  
        var community = communityService.newCommunity(); 
        var now = new Date();
        community.setTitle("StopCommunity.js " + now.getTime());
        community.setContent("Test community created: " + now);
        
        // Creating a community, owner automatically follows this
        communityService.createCommunity(community).then(
    		function(community) {
    			dom.setText("json", json.jsonBeanStringify(community));
    			return community;
    		}
    	).then(
    		function(community) {
    			// Getting followed Community resources
    			return followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType);
    		}
    	).then(  
    		function(followedResources) {
    			var followedResource = followedResources[0];
    			dom.setText("json", json.jsonBeanStringify(followedResource));
    			return followedResource;
    		}
    	).then(
    		function(followedResource) {
    			// Stop following the first community resource
    			return followService.stopFollowing(followedResource);
    		}
    	).then( 
    		function(resourceId) {
    			dom.setText("json", json.jsonBeanStringify({ stoppedFollowingResource : resourceId }));
    			
    			// Cleanup the community we just created
    			communityService.deleteCommunity(community.getCommunityUuid());
    		},
    		function(error) {
    		    dom.setText("json", json.jsonBeanStringify(error));
    		}
    	);
	}

);
