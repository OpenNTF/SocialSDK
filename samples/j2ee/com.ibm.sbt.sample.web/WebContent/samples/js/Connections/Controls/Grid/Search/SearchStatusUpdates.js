require(["sbt/dom", "sbt/connections/controls/search/SearchGrid"], function(dom, SearchGrid) {
        var grid = new SearchGrid({
             type : "statusupdates",
             query : "the"
        });

        dom.byId("gridDiv").appendChild(grid.domNode);

        grid.update();
});