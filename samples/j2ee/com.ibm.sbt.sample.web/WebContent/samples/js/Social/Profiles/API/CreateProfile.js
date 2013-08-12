require(["sbt/connections/ProfileAdminService","sbt/dom", "sbt/json"], function(ProfileAdminService,dom, json) {
	var id = "%{sample.createProfileId}";
	var profileAdminService = new ProfileAdminService();
	var profile = profileAdminService.newProfile(id);
	profile.setAsString("guid", "%{sample.createProfileId}");
    profile.setAsString("email", "%{sample.createProfileEmail}");
    profile.setAsString("uid", "%{sample.createProfileUid}");
    profile.setAsString("distinguishedName", "%{sample.createProfileDistinguishedName}");
    profile.setAsString("displayName", "%{sample.createProfileDisplayName}");
    profile.setAsString("givenNames", "%{sample.createProfileGivenNames}");
    profile.setAsString("surname", "%{sample.createProfileSurName}");
    profile.setAsString("userState", "%{sample.createProfileUserState}");
	var promise = profileAdminService.createProfile(profile);
	promise.then(function(profile){
		profile.load().then(function(profile){
			var result = toJson(profile);
				dom.setText("json",json.jsonBeanStringify(result));
			},function(error){
				dom.setText("json", json.jsonBeanStringify(error));
			}
		);
	},function(error){
		dom.setText("json", json.jsonBeanStringify(error));
	});
});	

function toJson(profile) {
    return {
        "getName" : profile.getName(),
        "getEmail": profile.getEmail(),
        "getUserid": profile.getUserid()
    };
}