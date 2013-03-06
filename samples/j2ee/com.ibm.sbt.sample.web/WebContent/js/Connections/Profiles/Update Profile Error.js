require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService,dom,config) {
    var id = "123";
    var profileService = new ProfileService();
    var profile = profileService.getProfile({
        id : id,
        loadIt : false
    });
    profile.setTitle("New Title");
    profileService.updateProfile(profile, {
        load : function(profile) {
            dom.setText("content", profile.getTitle());
        },
        error : function(error) {
        	dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
        }
    });
});