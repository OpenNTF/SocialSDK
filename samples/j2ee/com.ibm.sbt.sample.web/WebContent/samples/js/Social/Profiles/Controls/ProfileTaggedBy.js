require(["sbt/dom", "sbt/connections/controls/profiles/ProfileTagsGrid"], function(dom, ProfileTagsGrid) {
    var grid = new ProfileTagsGrid({
        type : "list",
        targetEmail : "%{sample.email1}",
        sourceEmail : "%{sample.email2}",
        sourceName : "%{sample.displayName2}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
