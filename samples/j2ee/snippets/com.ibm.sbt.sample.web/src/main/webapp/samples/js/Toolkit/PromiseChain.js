require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
	console.log('A------------------------');
    var communityService = new CommunityService();
    var communityUuid = null;
	console.log('B------------------------');

    communityService.getPublicCommunities().then(
        function(communities) {
        	console.log('C------------------------');

        	return communities[0].getCommunityUuid();
        }
    ).then(
    	function(communityUuid) {
    		console.log('D------------------------');

    		return communityService.getCommunity(communityUuid);
        }
    ).then(
        function(community) {
        	console.log('E------------------------');

        	return community.getMembers();
        }
    ).then(
        function(members) {
        	console.log('F------------------------');

        	dom.setText("json", json.jsonBeanStringify(members));
        },
        function(error) {
        	console.log('G------------------------' + error);
        	console.log('G------------------------' + Object.keys(error));
        	if(error.message) {
            	console.log('G------------------------' + error.message);
            	console.log('G------------------------' + error.fileName);
            	console.log('G------------------------' + error.lineNumber);

        	}
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
