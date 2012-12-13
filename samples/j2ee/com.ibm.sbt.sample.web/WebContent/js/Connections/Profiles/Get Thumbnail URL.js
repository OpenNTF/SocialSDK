require(["sbt/connections/ProfileService","sbt/dom"], function(ProfileService,dom) {
	var id = "%{sample.id1}";
	var profileService = new ProfileService();
	profileService.getProfile({
		id: id,		
		load: function(profile){
			if(profile.getThumbnailUrl()){
				var anchorElement = document.createElement("a");
				anchorElement.setAttribute("href", profile.getThumbnailUrl());
				dom.setText(anchorElement,profile.getThumbnailUrl());
				document.getElementById("content").appendChild(anchorElement);		
			}else{
				dom.setText("content","No Result");
			}			
			dom.byId("photo").src = profile.getThumbnailUrl();
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
}); 
