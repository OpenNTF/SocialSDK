require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "colleagues",
        userid : "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}"
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

