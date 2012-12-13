require([ 'sbt/connections/ProfileService', 'sbt/dom' ], function(ProfileService,dom) {
    var userId = "%{sample.userId1}";
    var profileService = new ProfileService();
    profileService.getProfile({
        userId : userId,
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
