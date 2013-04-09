require(["sbt/dom", "sbt/lang", "sbt/controls/grid/connections/CommunityGrid", "sbt/controls/grid/bootstrap/CommunityRendererMixin", "sbt/dom"], 
        function(dom, lang, CommunityGrid, CommunityRendererMixin) {
    var grid = new CommunityGrid();
    
    lang.mixin(grid.renderer, CommunityRendererMixin);
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


