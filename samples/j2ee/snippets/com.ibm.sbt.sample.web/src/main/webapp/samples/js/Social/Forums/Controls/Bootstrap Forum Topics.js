require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {
    var grid = new ForumGrid({
         type: "forumTopics",
         theme: "bootstrap",
         hidePager:true,
         forumUuid: "%{name=sample.forumUuid|helpSnippetId=Social_Forums_Get_My_Forums}"
    });
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});

