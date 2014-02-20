<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>
<?php require_once 'templates/ibm-sbtk-files-view.php';?>
<script type="text/javascript">
require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
	function(declare, dom, FilesView, FileGrid) {
		var actionTemplate = dom.byId("actionTemplate").textContent;
		var viewTemplate = dom.byId("viewTemplate").textContent;
		var moveToTrashTemplate = dom.byId("moveToTrashTemplate").textContent;
		var uploadFileTemplate = dom.byId("uploadFileTemplate").textContent;
		var addTagsTemplate = dom.byId("addTagsTemplate").textContent;
		var shareFilesTemplate = dom.byId("shareFilesTemplate").textContent;
		var dialogTemplate = dom.byId("dialogTemplate").textContent;

	    domNode = dom.byId("pagingHeader");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter");
	    var PagingFooter = domNode.text || domNode.textContent;
	
		var filesView = new FilesView({
			gridArgs: {
				type : "<?php echo (isset($instance['ibm-sbtk-files-type']) ? $instance['ibm-sbtk-files-type'] : 'publicFiles');?>",
		        hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
		        hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
		        hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
		        pinFile: <?php echo (isset($instance['ibm-sbtk-files-pin-file']) && $instance['ibm-sbtk-files-pin-file'] == 'pin' ? "false" : "true"); ?>,
		   	 	ps: "<?php echo $instance['ibm-sbtk-grid-page-size']; ?>"       	 
			}, 
	 		hideActionBar : <?php echo (isset($instance['ibm-sbtk-files-action-bar']) && $instance['ibm-sbtk-files-action-bar'] == 'actionBar' ? "false" : "true"); ?>,
			templateString: viewTemplate,
			
			moveToTrashArgs: {templateString:moveToTrashTemplate},
			shareFileArgs: {templateString:shareFilesTemplate},
			uploadFileArgs: {templateString:uploadFileTemplate},
			addTagsArgs: {templateString:addTagsTemplate},
			dialogArgs:{templateString:dialogTemplate},
			actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
		});

	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("fileRow").textContent;
	    filesView.grid.renderer.template = gridTemplate;

	    filesView.grid.renderer.pagerTemplate = PagingHeader;
	    filesView.grid.renderer.footerTemplate = PagingFooter;
	    
	    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(filesView.domNode);
	}
);
</script>