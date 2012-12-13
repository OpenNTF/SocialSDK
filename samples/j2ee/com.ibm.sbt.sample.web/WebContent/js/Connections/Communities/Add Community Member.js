require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var communityId = "%{sample.communityId}";
	var userId = "%{sample.id2}";
	var community = communityService.getCommunity({
		id : communityId,
		loadIt : false
	});
	var member = communityService.getMember({
		id : userId,
		loadIt : false
	});
	member.setRole("member");
	communityService.addMember(community, member, {
		load : function(member){
			dom.setText("content", member.getName());
		},
		error : function(error){
			dom.setText('content',"Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
