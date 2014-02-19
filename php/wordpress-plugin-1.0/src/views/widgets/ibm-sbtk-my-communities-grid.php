<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>
<?php require_once 'templates/ibm-sbtk-communities-grid-row.php';?>
<script type="text/javascript">
require(["sbt/dom", "sbt/lang", 
	"sbt/connections/controls/communities/CommunityGrid", 
	"sbt/connections/controls/bootstrap/CommunityRendererMixin"], 
    function(dom, lang, CommunityGrid, CommunityRendererMixin) {
    
    	var grid = new CommunityGrid({ 
             type : "<?php echo $instance['ibm-sbtk-communities-type'];?>",
	         hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
	         hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
	         hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
	    	 ps: "<?php echo $instance['ibm-sbtk-grid-page-size']; ?>"	  
        });
    
    	lang.mixin(grid.renderer, CommunityRendererMixin);
    	<?php 
    	    if ($instance['ibm-sbtk-integrate'] == 'integrate') {
    			?>
    			var domNode = dom.byId("communities-grid-row-template");
    		    var CustomFilesGridRow = domNode.text || domNode.textContent;
    		    grid.renderer.template = CustomFilesGridRow;         
    			<?php
    			}
    		?>     
    	dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(grid.domNode);  
             
    	grid.update();
});
</script>
