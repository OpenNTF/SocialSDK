<?php 
	$settings = new SBTSettings();
	
	// Ensure that element IDs are unique
	$milliseconds = microtime(true) * 1000; 
	$timestamp = round($milliseconds);
?>
<button style="font-size: 12px;" class="btn btn-primary" onclick="window.open('<?php echo $settings->getURL($instance['ibm-sbtk-endpoint']); ?>/files/app#', '_blank');"><?php echo $GLOBALS[LANG]['open_files'];?></button><br/><br/>
<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>

<?php 


if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
	require BASE_PATH . "{$instance['ibm-sbtk-template']}";
} else {
	require 'templates/ibm-sbt-files-grid-row.php';
}
?>

<script type="text/javascript">
require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/config", "sbt/connections/controls/files/FileGrid"], 
	function(ProfileService, dom, config, FileGrid) {

	var endpoint = config.findEndpoint("<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>");
	var url = "/connections/connections/opensocial/basic/rest/people/@me/@self";
	endpoint.request(url, { handleAs : "json" }).then(
		function(response) {
			var userid = parseUserid(response.entry);
			
			var domNode = dom.byId("fileRow-<?php echo $timestamp; ?>");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
		    var PagingFooter = domNode.text || domNode.textContent;

			var grid<?php echo $timestamp; ?> = null;
			
			if ("<?php echo $instance['ibm-sbtk-files-type'] ?>" == "myFolders") {
				grid<?php echo $timestamp; ?> = new FileGrid({
			    	 type : "<?php echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
			    	 endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
			         hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
			         hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
			         hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
			         pinFile: <?php echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
			    	 pageSize: <?php echo $instance['ibm-sbtk-grid-page-size']; ?>,
					 creator: userid,
			    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
			    });
			} else {
			    grid<?php echo $timestamp; ?> = new FileGrid({
			    	 type : "<?php echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
			    	 endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
			         hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
			         hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
			         hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
			         pinFile: <?php echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
			    	 pageSize: <?php echo $instance['ibm-sbtk-grid-page-size']; ?>,
			    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
			    });
			}

		    grid<?php echo $timestamp; ?>.renderer.tableClass = "table";
		    grid<?php echo $timestamp; ?>.renderer.template = FileRow;
		    
		    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(grid<?php echo $timestamp; ?>.domNode);    
		    grid<?php echo $timestamp; ?>.update();
		}, 
		function(error) {
			console.log(error);
		}
	);

	function getAttributeValue(value) {
		if (value) {
			return value;
		} else {
			return "";
		}
	};

	function parseUserid(entry) {
		return entry.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
	}
});
</script>