require(["sbt/dom", "sbt/smartcloud/controls/profiles/ProfilePanel"],
function(dom, ProfilePanel) {
	var profilePanel = new ProfilePanel({
	    endpoint: "smartcloud"
    }, document.createElement('div'));

    dom.byId("profilePanelDiv").appendChild(profilePanel.domNode);

});
