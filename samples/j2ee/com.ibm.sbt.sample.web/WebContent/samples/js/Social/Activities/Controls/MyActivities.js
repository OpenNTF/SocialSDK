require(["sbt/dom", "sbt/connections/controls/activities/ActivitiesGrid"], function(dom, ActivitiesGrid) {
    
	var grid = new ActivitiesGrid({
		type: "my"
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});