require(["sbt/smartcloud/CommunityService","sbt/dom"], function(CommunityService,dom) {
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
			dom.setText("content",displayStr);	
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}		
	});
});