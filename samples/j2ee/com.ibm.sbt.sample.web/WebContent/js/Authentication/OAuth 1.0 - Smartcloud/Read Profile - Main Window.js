require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService, dom, config) {
	config.Properties["loginUi"] = "mainWindow";
	dom.byId("loading").style.visibility = "visible"; 
    var service = new ProfileService(); 
    service.getProfile({
		load : 	function(profile)
		{
			var content = "";
			dom.byId("loading").style.visibility = "hidden"; 
			if (profile.getDisplayName()) {
    			content += profile.getDisplayName();
    		}
    		if(profile.getEmail()) {
    			content +=  " , " + profile.getEmail();
    		}
    		if(profile.getThumbnailUrl()) {
    			content += " , " + profile.getThumbnailUrl();
    		}
    		if(content) {
    			dom.setText("content", content);
    		}
    		else {
        		dom.setText("content", "No Result");
   			}
		},
		error : function(error)
		{
			dom.byId("loading").style.visibility = "hidden"; 
			dom.setText("content", "Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});