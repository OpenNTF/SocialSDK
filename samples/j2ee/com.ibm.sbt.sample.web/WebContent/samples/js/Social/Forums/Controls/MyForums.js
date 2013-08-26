require(["sbt/dom", "sbt/connections/controls/forum/ForumGrid"], function(dom, ForumGrid) {
    var grid = new ForumGrid({
         type: "my"
    });
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


