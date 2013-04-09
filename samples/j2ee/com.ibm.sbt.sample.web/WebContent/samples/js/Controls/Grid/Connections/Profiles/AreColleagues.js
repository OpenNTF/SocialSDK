require(["sbt/dom", "sbt/controls/grid/connections/ColleagueGrid"], 
    function(dom, ColleagueGrid, Endpoint, xml, xpath, core) {
        var grid = new ColleagueGrid({
            type: "dynamic",
            rendererArgs: { hideViewAll: true },
            email: "%{sample.email1}",
            targetEmails: [ "AndreasBerzat@renovations.com", "JasmineHaj@renovations.com", "TedAmado@renovations.com", 
                            "LouiseFitzgerald@renovations.com", "EdBlanks@renovations.com", "NancySmith@renovations.com" ]
        });
        dom.byId("gridDiv").appendChild(grid.domNode);
    }
);
