define("page/globals",["sbt/smartcloud/ProfileService"], function(ProfileService) {
		return {
			ProfileService:		new ProfileService({cacheSize:10})
		};
	});
require(["page/globals","sbt/dom"], function(globals,dom) {
		dom.byId("loading").style.visibility = "visible";
			var service = globals.ProfileService;
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