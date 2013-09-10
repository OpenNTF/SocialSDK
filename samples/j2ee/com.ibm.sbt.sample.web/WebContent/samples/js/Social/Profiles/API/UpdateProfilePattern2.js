require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var id = "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}";
	var result, promise2;
	var profileService = new ProfileService();
	var profile = profileService.newProfile({
		id : id,		
		jobTitle : "%{name=sample.updateProfileJobTitle}" + "-1",
		floor : "%{name=sample.updateProfileFloor}" + "-1",
		building : "%{name=sample.updateProfileBuilding}" + "-1",
		telephoneNumber : "%{name=sample.updateProfileTelephoneNumber}"
	});	
	promise2 = profile.update();
	promise2.then(
		function (profile){
			profile.load().then(
				function(profile){
					result = toJson(profile, json);
					dom.setText("json",json.jsonBeanStringify(result));
				},function(error){
			    	dom.setText("json", json.jsonBeanStringify(error));
			    }
			);
		 },function(error){
		    	dom.setText("json", json.jsonBeanStringify(error));
		 }
	);	
});	

function toJson(profile, json) {
    return {
        "jobTitle" : profile.getJobTitle(),
        "building" : profile.getBuilding(),
        "floor" : profile.getFloor(),        
        "telephoneNumber": profile.getTelephoneNumber()
    };
}