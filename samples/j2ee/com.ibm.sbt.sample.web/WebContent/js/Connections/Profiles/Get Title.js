require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		handle: function(profile){
			if(profile.getTitle()){
				dom.setText("content",profile.getTitle());
			}else{
				dom.setText("content","No Result");
			}				
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
}); 
