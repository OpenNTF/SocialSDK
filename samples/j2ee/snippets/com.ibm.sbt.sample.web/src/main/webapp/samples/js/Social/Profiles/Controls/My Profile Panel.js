require(["sbt/dom", "sbt/connections/controls/profiles/ProfilePanel"],
function(dom, ProfilePanel) {
	var profilePanel = new ProfilePanel({
    }, document.createElement('div'));

    dom.byId("profilePanelDiv").appendChild(profilePanel.domNode);

});
