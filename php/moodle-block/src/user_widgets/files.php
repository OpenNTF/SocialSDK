<?php 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>
<select style="font-size: 12px;" id="ibm-sbt-files-list-<?php echo $timestamp; ?>" onchange="onTypeChange<?php echo $timestamp; ?>();">
	<option value="myFiles"><?php echo get_string('my_files', 'block_ibmsbt');?></option>
	<option value="publicFiles"><?php echo get_string('public_files', 'block_ibmsbt');?></option>
	<option value="myPinnedFiles"><?php echo get_string('my_pinned_files', 'block_ibmsbt');?></option>
	<option value="myFolders"><?php echo get_string('my_folders', 'block_ibmsbt');?></option>
	<option value="publicFolders"><?php echo get_string('public_folders', 'block_ibmsbt');?></option>
	<option value="myPinnedFolders"><?php echo get_string('my_pinned_folders', 'block_ibmsbt');?></option>
	<option value="activeFolders"><?php echo get_string('active_folders', 'block_ibmsbt');?></option>
	<option value="sharedWithMe"><?php echo get_string('files_shared_with_me', 'block_ibmsbt');?></option>
	<option value="sharedByMe"><?php echo get_string('files_shared_by_me', 'block_ibmsbt');?></option>
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

	var publicFile = dom.byId("ibm-sbt-file-privacy-public-<?php echo $timestamp; ?>");
	var privacy = 'private';
	if (publicFile.selected) {
		privacy = 'public';
	}
	
	fileService.uploadFile("ibm-sbt-file-<?php echo $timestamp; ?>", {
		// additional paramertes to add file metadata			
		visibility : privacy
	}).then(function(file) {
		displayMessage(dom, "<?php echo get_string('successful_upload', 'block_ibmsbt');?>");
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

				var domNode = null;

				if (currentType == 'myFolders' || currentType == 'publicFolders' || currentType == 'myPinnedFolders' || currentType == 'activeFolders') {
					domNode = dom.byId("folderRow-<?php echo $timestamp; ?>");
				} else {
					domNode = dom.byId("fileRow-<?php echo $timestamp; ?>");
				}
				var FileRow = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
			    var PagingHeader = domNode.text || domNode.textContent;
			    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
			    var PagingFooter = domNode.text || domNode.textContent;

				
			    var grid = null;

			    if (currentType == 'sharedWithMe') {
				    grid = new FileGrid({
				    	 type : 'fileShares',
				    	 direction: 'inbound',
				    	 endpoint: "<?php echo $this->config->endpoint; ?>",
				         hidePager: false,
				         hideSorter: true,
				         hideFooter: false,
				    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
				    });
			    } else if (currentType == 'sharedByMe') {
			    	grid = new FileGrid({
				    	 type : 'fileShares',
				    	 direction: 'outbound',
				    	 endpoint: "<?php echo $this->config->endpoint; ?>",
				         hidePager: false,
				         hideSorter: true,
				         hideFooter: false,
				    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
				    });
			    } else {
			    	grid = new FileGrid({
				    	 type : currentType,
				    	 endpoint: "<?php echo $this->config->endpoint; ?>",
				         hidePager: false,
				         hideSorter: true,
				         hideFooter: false,
				    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
				    });
			    }

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