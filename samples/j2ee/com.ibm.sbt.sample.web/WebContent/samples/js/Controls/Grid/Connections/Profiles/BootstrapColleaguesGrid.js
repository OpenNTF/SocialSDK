require(["sbt/dom", "sbt/controls/grid/connections/ColleagueGrid"], function(dom, ColleagueGrid) {
    var grid = new ColleagueGrid({
        email : "%{sample.email1}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});