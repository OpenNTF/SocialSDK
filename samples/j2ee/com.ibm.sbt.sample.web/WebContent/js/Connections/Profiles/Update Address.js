require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id		: id,
		loadIt	: false,
		load	:function(profile){
			profile.setAddress({
				building:"WTFT5",
				floor:"3rd",
				streetAddress:"5 TECHNOLOGY PARK DR,WESTFORD TECHNOLOGY PARK",
				locality:"WESTFORD",
				region:"MA",
				postalCode:"01886-3141",
				country:"United States"
			});		
			profileService.updateProfile(profile, {
				load: function(profile){
					dom.setText("content","Address updated");
				},
				error : function(error){
					dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
				}
			});
		}
	});
	
});		