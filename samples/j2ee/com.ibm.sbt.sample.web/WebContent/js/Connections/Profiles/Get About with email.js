require([ 'sbt/connections/ProfileService', 'sbt/dom' ], function(ProfileService,dom) {
    var email = "%{sample.email1}";
    var profileService = new ProfileService();
    profileService.getProfile({
        email : email,
        load : function(profile) {
            if (profile.getAbout()) {
                dom.setText("content", profile.getAbout());
            } else {
                dom.setText("content", "No Result");
            }
        },
        error : function(error) {
            dom.setText('content', "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
        }
    });
});
