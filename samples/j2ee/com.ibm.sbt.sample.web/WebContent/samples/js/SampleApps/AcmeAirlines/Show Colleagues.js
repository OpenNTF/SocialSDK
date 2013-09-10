require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid" ], 
    function(dom, ColleagueGrid) {
    
    var domNode = dom.byId("colleagues");
    var template = domNode.text || domNode.textContent;

    var grid = new ColleagueGrid({
        type: "dynamic",
        rendererArgs: { hideViewAll: true, template : template },
        email: "%{name=sample.email1}",
        targetEmails: [ "%{name=sample.email1}", "%{name=sample.email2}" ]
    });
    dom.byId("gridDiv").appendChild(grid.domNode);
});