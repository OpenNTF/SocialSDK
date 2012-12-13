require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		load: function(profile){
			var address = profile.getAddress();			
			var displayStr = "";
			for (key in address) {
				displayStr = displayStr + " " + key + " : " + address[key];					
 			}
			dom.setText("content",displayStr);			
			if(!displayStr){
				dom.setText("content","No Result");
			}
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});			
