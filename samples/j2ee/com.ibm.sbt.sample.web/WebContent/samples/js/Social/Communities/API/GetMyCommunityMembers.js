require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
        var communityService = new CommunityService();
        var results = []; 
        communityService.getMyCommunities().then(
            function(communities) {
            	if (communities.length == 0) {
        			dom.setText("content", "You do not own any communities!");
        		}
        		for(var i=0; i<communities.length; i++) {
        			var communityUuid = communities[i].getCommunityUuid();
                	communityService.getMembers(communityUuid).then(
                        function(members) {
                        	for(var j=0; j<members.length; j++) {
                        		results.push({
                        			communityUuid : members[j].getCommunityUuid(),
                        			memberName : members[j].getName()
                        		});
                        	}
                            dom.setText("content", json.jsonBeanStringify(results));
                        },
                        function(error) {
                            dom.setText("content", json.jsonBeanStringify(error));
                        }
                    );
        		}
            },
            function(error) {
                dom.setText("content", json.jsonBeanStringify(error));
            }
        );
    }
);
