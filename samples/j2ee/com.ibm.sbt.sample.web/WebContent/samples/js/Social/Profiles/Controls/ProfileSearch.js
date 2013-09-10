require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "search",
        query : { userid : "%{name=sample.userId1}" }
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});


