require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/bootstrap/CommunityRendererMixin",
         "sbt/lang"], 
         
function(dom, CommunityGrid, CommunityRendererMixin, lang) {
    var grid = new CommunityGrid();
    
    lang.mixin(grid.renderer, CommunityRendererMixin);
           
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


