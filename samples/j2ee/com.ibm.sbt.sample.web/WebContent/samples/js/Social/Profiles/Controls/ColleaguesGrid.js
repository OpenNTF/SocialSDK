require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], function(dom, ColleagueGrid) {
    var grid = new ColleagueGrid({
    	userid : "%{sample.userId1}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});