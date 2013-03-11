require([ 'sbt/connections/ProfileService', 'sbt/dom' ], function(ProfileService,dom) {
	var displayStr = "";
    var id = "%{sample.id1}";
    var profileService = new ProfileService();
    var profile = profileService.getProfile({
		  id	: id,
		loadIt	: false
	});
    profileService.getColleagues(profile, {
        load : function(profiles,ioArgs) {        	
        	for(var count = 0; count < profiles.length; count ++){
				var profile = profiles[count];
				displayStr += profile.getDisplayName() + ((count == profiles.length -1) ?"  ":" , ");
			}
			dom.setText("content",displayStr);		
        },
        error : function(error) {
            dom.setText('content', "Error received. Error Code = " + error.code + ". Error Message = " + error.message);
        }
    });
});
