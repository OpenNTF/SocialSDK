require(["sbt/dom", "sbt/smartcloud/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "communityMembers",
        endpoint: "smartcloud",
        communityUuid:"%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}"
    });
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
  
});