require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();	
    	var community = communityService.getCommunity({
    		id : "%{sample.communityId}",
    		loadIt : false
    	});	
    	community.setTitle("%{sample.communityTitle}");
    	community.setContent("%{sample.communityContent}");
    	communityService.updateCommunity(community, {				
            load: function(response){           
                dom.setText("json", json.jsonBeanStringify(response));
            },
            error: function(error){
                dom.setText("json", json.jsonBeanStringify(error));
            }       
    	});
    }
);
