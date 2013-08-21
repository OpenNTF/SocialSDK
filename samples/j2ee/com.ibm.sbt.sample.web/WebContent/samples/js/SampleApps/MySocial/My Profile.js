require(["sbt/dom", 
         "sbt/connections/controls/profiles/ProfilePanel",
         "sbt/connections/controls/profiles/ProfileTagsGrid",
         "sbt/connections/controls/communities/CommunityGrid"],
function(dom, ProfilePanel, ProfileTagsGrid, CommunityGrid) {
	// Profile Panel
	var profilePanel = new ProfilePanel({
        email : '%{sample.email1}'
    }, document.createElement('div'));
    dom.byId("profilePanelDiv").appendChild(profilePanel.domNode);

    // Profile Tags Grid
    var grid = new ProfileTagsGrid({
        type : "list",
        targetEmail : "%{sample.email1}"
    });
    dom.byId("profileTagsGridDiv").appendChild(grid.domNode);
    grid.update();
    
    /*
    // Community Grid
    var grid = new CommunityGrid({
        type: "public",
        query: { email: "%{sample.email1}" }
   });
   dom.byId("communityGridDiv").appendChild(grid.domNode);
   grid.update();
   */
});
