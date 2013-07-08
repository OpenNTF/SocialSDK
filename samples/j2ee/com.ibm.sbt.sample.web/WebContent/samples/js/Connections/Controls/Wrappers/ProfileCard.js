require(["sbt/dom", "sbt/config", "sbt/connections/controls/wrappers/ProfileCardWrapper"], function(dom, config, ProfileCardWrapper) {
    config.Properties["loginUi"] = "popup"; // Popup login is the most suitable for iframes.
    var profileCardWrapper = new ProfileCardWrapper({
        type : "library",
        pinFile: true
    });
    
    dom.byId("vCardDiv").appendChild(profileCardWrapper.domNode);
    profileCardWrapper.startup();
});
