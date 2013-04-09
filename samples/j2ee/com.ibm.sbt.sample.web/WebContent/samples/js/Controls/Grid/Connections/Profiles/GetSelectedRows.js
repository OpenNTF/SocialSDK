require(["sbt/dom", "sbt/controls/grid/connections/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{sample.email1}"
    });
    
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
    
    dom.byId("selectedBtn").onclick = function(evt) {
        var profiles = grid.getSelected();
        
        var str = "";
        for (i in profiles) {
            str += profiles[i].data.getValue("name") + " ";
        }
        alert((str.length == 0) ? "Nothing Selected" : str);
    };
});

