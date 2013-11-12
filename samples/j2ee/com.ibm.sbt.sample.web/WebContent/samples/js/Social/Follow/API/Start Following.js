require(["sbt/connections/FollowService", "sbt/connections/FollowConstants", "sbt/dom", "sbt/json"],
    function(FollowService, consts, dom, json) {
    	var profileUuid = "%{sample.id2}";
        var followService = new FollowService();
        var followedResource = followService.newFollowedResource();
        followedResource.setSource(consts.ProfilesSource);
        followedResource.setResourceType(consts.ProfilesResourceType);
        followedResource.setResourceId(profileUuid);
        followService.startFollowing(followedResource).then(
			function(followedProfileResource) {
                dom.setText("json", json.jsonBeanStringify(followedProfileResource.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	
	}

);
