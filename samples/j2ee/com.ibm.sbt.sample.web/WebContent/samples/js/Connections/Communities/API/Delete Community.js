require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();	
    	var community = communityService.getCommunity({	
    		id : "%{sample.communityId2}",
    		loadIt : false
    	});	
    	communityService.deleteCommunity(community, {			
            load: function(community){
                dom.setText("json", json.jsonBeanStringify(community));
            },
            error: function(error){
                dom.setText("json", json.jsonBeanStringify(error));
            }
    	});
    }
);
