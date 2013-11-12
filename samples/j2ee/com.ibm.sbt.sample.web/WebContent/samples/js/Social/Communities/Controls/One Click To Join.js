require(["sbt/dom", "sbt/lang",
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/bootstrap/CommunityRendererMixin",
         "sbt/connections/CommunityService"], 
function(dom, lang, CommunityGrid, CommunityRendererMixin, CommunityService) {
	var communityService = new CommunityService();
	var communitiesGrid = new CommunityGrid({ hideSorter : true });
	lang.mixin(communitiesGrid.renderer, CommunityRendererMixin);

	//Set the custom template
	var domNode = dom.byId("communityRow");
	var CustomCommunityRow = domNode.text || domNode.textContent;
	communitiesGrid.renderer.template = CustomCommunityRow;

	dom.byId("gridDiv").appendChild(communitiesGrid.domNode);
	communitiesGrid.update();

	dom.byId("addBtn").onclick = function(evt) {
		var communities = communitiesGrid.getSelected();

		if (!communities || !communities.length > 0) {
			dom.byId("success").style.display = "none";
			dom.byId("error").style.display = "";
			dom.setText("error", "Please select a community.");
			return;
		}

		var userEmail = document.getElementById("emailTextField").value;
		if (!userEmail || !userEmail.length > 0) {
			dom.byId("success").style.display = "none";
			dom.byId("error").style.display = "";
			dom.setText("error", "Please enter a valid email adress.");
			return;
		}

		for (i in communities) {
			var communityUrl = communities[i].data.getValue("uid");
			var index = communityUrl.indexOf("communityUuid=");
			var communityUuid = communityUrl.substring(index + 14);
			
			communityService.addMember(communityUuid, userEmail).then(
				function(community) {
					dom.byId("success").style.display = "";
					dom.byId("error").style.display = "none";
					dom.setText("success", "Successfully added " + userEmail + " to community: " + community.getCommunityUuid());
				},
				function(error) {
					dom.byId("success").style.display = "none";
					dom.byId("error").style.display = "";
					dom.setText("error", "Could not add the selecedted user to the selected community: "+error.message);
					console.error(error);
				}
			);
		}
	};
});