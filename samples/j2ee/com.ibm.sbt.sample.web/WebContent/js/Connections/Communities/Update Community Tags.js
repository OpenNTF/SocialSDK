require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();	
	communityService.getCommunity({
		id : "%{sample.communityId}",
		loadIt : true,
		load : function (community){
			community.setAddedTags(["new"]);
			//community.setDeletedTags(["testing"]);
			communityService.updateCommunity(community, {						
				load : function(community){
					var displayStr = "";
					var tags = community.getTags();					
					while(tags.length > 0){
						var tag = tags.shift();			
						displayStr += tag + ((tags.length > 0) ?",":"");
					}
					dom.setText("content",displayStr);	
				},
				error : function(error){
					dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
				}			
			});
		}
	});
});
