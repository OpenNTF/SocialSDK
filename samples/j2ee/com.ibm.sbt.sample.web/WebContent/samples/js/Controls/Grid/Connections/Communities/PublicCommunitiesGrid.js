require(["sbt/dom", "sbt/controls/grid/connections/CommunityGrid"], function(dom, CommunityGrid) {
    var grid = new CommunityGrid();
           
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


