<?php 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>
<select id="ibm-sbt-files-list-<?php echo $timestamp; ?>" onchange="onTypeChange<?php echo $timestamp; ?>();">
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
function onTypeChange<?php echo $timestamp; ?>() {
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
			function(dom, FileGrid) {
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
		});
}


onTypeChange<?php echo $timestamp; ?>();
</script>