require(["sbt/connections/FollowService", "sbt/connections/FollowConstants", "sbt/dom", "sbt/json"], 
    function(FollowService, consts, dom, json) {
        var followService = new FollowService();  
        var followedResource = followService.newFollowedResource();
        followedResource.setSource(consts.CommunitiesSource);
        followedResource.setResourceType(consts.CommunitiesResourceType);
        followedResource.setResourceId("%{name=communityUuid|required=false|helpSnippetId=Social_Communities_Get_My_Communities}");
        followService.getFollowedResource(followedResource).then(
       		function(followedResource) {
                dom.setText("json", json.jsonBeanStringify(followedResource));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
       ); 
	}
);