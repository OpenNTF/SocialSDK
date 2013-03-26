require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
	var communityService = new CommunityService();
	var communityId = "%{sample.communityId}";
	var email = "%{sample.email2}";
	var community = communityService.getCommunity({
		id : communityId,
		loadIt : false
	});
	var member = communityService.getMember({
		email : email,
		loadIt : false
	});
	member.setRole("member");
	communityService.addMember(community, member, {
		load : function(member){
			dom.setText("json", json.jsonBeanStringify(member));
		},
		error : function(error){
			dom.setText("json", json.jsonBeanStringify(error));
		}
	});
});
