<?php 	
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-view.php';
?>

<select id="ibm-sbt-files-view-list-<?php echo $timestamp; ?>" onchange="onFilesViewTypeChange<?php echo $timestamp; ?>();">
	<option value="myFiles">My files</option>
	<option value="publicFiles">Public files</option>
	<option value="myPinnedFiles">My pinned files</option>
	<option value="myFolders">My folders</option>
	<option value="publicFolders">Public folders</option>
	<option value="myPinnedFolders">My pinned folders</option>
	<option value="activeFolders">Active folders</option>
	<option value="fileShares">File shares</option>
</select>
<div id="<?php echo $this->config->elementID; ?>"></div>

<script type="text/javascript">
function onFilesViewTypeChange<?php echo $timestamp; ?>() {
	require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
			function(declare, dom, FilesView, FileGrid) {
				var typeList = dom.byId("ibm-sbt-files-view-list-<?php echo $timestamp; ?>");
				var currentType = typeList.options[typeList.selectedIndex].value;
		
				var actionTemplate = dom.byId("actionTemplate-<?php echo $timestamp; ?>").textContent;
				var viewTemplate = dom.byId("viewTemplate-<?php echo $timestamp; ?>").textContent;
				var moveToTrashTemplate = dom.byId("moveToTrashTemplate-<?php echo $timestamp; ?>").textContent;
				var uploadFileTemplate = dom.byId("uploadFileTemplate-<?php echo $timestamp; ?>").textContent;
				var addTagsTemplate = dom.byId("addTagsTemplate-<?php echo $timestamp; ?>").textContent;
				var shareFilesTemplate = dom.byId("shareFilesTemplate-<?php echo $timestamp; ?>").textContent;
				var dialogTemplate = dom.byId("dialogTemplate-<?php echo $timestamp; ?>").textContent;
				

			    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;
			    domNode = dom.byId("filesViewRow-<?php echo $timestamp; ?>");
			    var FileRow = domNode.text || domNode.textContent;
			
				var filesView = new FilesView({
					gridArgs: {
						 type : currentType,
				    	 endpoint: "<?php echo $this->config->endpoint; ?>",
				         hidePager: false,
				         hideSorter: true,
				         hideFooter: false,
				    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 	 
					}, 
			 		hideActionBar : false,
					templateString: viewTemplate,
					endpoint: "<?php echo $this->config->endpoint; ?>",
					moveToTrashArgs: {templateString:moveToTrashTemplate, endpoint: "<?php echo $this->config->endpoint; ?>"},
					shareFileArgs: {templateString:shareFilesTemplate, endpoint: "<?php echo $this->config->endpoint; ?>"},
					uploadFileArgs: {templateString:uploadFileTemplate, endpoint: "<?php echo $this->config->endpoint; ?>"},
					addTagsArgs: {templateString:addTagsTemplate, endpoint: "<?php echo $this->config->endpoint; ?>"},
					dialogArgs:{templateString:dialogTemplate},
					actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
				});

			    filesView.grid.renderer.tableClass = "table";
			    var gridTemplate = dom.byId("filesViewRow-<?php echo $timestamp; ?>").textContent;
			    filesView.grid.renderer.template = gridTemplate;

			    filesView.grid.renderer.pagerTemplate = PagingHeader;
			    filesView.grid.renderer.footerTemplate = PagingFooter;
			    dom.byId("<?php echo $this->config->elementID; ?>").innerHTML = "";
			    dom.byId("<?php echo $this->config->elementID; ?>").appendChild(filesView.domNode);
			}
		);
}
onFilesViewTypeChange<?php echo $timestamp; ?>();
</script>
