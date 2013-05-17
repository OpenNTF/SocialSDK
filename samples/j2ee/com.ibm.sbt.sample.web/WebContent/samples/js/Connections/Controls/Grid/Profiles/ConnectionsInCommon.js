require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "connectionsInCommon",
        email1 : "%{sample.email1}",
        email2 : "%{sample.email2}",
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});