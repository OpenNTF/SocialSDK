<?php 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>
<select style="font-size: 12px;" id="ibm-sbt-files-list-<?php echo $timestamp; ?>" onchange="onTypeChange<?php echo $timestamp; ?>();">
	<option value="myFiles">My files</option>
	<option value="publicFiles">Public files</option>
	<option value="myPinnedFiles">My pinned files</option>
	<option value="myFolders">My folders</option>
	<option value="publicFolders">Public folders</option>
	<option value="myPinnedFolders">My pinned folders</option>
	<option value="activeFolders">Active folders</option>
	<option value="fileShares">File shares</option>
</select>

<div id="<?php echo $this->config->elementID;?>"></div>
<script type="text/javascript">
function addOnClickHandlers(fileService,grid, dom) {

	dom.byId("ibm-sbt-upload-button-<?php echo $timestamp; ?>").onclick = function(evt) {
		uploadFile(fileService, grid, dom);
	};
}

function initFileUploadControls(fileService, grid, dom) {
	addOnClickHandlers(fileService, grid, dom);
}

function uploadFile(fileService, grid, dom) {

	dom.byId("ibm-sbt-loading-<?php echo $timestamp; ?>").style.display = "block";

	// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
	fileService.uploadFile("ibm-sbt-file-<?php echo $timestamp; ?>", {
		// additional paramertes to add file metadata			
		visibility : "public"
	}).then(function(file) {
		displayMessage(dom, "File uploaded successfuly");
		grid.update(null);
		document.getElementById('ibm-sbt-upload-dialog-<?php echo $timestamp; ?>').style.display='none';
		dom.byId("ibm-sbt-loading-<?php echo $timestamp; ?>").style.display = "none";
	}, function(error) {
		handleError(dom, error);
		dom.byId("ibm-sbt-loading-<?php echo $timestamp; ?>").style.display = "none";
	});
}

function displayMessage(dom, msg) {
	dom.setText("ibm-sbt-success-<?php echo $timestamp; ?>", msg);

	dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "";
	dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "none";
}

function handleError(dom, error) {
	dom.setText("ibm-sbt-error-<?php echo $timestamp; ?>", "Error: " + error.message);

	dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "none";
	dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "";
}

function clearError(dom) {
	dom.setText("ibm-sbt-error-<?php echo $timestamp; ?>", "");

	dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "none";
}



function onTypeChange<?php echo $timestamp; ?>() {
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid", "sbt/connections/FileService"], 
			function(dom, FileGrid, FileService) {
				var fileService = new FileService({endpoint: "<?php echo $this->config->endpoint; ?>"});
				
		
				var typeList = dom.byId("ibm-sbt-files-list-<?php echo $timestamp; ?>");
				var currentType = typeList.options[typeList.selectedIndex].value;
		
				var domNode = dom.byId("fileRow-<?php echo $timestamp; ?>");
				var FileRow = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;
			
			    var grid = new FileGrid({
			    	 type : currentType,
			    	 endpoint: "<?php echo $this->config->endpoint; ?>",
			         hidePager: false,
			         hideSorter: true,
			         
			         hideFooter: false,
			    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
			    });

			    grid.renderer.tableClass = "table";
			    grid.renderer.template = FileRow;
			    dom.byId("<?php echo $this->config->elementID;?>").innerHTML = "";
			    dom.byId("<?php echo $this->config->elementID;?>").appendChild(grid.domNode);    
			    grid.update();
			    initFileUploadControls(fileService, grid, dom);
		});
}


onTypeChange<?php echo $timestamp; ?>();
</script>