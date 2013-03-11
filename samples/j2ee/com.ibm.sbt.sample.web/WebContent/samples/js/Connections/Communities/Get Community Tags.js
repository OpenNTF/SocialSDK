require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var displayStr = "";
	var communityId = "%{sample.communityId}";
	communityService.getCommunity({
		id: communityId,
		loadIt: true,
		load: function(community){
			var tags = community.getTags();
			if(tags.length > 0){				
				for(var count = 0; count < tags.length; count ++){
					displayStr +=  tags[count] + ((count == tags.length -1) ?"  ":" , ");
				}
				dom.setText("content",displayStr);	
			}
			else{
				dom.setText("content","No result");	
			}
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});