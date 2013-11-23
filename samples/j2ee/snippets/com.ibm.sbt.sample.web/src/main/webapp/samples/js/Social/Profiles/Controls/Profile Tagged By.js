require(["sbt/dom", "sbt/connections/controls/profiles/ProfileTagsGrid"], function(dom, ProfileTagsGrid) {
    var grid = new ProfileTagsGrid({
        type : "list",
        targetEmail : "%{name=sample.email1}",
        sourceEmail : "%{name=sample.email2}",
        sourceName : "%{name=sample.displayName2}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
