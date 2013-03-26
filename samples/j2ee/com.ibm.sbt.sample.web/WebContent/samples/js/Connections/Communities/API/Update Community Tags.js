require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
    	var communityService = new CommunityService();	
    	communityService.getCommunity({
    		id: "%{sample.communityId}",
    		loadIt: true,
    		load: function (community){
    			community.setTags(["newTag1", "newTag2", "newTag3"]);
    			communityService.updateCommunity(community, {						
    	            load: function(response){           
    	                dom.setText("json", json.jsonBeanStringify(response));
    	            },
    	            error: function(error){
    	                dom.setText("json", json.jsonBeanStringify(error));
    	            }       
    			});
    		}
    	});
    }
);
