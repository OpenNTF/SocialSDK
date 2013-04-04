require(["sbt/connections/CommunityService",'sbt/Endpoint',"sbt/dom","sbt/config"], function(CommunityService,Endpoint,dom,config) {
	config.Properties["loginUi"] = "popup";
	var ep = Endpoint.find("connections");
	ep.authenticate({
		forceAuthentication: true,
		success: function(){
			var communityService = new CommunityService();
			var displayStr = "";
			communityService.getMyCommunities({
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
		},
		cancel: function(){
			dom.setText("content","You need to Login to see Communties List");
		}
	});
});