require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {
    var grid = new ForumGrid({
         type: "my"
    });
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


