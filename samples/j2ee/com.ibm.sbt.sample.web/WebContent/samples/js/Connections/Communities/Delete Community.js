require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();	
	var community = communityService.getCommunity({	
		id : "%{sample.communityId2}",
		loadIt : false
	});	
	communityService.deleteCommunity(community, {			
		load : function(){
			dom.setText("content","Community deleted");
		},
		error : function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
