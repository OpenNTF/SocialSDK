require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
	var communityService = new CommunityService();
	communityService.getPublicCommunities({
		parameters:{
			ps:5
		},
		load: function(communities){		   
			if (communities.length == 0) {
				dom.setText("content", "No Result");
            }else{
				for(var count = 0; count < communities.length; count ++){
					var displayStr = "";
					var community = communities[count];				
					displayStr +=  community.getTitle() + " : " + community.getCommunityUuid() + " : " + community.getAuthor().getName();               
					var liElement = document.createElement("li");
					liElement.setAttribute("id", "li" + count);
					document.getElementById("content").appendChild(liElement);
					dom.setText("li" + count, displayStr);
				}
            }
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}		
	});
});