require(["sbt/dom", "sbt/config", "sbt/connections/controls/wrappers/ProfileCardWrapper"], function(dom, config, ProfileCardWrapper) {
    config.Properties["loginUi"] = "popup";
    var profileCardWrapper = new ProfileCardWrapper({ 
        userName : "%{name=sample.displayName1}", 
        userId : "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}" 
    });
    
    dom.byId("vCardDiv").appendChild(profileCardWrapper.domNode);
    profileCardWrapper.startup();
});
