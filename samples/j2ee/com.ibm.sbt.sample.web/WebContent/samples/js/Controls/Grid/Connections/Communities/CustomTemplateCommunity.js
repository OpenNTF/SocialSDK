require(["sbt/dom", "sbt/controls/grid/connections/CommunityGrid", 
         "sbt/text!./samples/js/Controls/Grid/Connections/Communities/templates/CustomCommunityRow.html"], 
    function(dom, CommunityGrid, CustomCommunityRow) {
        var grid = new CommunityGrid();
        grid.renderer.template = CustomCommunityRow;
                 
        dom.byId("gridDiv").appendChild(grid.domNode);
                 
        grid.update();
    }
);
