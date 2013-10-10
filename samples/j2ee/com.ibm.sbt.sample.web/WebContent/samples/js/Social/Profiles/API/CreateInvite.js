require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var id = "%{name=sample.inviteProfileId}";
	var profileService = new ProfileService();
	var invite = profileService.newInvite();
	invite.setContent("Please join my network. Sent using Social Business Toolkit SDK.");
	profileService.createInvite(id, invite).then(
		function(invite) {
			dom.setText("json", json.jsonBeanStringify(invite));
		},
		function(error){
			dom.setText("json", json.jsonBeanStringify(error));
		}
	);
});	
