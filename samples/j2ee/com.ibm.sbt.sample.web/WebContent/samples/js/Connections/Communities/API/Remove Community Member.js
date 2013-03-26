require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();
    	var communityId = "%{sample.communityId}";
    	var userId = "%{sample.id2}";
    	var community = communityService.getCommunity({
    		id: communityId,
    		loadIt: false
    	});
    	var member = communityService.getMember({
    		id: userId,
    		loadIt: false
    	});
    	communityService.removeMember(community, member, {
            load: function(response){           
                dom.setText("json", json.jsonBeanStringify(response));
            },
            error: function(error){
                dom.setText("json", json.jsonBeanStringify(error));
            }       
    	});
    }
);
