require(["sbt/dom","sbt/connections/controls/files/FileGrid"], function(dom,FileGrid){
	
	var grid = new FileGrid({
		type:"public"
	});
	
	dom.byId("gridDiv").appendChild(grid.domNode);
	
	grid.update();
	
	grid.onUpdate = function(){
		grid.setSelectionListener(function(){
			alert("row");
		},"click");
	}
	
	
});