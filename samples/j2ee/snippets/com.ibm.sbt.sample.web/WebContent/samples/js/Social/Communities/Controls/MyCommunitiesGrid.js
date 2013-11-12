require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
    var grid = new CommunityGrid({
         type: "my"
    });
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


