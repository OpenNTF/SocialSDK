require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService,dom,json) {
    	var communityService = new CommunityService();	
    	var promise = communityService.getCommunity("%{sample.communityId}");
    	promise.then(
    		function (community) {
    			community.setTags(["newTag1", "newTag2", "newTag3"]);
    			communityService.updateCommunity(community).then(						
    	            function(response) {           
    	                dom.setText("json", json.jsonBeanStringify(response));
    	            },
    	            function(error) {
    	                dom.setText("json", json.jsonBeanStringify(error));
    	            }       
    			);
    		}
    	);
    }
);
