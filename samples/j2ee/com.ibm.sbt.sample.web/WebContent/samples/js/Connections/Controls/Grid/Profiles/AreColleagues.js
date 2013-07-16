require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], 
    function(dom, ColleagueGrid) {
        var grid = new ColleagueGrid({
            type: "dynamic",
            rendererArgs: { hideViewAll: true },
            userid: "%{sample.userId1}",
            targetUserids: [ "%{sample.userId2}",  "%{sample.userId3}", "%{sample.userId4}", "%{sample.userId5}" ]
        });
        dom.byId("gridDiv").appendChild(grid.domNode);
    }
);
