<div id="<?php echo $this->config->elementID; ?>"></div>
<?php 	
global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-view.php';
?>


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
	    domNode = dom.byId("filesViewRow");
	    var FileRow = domNode.text || domNode.textContent;
	
		var filesView = new FilesView({
			gridArgs: {
				type : "myFiles",
		    	 endpoint: "<?php echo $this->config->endpoint; ?>",
		         hidePager: false,
		         hideSorter: true,
		         hideFooter: false,
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 	 
			}, 
	 		hideActionBar : false,
			templateString: viewTemplate,
			endpoint: "<?php echo $this->config->endpoint; ?>",
			moveToTrashArgs: {templateString:moveToTrashTemplate},
			shareFileArgs: {templateString:shareFilesTemplate},
			uploadFileArgs: {templateString:uploadFileTemplate},
			addTagsArgs: {templateString:addTagsTemplate},
			dialogArgs:{templateString:dialogTemplate},
			actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
		});

	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("filesViewRow").textContent;
	    filesView.grid.renderer.template = gridTemplate;

	    filesView.grid.renderer.pagerTemplate = PagingHeader;
	    filesView.grid.renderer.footerTemplate = PagingFooter;
	    
	    dom.byId("<?php echo $this->config->elementID; ?>").appendChild(filesView.domNode);
	}
);
</script>
