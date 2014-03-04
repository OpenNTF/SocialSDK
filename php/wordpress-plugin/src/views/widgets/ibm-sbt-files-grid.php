<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>

***REMOVED*** 
if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
	require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
} else {
	require_once 'templates/ibm-sbt-files-grid-row.php';
}
?>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
			var domNode = dom.byId("fileRow");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingHeader");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingFooter");
		    var PagingFooter = domNode.text || domNode.textContent;
		
		    var grid = new FileGrid({
		    	 type : "***REMOVED*** echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		         hidePager: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		         hideSorter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		         hideFooter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		         pinFile: ***REMOVED*** echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		    	 ps: "***REMOVED*** echo $instance['ibm-sbtk-grid-page-size']; ?>",
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
		    });

		    grid.renderer.tableClass = "table";
		    grid.renderer.template = FileRow;
		    
		    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(grid.domNode);    
		    grid.update();
	});
</script>