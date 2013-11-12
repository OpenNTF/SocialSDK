require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], 
    function(dom, CommunityGrid) {
        var domNode = dom.byId("communityRow");
        var CustomCommunityRow = domNode.text || domNode.textContent;

        var grid = new CommunityGrid();
        grid.renderer.template = CustomCommunityRow;
                 
        dom.byId("gridDiv").appendChild(grid.domNode);
                 
        grid.update();
    }
);
