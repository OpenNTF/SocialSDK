require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "communityMembers",
        communityUuid : "%{name=sample.userId3|helpSnippetId=Social_Communities_Get_My_Communities}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});


