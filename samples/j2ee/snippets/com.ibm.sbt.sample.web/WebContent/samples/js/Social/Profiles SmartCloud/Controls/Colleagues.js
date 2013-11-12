require(["sbt/dom", "sbt/smartcloud/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "profile",
        endpoint: "smartcloud"
    });
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
  
});