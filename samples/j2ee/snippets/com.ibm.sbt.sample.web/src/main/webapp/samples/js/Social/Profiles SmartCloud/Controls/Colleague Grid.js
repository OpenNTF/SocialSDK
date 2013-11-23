require(["sbt/dom", "sbt/smartcloud/controls/profiles/ColleagueGrid"], function(dom, ColleagueGrid) {
    var grid = new ColleagueGrid({
        endpoint:"smartcloud"
    });
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
  
});