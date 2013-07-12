require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], 
    function(dom, ColleagueGrid) {
        var grid = new ColleagueGrid({
            type: "dynamic",
            rendererArgs: { hideViewAll: true },
            userid: "%{sample.userId1}",
            targetUserids: [ "%{sample.userId2}",  "%{sample.userId3}", "26356FD1-CDC8-67D2-4825-7A7000256C06", "3657BE03-58AC-4CB5-4825-7A700025E3BA" ]
        });
        dom.byId("gridDiv").appendChild(grid.domNode);
    }
);
