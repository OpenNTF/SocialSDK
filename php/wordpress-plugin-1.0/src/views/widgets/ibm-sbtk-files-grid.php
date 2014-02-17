<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>
***REMOVED*** require_once 'templates/ibm-sbtk-files-grid-row.php';?>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
		    var grid = new FileGrid({
		    	 type : "***REMOVED*** echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		         hidePager: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		         hideSorter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		         hideFooter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		         pinFile: ***REMOVED*** echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		    	 ps: "***REMOVED*** echo $instance['ibm-sbtk-grid-page-size']; ?>"       	 
		    });
		    ***REMOVED*** 
		    	if ($instance['ibm-sbtk-integrate'] == 'integrate') {
				?>
				var domNode = dom.byId("files-grid-row-template");
			    var CustomFilesGridRow = domNode.text || domNode.textContent;
			    grid.renderer.template = CustomFilesGridRow;         
				***REMOVED***
				}
			?>
		    
		    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(grid.domNode);    
		    grid.update();
	});
</script>