require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var id1 = "%{sample.id1}";	
	var result;
	var profileService = new ProfileService();
	var profile = profileService.newProfile(id1);				
	profile.setJobTitle("%{sample.updateProfileJobTitle}");
	profile.setBuilding("%{sample.updateProfileBuilding}");
	profile.setFloor("%{sample.updateProfileFloor}");
	profile.setTelephoneNumber("%{sample.updateProfileTelephoneNumber}");
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