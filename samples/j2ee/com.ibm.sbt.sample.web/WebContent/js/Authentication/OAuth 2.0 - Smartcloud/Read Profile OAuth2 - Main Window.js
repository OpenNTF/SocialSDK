require([ "sbt/smartcloud/ProfileService", "sbt/dom" , "sbt/config" ], function(ProfileService, dom, config) {
	config.Properties["loginUi"] = "mainWindow";
    dom.byId("loading").style.visibility = "visible";
    var service = new ProfileService({
        endpoint : "smartcloudOA2"
    }); 
    service.getProfile({
		load : 	function(profile)
		{
			dom.byId("loading").style.visibility = "hidden"; 
			dom.setText("content",profile.getDisplayName() + ", " + profile.getEmail() + ", " + profile.getThumbnailUrl());			
		},
		error : function(error)
		{
			dom.byId("loading").style.visibility = "hidden"; 
			dom.setText("content", "Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});
