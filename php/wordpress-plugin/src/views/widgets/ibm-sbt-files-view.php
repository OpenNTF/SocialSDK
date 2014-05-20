<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>
<?php 
// Ensure that element IDs are unique
$timestamp = time();

	if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
		require BASE_PATH . "{$instance['ibm-sbtk-template']}";
	} else {
		require 'templates/ibm-sbt-files-view.php';
	}
?>
<script type="text/javascript">
require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
	function(declare, dom, FilesView, FileGrid) {
		var actionTemplate = dom.byId("actionTemplate-<?php echo $timestamp; ?>").textContent;
		var viewTemplate = dom.byId("viewTemplate-<?php echo $timestamp; ?>").textContent;
		var moveToTrashTemplate = dom.byId("moveToTrashTemplate-<?php echo $timestamp; ?>").textContent;
		var uploadFileTemplate = dom.byId("uploadFileTemplate-<?php echo $timestamp; ?>").textContent;
		var addTagsTemplate = dom.byId("addTagsTemplate-<?php echo $timestamp; ?>").textContent;
		var shareFilesTemplate = dom.byId("shareFilesTemplate-<?php echo $timestamp; ?>").textContent;
		var dialogTemplate = dom.byId("dialogTemplate-<?php echo $timestamp; ?>").textContent;

	    domNode = dom.byId("filesViewPagingHeader-<?php echo $timestamp; ?>");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
	    var PagingFooter = domNode.text || domNode.textContent;
	
		var filesView = new FilesView({
			gridArgs: {
				type : "<?php echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		        hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		        hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		        hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		        pinFile: <?php echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		        endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
		        pageSize: <?php echo $instance['ibm-sbtk-grid-page-size']; ?>      	 
			}, 
	 		hideActionBar : <?php echo (isset($instance['ibm-sbtk-files-action-bar']) && $instance['ibm-sbtk-files-action-bar'] == 'actionBar' ? "false" : "true"); ?>,
			templateString: viewTemplate,
			endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
			moveToTrashArgs: {templateString:moveToTrashTemplate, endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			shareFileArgs: {templateString:shareFilesTemplate, endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			uploadFileArgs: {templateString:uploadFileTemplate, endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			addTagsArgs: {templateString:addTagsTemplate, endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			dialogArgs:{templateString:dialogTemplate},
			actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
		});

		if ("<?php echo $instance['ibm-sbtk-files-type'] ?>" == "publicFiles") {
			filesView.hideAction("share");
			filesView.hideAction("Upload File");
		}
		filesView.hideAction("Add Tags");

	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("filesViewRow-<?php echo $timestamp; ?>").textContent;
	    filesView.grid.renderer.template = gridTemplate;

	    filesView.grid.renderer.pagerTemplate = PagingHeader;
	    filesView.grid.renderer.footerTemplate = PagingFooter;
	    
	    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(filesView.domNode);
	}
);
</script>
