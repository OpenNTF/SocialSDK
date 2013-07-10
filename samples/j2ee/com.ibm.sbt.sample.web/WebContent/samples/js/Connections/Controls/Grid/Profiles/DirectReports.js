require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "peopleManaged",
        email : "georgebandini@renovations.com"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});