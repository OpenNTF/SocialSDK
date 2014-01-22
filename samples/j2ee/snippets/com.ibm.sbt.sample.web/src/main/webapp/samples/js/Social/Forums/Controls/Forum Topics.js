require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {
    var grid = new ForumGrid({
         type: "forumTopics",
         forumUuid: "c843c7b6-47e3-4296-b221-ad8ac8e426be"
    });
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});

