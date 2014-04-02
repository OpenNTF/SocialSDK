***REMOVED*** 	
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-view.php';
?>

<select id="ibm-sbt-files-view-list-***REMOVED*** echo $timestamp; ?>" onchange="onFilesViewTypeChange***REMOVED*** echo $timestamp; ?>();">
	<option value="myFiles">My files</option>
	<option value="publicFiles">Public files</option>
	<option value="myPinnedFiles">My pinned files</option>
	<option value="myFolders">My folders</option>
	<option value="publicFolders">Public folders</option>
	<option value="myPinnedFolders">My pinned folders</option>
	<option value="activeFolders">Active folders</option>
	<option value="fileShares">File shares</option>
</select>
<div id="***REMOVED*** echo $this->config->elementID; ?>"></div>

<script type="text/javascript">
function onFilesViewTypeChange***REMOVED*** echo $timestamp; ?>() {
	require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView", "sbt/connections/controls/files/FileGrid"], 
			function(declare, dom, FilesView, FileGrid) {
				var typeList = dom.byId("ibm-sbt-files-view-list-***REMOVED*** echo $timestamp; ?>");
				var currentType = typeList.options[typeList.selectedIndex].value;
		
				var actionTemplate = dom.byId("actionTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var viewTemplate = dom.byId("viewTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var moveToTrashTemplate = dom.byId("moveToTrashTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var uploadFileTemplate = dom.byId("uploadFileTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var addTagsTemplate = dom.byId("addTagsTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var shareFilesTemplate = dom.byId("shareFilesTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				var dialogTemplate = dom.byId("dialogTemplate-***REMOVED*** echo $timestamp; ?>").textContent;
				

			    domNode = dom.byId("pagingHeader-***REMOVED*** echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingFooter-***REMOVED*** echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;
			    domNode = dom.byId("filesViewRow-***REMOVED*** echo $timestamp; ?>");
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
			    var gridTemplate = dom.byId("filesViewRow-***REMOVED*** echo $timestamp; ?>").textContent;
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
