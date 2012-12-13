require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();	
	var community = communityService.getCommunity({
		id : "%{sample.communityId}",
		loadIt : false
	});	
	community.setTitle("Test create deleteqhh");
	community.setContent("Test create deleteqhh");
	communityService.updateCommunity(community, {				
		load : function(community){			
			dom.setText("content", "Updated Community Title is \"" + community.getTitle() + "\" and description is \"" +  community.getContent() +"\"" );
		},
		error : function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
