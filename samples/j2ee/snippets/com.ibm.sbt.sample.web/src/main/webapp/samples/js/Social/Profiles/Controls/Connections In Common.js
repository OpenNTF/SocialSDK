require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "connectionsInCommon",
        userid1 : "%{name=sample.userId1|helpSnippetId=Social_Profiles_API_GetColleagues}",
        userid2 : "%{name=sample.userId2|helpSnippetId=Social_Profiles_API_GetColleagues}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});