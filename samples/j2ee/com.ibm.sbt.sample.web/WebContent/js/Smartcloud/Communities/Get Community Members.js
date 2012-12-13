require(["sbt/smartcloud/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var communityId = "%{sample.smartcloud.communityId1}";
	var displayStr = "";
	var community = communityService.getCommunity({
		id		: communityId,
		loadIt	: false
	});
	communityService.getMembers(community, {		
		load: function(members){
			while(members.length > 0){
				var member = members.shift();
				displayStr += member.getName() + ((members.length > 0) ?",":"");
			}
			dom.setText("content",displayStr);		
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});