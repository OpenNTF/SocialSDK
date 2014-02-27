require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid","sbt/connections/CommunityService"], 
		function(dom, ProfileGrid, CommunityService){
		
			var id = "";
			var communityService = new CommunityService();
			var promise = communityService.getPublicCommunities({}); 
			var self = this;
			promise.then(
					function(communities) {
						if(communities.length < 0){
							dom.setText("content", "There are no public communities");
						}else{
							var id = communities[communities.length-1].getCommunityUuid();
							var grid = new ProfileGrid({
						        type : "communityMembers",
						        communityUuid : id
						    });
						
						    dom.byId("gridDiv").appendChild(grid.domNode);
						
						    grid.update();
						}
					}, 
					function(error) {
						dom.setText("content", error);
					}
			);
			
		
				
			
			
});


