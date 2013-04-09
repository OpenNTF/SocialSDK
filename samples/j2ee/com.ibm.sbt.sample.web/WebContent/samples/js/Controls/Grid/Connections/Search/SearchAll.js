require(["sbt/dom", "sbt/controls/grid/connections/SearchGrid"], function(dom, SearchGrid) {
        var grid = new SearchGrid({
             type : "all",
             query : "bookmark"
        });

        dom.byId("gridDiv").appendChild(grid.domNode);

        grid.update();
});