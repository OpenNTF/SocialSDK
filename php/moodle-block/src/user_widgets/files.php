***REMOVED*** 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>
<select style="font-size: 12px;" id="ibm-sbt-files-list-***REMOVED*** echo $timestamp; ?>" onchange="onTypeChange***REMOVED*** echo $timestamp; ?>();">
	<option value="myFiles">My files</option>
	<option value="publicFiles">Public files</option>
	<option value="myPinnedFiles">My pinned files</option>
	<option value="myFolders">My folders</option>
	<option value="publicFolders">Public folders</option>
	<option value="myPinnedFolders">My pinned folders</option>
	<option value="activeFolders">Active folders</option>
	<option value="fileShares">File shares</option>
</select>

<div id="***REMOVED*** echo $this->config->elementID;?>"></div>
<script type="text/javascript">
function addOnClickHandlers(fileService,grid, dom) {

	dom.byId("ibm-sbt-upload-button-***REMOVED*** echo $timestamp; ?>").onclick = function(evt) {
		uploadFile(fileService, grid, dom);
	};
}

function initFileUploadControls(fileService, grid, dom) {
	addOnClickHandlers(fileService, grid, dom);
}

function uploadFile(fileService, grid, dom) {

	dom.byId("ibm-sbt-loading-***REMOVED*** echo $timestamp; ?>").style.display = "block";

	// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
	fileService.uploadFile("ibm-sbt-file-***REMOVED*** echo $timestamp; ?>", {
		// additional paramertes to add file metadata			
		visibility : "public"
	}).then(function(file) {
		displayMessage(dom, "File uploaded successfuly");
		grid.update(null);
		document.getElementById('ibm-sbt-upload-dialog-***REMOVED*** echo $timestamp; ?>').style.display='none';
		dom.byId("ibm-sbt-loading-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	}, function(error) {
		handleError(dom, error);
		dom.byId("ibm-sbt-loading-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	});
}

function displayMessage(dom, msg) {
	dom.setText("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>", msg);

	dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "";
	dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "none";
}

function handleError(dom, error) {
	dom.setText("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>", "Error: " + error.message);

	dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "";
}

function clearError(dom) {
	dom.setText("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>", "");

	dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "none";
}



function onTypeChange***REMOVED*** echo $timestamp; ?>() {
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid", "sbt/connections/FileService"], 
			function(dom, FileGrid, FileService) {
				var fileService = new FileService({endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"});
				
		
				var typeList = dom.byId("ibm-sbt-files-list-***REMOVED*** echo $timestamp; ?>");
				var currentType = typeList.options[typeList.selectedIndex].value;
		
				var domNode = dom.byId("fileRow-***REMOVED*** echo $timestamp; ?>");
				var FileRow = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingHeader-***REMOVED*** echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingFooter-***REMOVED*** echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;
			
			    var grid = new FileGrid({
			    	 type : currentType,
			    	 endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
			         hidePager: false,
			         hideSorter: true,
			         
			         hideFooter: false,
			    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
			    });

			    grid.renderer.tableClass = "table";
			    grid.renderer.template = FileRow;
			    dom.byId("***REMOVED*** echo $this->config->elementID;?>").innerHTML = "";
			    dom.byId("***REMOVED*** echo $this->config->elementID;?>").appendChild(grid.domNode);    
			    grid.update();
			    initFileUploadControls(fileService, grid, dom);
		});
}


onTypeChange***REMOVED*** echo $timestamp; ?>();
</script>