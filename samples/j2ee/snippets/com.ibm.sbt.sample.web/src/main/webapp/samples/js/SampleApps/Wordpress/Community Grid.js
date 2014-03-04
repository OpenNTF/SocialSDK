require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow");
    var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingHeader");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingFooter");
    var PagingFooter = domNode.text || domNode.textContent;
    
    var grid = new CommunityGrid({
    	type : "my",
        hidePager: false,
        hideSorter: true,
        hideFooter: false,
         rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
    });

    grid.renderer.tableClass = "table";
    grid.renderer.template = CommunityRow;
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});