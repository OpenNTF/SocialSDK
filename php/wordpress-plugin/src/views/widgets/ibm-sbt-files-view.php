<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>
***REMOVED*** 
// Ensure that element IDs are unique
$timestamp = time();

	if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
		require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
	} else {
		require_once 'templates/ibm-sbt-files-view.php';
	}
?>
<script type="text/javascript">
require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
	function(declare, dom, FilesView, FileGrid) {
		var actionTemplate = dom.byId("actionTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var viewTemplate = dom.byId("viewTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var moveToTrashTemplate = dom.byId("moveToTrashTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var uploadFileTemplate = dom.byId("uploadFileTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var addTagsTemplate = dom.byId("addTagsTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var shareFilesTemplate = dom.byId("shareFilesTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
		var dialogTemplate = dom.byId("dialogTemplate-***REMOVED*** echo $timestamp; ?>").textContent;

	    domNode = dom.byId("filesViewPagingHeader-***REMOVED*** echo $timestamp; ?>");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter-***REMOVED*** echo $timestamp; ?>");
	    var PagingFooter = domNode.text || domNode.textContent;
	
		var filesView = new FilesView({
			gridArgs: {
				type : "***REMOVED*** echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		        hidePager: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		        hideSorter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		        hideFooter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		        pinFile: ***REMOVED*** echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		        endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
		        pageSize: ***REMOVED*** echo $instance['ibm-sbtk-grid-page-size']; ?>      	 
			}, 
	 		hideActionBar : ***REMOVED*** echo (isset($instance['ibm-sbtk-files-action-bar']) && $instance['ibm-sbtk-files-action-bar'] == 'actionBar' ? "false" : "true"); ?>,
			templateString: viewTemplate,
			endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
			moveToTrashArgs: {templateString:moveToTrashTemplate, endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			shareFileArgs: {templateString:shareFilesTemplate, endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			uploadFileArgs: {templateString:uploadFileTemplate, endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			addTagsArgs: {templateString:addTagsTemplate, endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>"},
			dialogArgs:{templateString:dialogTemplate},
			actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
		});

	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("filesViewRow-***REMOVED*** echo $timestamp; ?>").textContent;
	    filesView.grid.renderer.template = gridTemplate;

	    filesView.grid.renderer.pagerTemplate = PagingHeader;
	    filesView.grid.renderer.footerTemplate = PagingFooter;
	    
	    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(filesView.domNode);
	}
);
</script>
