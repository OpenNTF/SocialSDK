require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var id1 = "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}";	
	var result;
	var profileService = new ProfileService();
	var profile = profileService.newProfile(id1);				
	profile.setJobTitle("%{name=sample.updateProfileJobTitle}");
	profile.setBuilding("%{name=sample.updateProfileBuilding}");
	profile.setFloor("%{name=sample.updateProfileFloor}");
	profile.setTelephoneNumber("%{name=sample.updateProfileTelephoneNumber}");
	profileService.updateProfile(profile).then(
		function (profile){
			profile.load().then(						
				function(profile){
					result = toJson(profile, json);
					dom.setText("json",json.jsonBeanStringify(result));												
		    	},function(error){
		    		dom.setText("json", json.jsonBeanStringify(error));
		    	}
			);
		}, function(error){
    		dom.setText("json", json.jsonBeanStringify(error));
    	}		
		
	);
		
});
function toJson(profile, json) {
    return {
    	"userid" : profile.getUserid(),
        "jobTitle" : profile.getJobTitle(),
        "building" : profile.getBuilding(),
        "floor" : profile.getFloor(),        
        "telephoneNumber": profile.getTelephoneNumber()
    };
}