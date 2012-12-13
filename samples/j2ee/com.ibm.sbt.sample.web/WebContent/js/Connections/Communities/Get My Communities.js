require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	communityService.getMyCommunities({
		parameters:{
			ps:5
		},
		load: function(communities){
		    var displayStr = "";
		    for(var count = 0; count < communities.length; count ++){
				var community = communities[count];
				displayStr += community.getTitle() + ((count == communities.length -1) ?"  ":" , ");
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