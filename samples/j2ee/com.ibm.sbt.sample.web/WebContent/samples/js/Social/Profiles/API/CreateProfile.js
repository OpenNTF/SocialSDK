require(["sbt/connections/ProfileAdminService","sbt/dom", "sbt/json"], function(ProfileAdminService,dom, json) {
	var id = "%{name=sample.createProfileId}";
	var profileAdminService = new ProfileAdminService();
	var profile = profileAdminService.newProfile(id);
	profile.setAsString("guid", "%{name=sample.createProfileId}");
    profile.setAsString("email", "%{name=sample.createProfileEmail}");
    profile.setAsString("uid", "%{name=sample.createProfileUid}");
    profile.setAsString("distinguishedName", "%{name=sample.createProfileDistinguishedName}");
    profile.setAsString("displayName", "%{name=sample.createProfileDisplayName}");
    profile.setAsString("givenNames", "%{name=sample.createProfileGivenNames}");
    profile.setAsString("surname", "%{name=sample.createProfileSurName}");
    profile.setAsString("userState", "%{name=sample.createProfileUserState}");
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