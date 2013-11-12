require(["sbt/dom", "sbt/connections/controls/activities/ActivityGrid"], function(dom, ActivityGrid) {
    
	var grid = new ActivityGrid({
		type: "my"
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});