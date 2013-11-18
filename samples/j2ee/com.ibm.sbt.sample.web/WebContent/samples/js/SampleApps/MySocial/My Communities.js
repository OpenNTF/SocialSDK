require([ "sbt/dom", "sbt/connections/controls/communities/CommunityGrid" ], function(dom, CommunityGrid) {
	var communityGrid = new CommunityGrid({
		type : "my",
		hidePager : true,
		hideSorter : true
	});
	
	var domNode = dom.byId("MyCommunitiesTmpl");
	communityGrid.renderer.template = domNode.text || domNode.textContent;

	communityGrid.renderer.renderTable = function(grid, el, items, data) {
		var div = dom.create("div", {
			"class" : "span3"
		}, el);

		var headingDiv = dom.create("div", {
			"class" : "examples"
		}, div);
		
		var h3 = dom.create("h3", {
			"class" : "h3",
		}, headingDiv);
		dom.setText(h3, '<a href="">My Communities</a>');

		var ul = this._create("ul", {
			"class" : "files list"
		}, div);
		
		return ul;
	};

	dom.byId("myCommunitiesDiv").appendChild(communityGrid.domNode);
	communityGrid.update();

});
