***REMOVED*** 	
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-view.php';
?>

<select id="ibm-sbt-files-view-list-***REMOVED*** echo $timestamp; ?>" onchange="onFilesViewTypeChange***REMOVED*** echo $timestamp; ?>();">
	<option value="myFiles">***REMOVED*** echo get_string('my_files', 'block_ibmsbt');?></option>
	<option value="publicFiles">***REMOVED*** echo get_string('public_files', 'block_ibmsbt');?></option>
	<option value="myPinnedFiles">***REMOVED*** echo get_string('my_pinned_files', 'block_ibmsbt');?></option>
	<option value="myFolders">***REMOVED*** echo get_string('my_folders', 'block_ibmsbt');?></option>
	<option value="publicFolders">***REMOVED*** echo get_string('public_folders', 'block_ibmsbt');?></option>
	<option value="myPinnedFolders">***REMOVED*** echo get_string('my_pinned_folders', 'block_ibmsbt');?></option>
	<option value="activeFolders">***REMOVED*** echo get_string('active_folders', 'block_ibmsbt');?></option>
</select>
<div id="***REMOVED*** echo $this->config->elementID; ?>"></div>

<script type="text/javascript">
function onFilesViewTypeChange***REMOVED*** echo $timestamp; ?>() {
	require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
			function(declare, dom, FilesView, FileGrid) {
				var typeList = dom.byId("ibm-sbt-files-view-list-***REMOVED*** echo $timestamp; ?>");
				var currentType = typeList.options[typeList.selectedIndex].value;
		
				var actionTemplate = dom.byId("filesViewActionTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var viewTemplate = dom.byId("filesViewViewTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var moveToTrashTemplate = dom.byId("filesViewMoveToTrashTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var uploadFileTemplate = dom.byId("filesViewUploadFileTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var addTagsTemplate = dom.byId("filesViewAddTagsTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var shareFilesTemplate = dom.byId("filesViewShareFilesTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				var dialogTemplate = dom.byId("filesViewDialogTemplate***REMOVED*** echo $timestamp; ?>").textContent;
				

			    domNode = dom.byId("filesViewPagingHeader***REMOVED*** echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("filesViewPagingFooter***REMOVED*** echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;
			    domNode = dom.byId("filesViewRow***REMOVED*** echo $timestamp; ?>");
			    var FileRow = domNode.text || domNode.textContent;
			
				var filesView = new FilesView({
					gridArgs: {
						 type : currentType,
				    	 endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
				         hidePager: false,
				         hideSorter: true,
				         hideFooter: false,
				    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 	 
					}, 
			 		hideActionBar : false,
					templateString: viewTemplate,
					endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
					moveToTrashArgs: {templateString:moveToTrashTemplate, endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"},
					shareFileArgs: {templateString:shareFilesTemplate, endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"},
					uploadFileArgs: {templateString:uploadFileTemplate, endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"},
					addTagsArgs: {templateString:addTagsTemplate, endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"},
					dialogArgs:{templateString:dialogTemplate},
					actionBarArgs: {actionTemplate:actionTemplate, disabledClass: "btn-disabled"}
				});

			    filesView.grid.renderer.tableClass = "table";
			    var gridTemplate = dom.byId("filesViewRow***REMOVED*** echo $timestamp; ?>").textContent;
			    filesView.grid.renderer.template = gridTemplate;

			    filesView.grid.renderer.pagerTemplate = PagingHeader;
			    filesView.grid.renderer.footerTemplate = PagingFooter;
			    dom.byId("***REMOVED*** echo $this->config->elementID; ?>").innerHTML = "";
			    dom.byId("***REMOVED*** echo $this->config->elementID; ?>").appendChild(filesView.domNode);
			}
		);
}
onFilesViewTypeChange***REMOVED*** echo $timestamp; ?>();
</script>
