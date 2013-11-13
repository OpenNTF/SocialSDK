require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "search",
        query : { profileTags : "%{name=sample.profileTags}" }
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});


