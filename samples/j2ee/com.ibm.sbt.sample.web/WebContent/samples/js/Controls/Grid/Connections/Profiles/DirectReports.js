require(["sbt/dom", "sbt/controls/grid/connections/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "peopleManaged",
        //email : "%{sample.email2}"
        email : "georgebandini@renovations.com"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});