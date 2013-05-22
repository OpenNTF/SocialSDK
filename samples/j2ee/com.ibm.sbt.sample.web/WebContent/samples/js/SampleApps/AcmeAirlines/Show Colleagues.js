require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid" ], 
    function(dom, ColleagueGrid) {
    
    var domNode = dom.byId("colleagues");
    var template = domNode.text || domNode.textContent;

    var grid = new ColleagueGrid({
        type: "dynamic",
        rendererArgs: { hideViewAll: true, template : template },
        email: "%{sample.email1}",
        targetEmails: [ "%{sample.email1}", "%{sample.email2}" ]
    });
    dom.byId("gridDiv").appendChild(grid.domNode);
});