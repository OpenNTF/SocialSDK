require(["sbt/dom", "sbt/controls/grid/connections/CommunityGrid"], 
    function(dom, CommunityGrid, CustomCommunityRow) {
        var domNode = dom.byId("communityRow");
        var CustomCommunityRow = domNode.text || domNode.textContent;

        var grid = new CommunityGrid();
        grid.renderer.template = CustomCommunityRow;
                 
        dom.byId("gridDiv").appendChild(grid.domNode);
                 
        grid.update();
    }
);
