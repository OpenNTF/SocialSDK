require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{sample.email1}",
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
   
});