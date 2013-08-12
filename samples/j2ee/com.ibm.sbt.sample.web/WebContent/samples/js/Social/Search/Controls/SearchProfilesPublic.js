require(["sbt/dom", "sbt/connections/controls/search/SearchGrid"], function(dom, SearchGrid) {
        var grid = new SearchGrid({
             type: "public",
             app : "profiles",
             query : "frank"
        });

        dom.byId("gridDiv").appendChild(grid.domNode);

        grid.update();
});