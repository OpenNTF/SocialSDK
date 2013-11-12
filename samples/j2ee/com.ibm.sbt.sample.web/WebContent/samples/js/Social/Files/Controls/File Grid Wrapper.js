require(["sbt/dom", "sbt/config", "sbt/connections/controls/wrappers/FileGridWrapper"], function(dom, config, FileGridWrapper) {
    config.Properties["loginUi"] = "popup"; // Popup login is the most suitable for iframes.
    var fileGridWrapper = new FileGridWrapper({
        type : "myFiles",
        pinFile: true
    });
    
    dom.byId("gridDiv").appendChild(fileGridWrapper.domNode);
    fileGridWrapper.startup();
});
