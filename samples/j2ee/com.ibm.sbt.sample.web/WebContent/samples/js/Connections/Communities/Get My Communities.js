require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
    	var communityService = new CommunityService();
    	communityService.getMyCommunities({
    		parameters: { ps: 5 },
    		load: function(communities){
    		    var text = "";
    		    for(var i=0; i<communities.length; i++){
    				var community = communities[i];
    				text += community.getTitle() + ((i==communities.length-1) ? "" : ", ");
    			}
                if (!text.length) {
                    text = "You are not a member of any communities.";
                }
    			dom.setText("content", text);	
    		},
    		error: function(error){
    			dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
    		}		
    	});
    }
);