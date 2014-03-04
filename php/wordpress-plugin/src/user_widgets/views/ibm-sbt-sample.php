<h1 class="sampleWidget">My IBM Connections Sample</h1>
<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>

***REMOVED*** 
	if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
		require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
	} else {
		require_once 'templates/ibm-sbt-sample-template.php';
	}
?>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
			var domNode = dom.byId("sampleFileRow");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("samplePagingHeader");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("samplePagingFooter");
		    var PagingFooter = domNode.text || domNode.textContent;
		
		    var grid = new FileGrid({
		    	 type : "myFiles",
		         hidePager: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		         hideSorter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		         hideFooter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		    	 ps: "***REMOVED*** echo $instance['ibm-sbtk-grid-page-size']; ?>",
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
		    });

		    grid.renderer.tableClass = "table";
		    grid.renderer.template = FileRow;
		    
		    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(grid.domNode);    
		    grid.update();
	});
</script>