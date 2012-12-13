require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		load: function(profile){
			if(profile.getPhoneNumber()){
				dom.setText("content",profile.getPhoneNumber());
			}else{
				dom.setText("content","No Result");
			}			
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
}); 
