require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "peopleManaged",
        userid : "%{sample.userId3}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});