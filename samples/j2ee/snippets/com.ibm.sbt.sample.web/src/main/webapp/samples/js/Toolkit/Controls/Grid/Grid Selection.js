require(["sbt/dom","sbt/connections/controls/files/FileGrid"], 
	function(dom,FileGrid) {
	
	var grid = new FileGrid({
		type:"public"
	});
	
	var listener = {
		selectionChanged : function(selection) {
			var msg = "You have selected: ";
			for (var i=0; i<selection.length; i++) {
				msg += selection[i].title;
				if (i+1 < selection.length) {
					msg += ", ";
				}
			}
			alert(msg);
		}
	};
	grid.addSelectionListener(listener);
	
	dom.byId("gridDiv").appendChild(grid.domNode);
	
	grid.update();
});