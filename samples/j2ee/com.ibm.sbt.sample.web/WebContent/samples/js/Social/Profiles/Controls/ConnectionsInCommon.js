require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "connectionsInCommon",
        userid1 : "%{name=sample.userId1}",
        userid2 : "%{name=sample.userId2}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});