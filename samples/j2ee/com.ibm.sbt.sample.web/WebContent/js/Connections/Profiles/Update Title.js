require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	var profile = profileService.getProfile({
		  id	: id,
		loadIt	: false
	});
	profile.setTitle("new title1");			
	profileService.updateProfile(profile, {
    	load: function(profile){
    		dom.setText("content",profile.getTitle());
    	},
    	error: function(error){
    		dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
    	}
    });
	
});		