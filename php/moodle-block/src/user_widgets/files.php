***REMOVED*** 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>
<select id="ibm-sbt-files-list-***REMOVED*** echo $timestamp; ?>" onchange="onTypeChange***REMOVED*** echo $timestamp; ?>();">
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
function onTypeChange***REMOVED*** echo $timestamp; ?>() {
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
			function(dom, FileGrid) {
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
		});
}


onTypeChangeChange***REMOVED*** echo $timestamp; ?>();
</script>