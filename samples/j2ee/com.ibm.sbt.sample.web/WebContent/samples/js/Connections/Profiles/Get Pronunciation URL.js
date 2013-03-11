require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		load: function(profile){
			if(profile.getPronunciationUrl()){
				var anchorElement = document.createElement("a");
				anchorElement.setAttribute("href", profile.getPronunciationUrl());
				dom.setText(anchorElement,profile.getPronunciationUrl());
				document.getElementById("content").appendChild(anchorElement);				
			}else{
				dom.setText("content","No Result");
			}				
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " + error.code + ". Error Message = " + error.message);
		}
	});
}); 
