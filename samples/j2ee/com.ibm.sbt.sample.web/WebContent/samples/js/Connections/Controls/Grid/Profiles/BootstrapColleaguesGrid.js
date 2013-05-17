require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], function(dom, ColleagueGrid) {
    var grid = new ColleagueGrid({
        email : "%{sample.email1}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});