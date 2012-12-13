require(["sbt/connections/CommunityService","sbt/dom","sbt/config"], function(CommunityService,dom,config) {
	config.Properties["loginUi"] = "mainWindow";
	var communityService = new CommunityService();
	var displayStr = "";
	communityService.getMyCommunities({
		parameters:{
			ps:5
		},
		load: function(communities){
			while(communities.length > 0){
				var community = communities.shift();
				displayStr += community.getTitle() + ((communities.length > 0) ?" , ":"");
			}
			if (displayStr.length == 0) {
                displayStr = "You are not a member of any communities.";
            }
			dom.setText("content",displayStr);	
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}		
	});
});