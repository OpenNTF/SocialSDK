require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();	
	var community = communityService.getCommunity({		
		loadIt : false
	});	
	community.setTitle("Test Community Title");
	community.setContent("Test Community Title");
	communityService.createCommunity(community, {				
		load : function(community){			
			dom.setText("content","Title of new community is " + community.getTitle());
		},
		error : function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
