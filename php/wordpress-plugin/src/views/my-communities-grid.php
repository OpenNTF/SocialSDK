<div id="my-communities-gridDiv" ></div>

<script type="text/javascript">
require(["sbt/dom", "sbt/lang", 
	"sbt/connections/controls/communities/CommunityGrid", 
	"sbt/connections/controls/bootstrap/CommunityRendererMixin"], 
    function(dom, lang, CommunityGrid, CommunityRendererMixin) {
    
    	var grid = new CommunityGrid({ hideSorter:true, hideFooter:true });
    
    	lang.mixin(grid.renderer, CommunityRendererMixin);
             
    	dom.byId("my-communities-gridDiv").appendChild(grid.domNode);
             
    	grid.update();
});
</script>
