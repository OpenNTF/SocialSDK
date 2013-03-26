require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();
    	var communityId = "%{sample.communityId}";
    	var community = communityService.getCommunity({
    		id: communityId,
    		loadIt: false
    	});
    	communityService.getMembers(community, {		
            load: function(members){
                dom.setText("json", json.jsonBeanStringify(members));
            },
            error: function(error){
                dom.setText("json", json.jsonBeanStringify(error));
            }
    	});
    }
);