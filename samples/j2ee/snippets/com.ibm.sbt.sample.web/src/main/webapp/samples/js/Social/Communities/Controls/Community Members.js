require(["sbt/dom", "sbt/connections/controls/communities/CommunityMembersGrid"], function(dom, CommunityMembersGrid) {
    var grid = new CommunityMembersGrid({
        type : "communityMembers",
        communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});


