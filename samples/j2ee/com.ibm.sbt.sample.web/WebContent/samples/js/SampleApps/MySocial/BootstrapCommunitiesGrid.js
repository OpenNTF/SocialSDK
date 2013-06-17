require(["sbt/dom", "sbt/lang", "sbt/connections/controls/communities/CommunityGrid", "sbt/connections/controls/bootstrap/CommunityRendererMixin", "sbt/dom"], 
        function(dom, lang, CommunityGrid, CommunityRendererMixin) {
		    var grid = new CommunityGrid();
		    
		    lang.mixin(grid.renderer, CommunityRendererMixin);
		    
		    var domNode = dom.byId("communityRow");
	        var CustomCommunityRow = domNode.text || domNode.textContent;
	        grid.renderer.template = CustomCommunityRow;
	        
		    dom.byId("gridDiv").appendChild(grid.domNode);
		             
		    grid.update();
});


