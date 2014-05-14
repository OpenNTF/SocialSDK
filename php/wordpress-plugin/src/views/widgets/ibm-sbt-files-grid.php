<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>

<?php 
// Ensure that element IDs are unique
$timestamp = time();

if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
	require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
} else {
	require_once 'templates/ibm-sbt-files-grid-row.php';
}
?>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
			var domNode = dom.byId("fileRow-<?php echo $timestamp; ?>");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
		    var PagingFooter = domNode.text || domNode.textContent;
		
		    var grid<?php echo $timestamp; ?> = new FileGrid({
		    	 type : "<?php echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		    	 endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
		         hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		         hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		         hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		         pinFile: <?php echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		    	 pageSize: <?php echo $instance['ibm-sbtk-grid-page-size']; ?>,
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
		    });

		    grid<?php echo $timestamp; ?>.renderer.tableClass = "table";
		    grid<?php echo $timestamp; ?>.renderer.template = FileRow;
		    
		    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(grid<?php echo $timestamp; ?>.domNode);    
		    grid<?php echo $timestamp; ?>.update();
	});
</script>