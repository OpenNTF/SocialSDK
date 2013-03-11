require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var communityId = "%{sample.communityId}";
	var displayStr = "";
	var community = communityService.getCommunity({
		id		: communityId,
		loadIt	: false
	});
	communityService.getMembers(community, {		
		load: function(members){
			for(var count = 0; count < members.length; count ++){
				var member = members[count];
				displayStr += member.getName() + ((count == members.length -1) ?"  ":" , ");
			}
			dom.setText("content",displayStr);		
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});