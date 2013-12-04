require(["sbt/dom", "sbt/smartcloud/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "communityMembers",
        endpoint: "smartcloud",
        communityUuid:"4791821f-de8c-4f8b-8699-1c0380d49a85"
    });
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
    
    console.log("!");
  
});