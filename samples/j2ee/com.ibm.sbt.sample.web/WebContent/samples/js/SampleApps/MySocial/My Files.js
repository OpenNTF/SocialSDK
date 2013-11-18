require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
	var grid = new FileGrid({
		type : "library",
		pinFile : true,
		hideSorter : true,
		hidePager : true
	});
	
	var domNode = dom.byId("fileRow");
	grid.renderer.template = domNode.text || domNode.textContent;

	grid.renderer.renderTable = function(grid, el, items, data) {
		var div = dom.create("div", {
			"class" : "span3"
		}, el);

		var headingDiv = dom.create("div", {
			"class" : "examples"
		}, div);
	
		var h3 = dom.create("h3", {
			"class" : "h3",
		}, headingDiv);
		dom.setText(h3, '<a href="">My Files</a>');

		var ul = dom.create("ul", {
			style : "list-style:none outside none;margin: 0;"
		}, div);
		
		return ul;
	};

	dom.byId("gridDiv").appendChild(grid.domNode);
	grid.update();
});