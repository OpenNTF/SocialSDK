require(["sbt/dom", "sbt/connections/controls/profiles/ColleagueGrid"], function(dom, ColleagueGrid) {
    var grid = new ColleagueGrid({
    	userid : "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});