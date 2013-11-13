require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], 
    function(dom, ColleagueGrid) {
        var grid = new ColleagueGrid({
            type: "dynamic",
            rendererArgs: { hideViewAll: true },
            userid: "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}",
            targetUserids: [ "%{name=sample.userId2}",  "%{name=sample.userId3}", "%{name=sample.userId4}", "%{name=sample.userId5}" ]
        });
        dom.byId("gridDiv").appendChild(grid.domNode);
    }
);
