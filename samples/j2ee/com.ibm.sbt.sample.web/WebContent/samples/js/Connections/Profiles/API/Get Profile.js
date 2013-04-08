require(["sbt/connections/ProfileService","sbt/dom","sbt/json"], function(ProfileService,dom,json) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
        load: function(profile){
            dom.setText("json", json.jsonBeanStringify(profile));
        },
        error: function(error){
            dom.setText("json", json.jsonBeanStringify(error));
        }
	});
}); 
