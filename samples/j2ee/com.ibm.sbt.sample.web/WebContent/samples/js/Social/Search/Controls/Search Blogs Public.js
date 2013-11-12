require(["sbt/dom", "sbt/connections/controls/search/SearchGrid"], function(dom, SearchGrid) {
        var grid = new SearchGrid({
             type: "public",
             query : { component : "blogs", query : "blog" }
        });

        dom.byId("gridDiv").appendChild(grid.domNode);

        grid.update();
});