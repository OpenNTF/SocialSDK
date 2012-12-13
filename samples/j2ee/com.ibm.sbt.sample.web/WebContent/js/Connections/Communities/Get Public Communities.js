require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var displayStr = "";
	communityService.getPublicCommunities({
		parameters:{
			ps:5
		},
		load: function(communities){
			for(var count = 0; count < communities.length; count ++){
				var community = communities[count];
				displayStr += community.getTitle() + ((count == communities.length -1) ?"  ":" , ");
			}
			dom.setText("content",displayStr);	
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}		
	});
});