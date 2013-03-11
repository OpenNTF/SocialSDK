require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	var communityId = "%{sample.communityId}";
	var email = "%{sample.email1}";
	var community = communityService.getCommunity({
		id : communityId,
		loadIt : false
	});
	communityService.getMember({
		community: community,
		email: email,
		load: function(member){
			if (member.getName()){
				dom.setText("content", member.getName());
			} else {
				dom.setText("content", "Error received. No Member.");
			}			
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
