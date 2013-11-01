require([  "sbt/connections/FollowService", "sbt/connections/CommunityService", "sbt/connections/FollowConstants", "sbt/dom"],
	function(FollowService, CommunityService, consts, dom) {
		var communityService = new CommunityService();
	    var promise = communityService.getPublicCommunities({ps : 1}); // getting 1 public community
	    promise.then(
	        function(communities) {
	        	if (communities.length == 0) { // if there is no public community found, create one.
	        		var community = communityService.newCommunity(); 
	                var now = new Date();
	                community.setTitle("CreateCommunity.js " + now.getTime());
	                community.setContent("Test community created: " + now);
	                communityService.createCommunity(community).then(
		        		function(community) {
		                    return community;
		                }
	                );
	            } else { //  if there is a public community found
	                return communities[0];
	            }
	        }
		).then(
			function(community) {  // we have a public community
				var followService = new FollowService();
				//returning followed resource by passing resource param to getFollowedResources()
				//this will return one entry if community is followed, otherwise it will return 0 entries
				followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, {resource : community.getCommunityUuid()}).then(
					function(followedResources) {	
						if (followedResources.length == 0) { // if community is not followed
							var followedResource = followService.newFollowedResource();
					        followedResource.setSource(consts.CommunitiesSource);
					        followedResource.setResourceType(consts.CommunitiesResourceType);
					        followedResource.setResourceId(community.getCommunityUuid());
					        followService.startFollowing(followedResource).then( // start following community resource
				        		function(followedCommunityResource) {
				        			dom.setText("followMessage", "You started following resource with id: "+followedCommunityResource.getResourceId());
				        		}
					        );    
						} else{ // if community is followed
							var followedResource = followedResources[0];
							followService.stopFollowing(followedResource).then( // stop following community resource
								function(followedResourceId) {
				        			dom.setText("followMessage", "You stopped following resource with id: "+followedResourceId);
				        		}
							);
						}
					}
			    );
	//			dom.byId("resourceId").value = community.getCommunityUuid();
			} 
		);
    }
);