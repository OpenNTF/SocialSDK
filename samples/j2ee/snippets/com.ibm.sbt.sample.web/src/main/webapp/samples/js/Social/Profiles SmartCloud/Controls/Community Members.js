require(["sbt/dom", "sbt/smartcloud/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "communityMembers",
        endpoint: "smartcloud",
        communityUuid:"%{name=sample.fileCommunityId|helpSnippetId=Social_Communities_Get_My_Communities}"
    });
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
    
    console.log("!");
  
});