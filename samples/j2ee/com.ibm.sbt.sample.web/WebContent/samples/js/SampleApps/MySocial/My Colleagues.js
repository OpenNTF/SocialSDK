require(["sbt/dom", "sbt/lang", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, lang, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{sample.email1}"
    });

    grid.renderer.template = dom.byId("profileRow").innerHTML;
    grid.renderer.pagerTemplate = dom.byId("pagerTemplate").innerHTML;
    grid.renderer.sortTemplate = dom.byId("sortingTemplate").innerHTML;
    grid.renderer.sortAnchor = dom.byId("sortAnchorTemplate").innerHTML;
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
   
});