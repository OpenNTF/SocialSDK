require([ "sbt/dom", "sbt/lang", "sbt/connections/controls/profiles/ProfileGrid" ], function(dom, lang, ProfileGrid) {
	var grid = new ProfileGrid({
		type : "colleagues",
		userid : "%{name=sample.userId1}",
		hideSorter : true,
		hidePager : true,
		pageSize : 6
	});
	
	var domNode = dom.byId("profileRow");
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
		dom.setText(h3, "<a href=''>My Network</a>");

		var ul = dom.create("ul", {
			"class" : "network"
		}, div);

		return ul;
	};

	grid.profileAction = {
		execute : function(item, opts, event) {
			window.location.assign(item.fnUrl);
		}
	};

	dom.byId("gridDiv").appendChild(grid.domNode);
	grid.update();
});