require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		load: function(profile){
			if(profile.getProfileUrl()){
				var anchorElement = document.createElement("a");
				anchorElement.setAttribute("href", profile.getProfileUrl());
				dom.setText(anchorElement,profile.getProfileUrl());
				document.getElementById("content").appendChild(anchorElement);				
			}else{
				dom.setText("content","No Result");
			}					
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
}); 
