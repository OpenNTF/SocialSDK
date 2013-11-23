require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
    var communityService = new CommunityService();
    var communityUuid = null;
    communityService.getPublicCommunities().then(
        function(communities) {
        	return communities[0].getCommunityUuid();
        }
    ).then(
    	function(communityUuid) {
    		return communityService.getCommunity(communityUuid);
        }
    ).then(
        function(community) {
        	return community.getMembers();
        }
    ).then(
        function(members) {
        	dom.setText("json", json.jsonBeanStringify(members));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
