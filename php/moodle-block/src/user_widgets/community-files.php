<div id="<?php echo $this->config->elementID;?>"></div>

<?php 
// Ensure that element IDs are unique
$milliseconds = microtime(true) * 1000; 
$timestamp = round($milliseconds);

global $CFG;
require $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-community-files.php';
?>

<script type="text/javascript">
var grid<?php echo $timestamp; ?>;

function onCommunityChange<?php echo $timestamp; ?>() {
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
		var communityList = dom.byId("ibm-sbt-communities-<?php echo $timestamp; ?>");
		var currentCommunity = communityList.options[communityList.selectedIndex].value;

		var domNode = dom.byId("fileRow-<?php echo $timestamp; ?>");

		var FileRow = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
	    var PagingFooter = domNode.text || domNode.textContent;
		
		grid<?php echo $timestamp; ?> = new FileGrid({
	         type : "communityFiles",
	         communityId: currentCommunity,
	         endpoint: "<?php echo $this->config->endpoint; ?>",
	         hidePager: false,
	         hideSorter: true,
	         hideFooter: false,
	    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       
	    });
		dom.byId("ibm-sbt-community-files-list-<?php echo $timestamp; ?>").innerHTML = "";
        dom.byId("ibm-sbt-community-files-list-<?php echo $timestamp; ?>").appendChild(grid<?php echo $timestamp; ?>.domNode);

        grid<?php echo $timestamp; ?>.renderer.tableClass = "table";
	    grid<?php echo $timestamp; ?>.renderer.template = FileRow;
        grid<?php echo $timestamp; ?>.update();
	});
}

function addOnClickHandlers<?php echo $timestamp; ?>(communityService, dom) {

	dom.byId("ibm-sbt-communities-file-upload-dialog-button-<?php echo $timestamp; ?>").onclick = function(evt) {
		var communityList = dom.byId("ibm-sbt-communities-<?php echo $timestamp; ?>");
		var currentCommunity = communityList.options[communityList.selectedIndex].value;
		var communityName = communityList.options[communityList.selectedIndex].innerHTML;
		if (currentCommunity) {
			uploadCommunityFile<?php echo $timestamp; ?>(communityService, currentCommunity, communityName, dom);
		}
	};
}

function handleLoggedIn<?php echo $timestamp; ?>(communityService, dom) {
	loadCommunity<?php echo $timestamp; ?>(communityService, dom);
	addOnClickHandlers<?php echo $timestamp; ?>(communityService, dom);
}

function uploadCommunityFile<?php echo $timestamp; ?>(communityService, communityId, communityName, dom) {
	displayMessage<?php echo $timestamp; ?>(dom, "Uploading...Please wait.");
	var img = dom.byId("ibm-sbt-loading-<?php echo $timestamp; ?>");
	img.style.display = "block";
	communityService.uploadCommunityFile("ibm-sbt-community-files-<?php echo $timestamp; ?>", communityId).then(
		function(community) {
			displayMessage<?php echo $timestamp; ?>(dom, "Community file uploaded successfuly to community : " + communityName);
			document.getElementById('ibm-sbt-communities-upload-dialog-<?php echo $timestamp; ?>').style.display='none';
			img.style.display = "none";
			grid<?php echo $timestamp; ?>.update(null);
		}, 
		function(error) {
			handleError<?php echo $timestamp; ?>(dom, error);
		}
	);
}

function displayMessage<?php echo $timestamp; ?>(dom, msg) {
	dom.setText("ibm-sbt-community-files-success-<?php echo $timestamp; ?>", msg);
	dom.byId("ibm-sbt-community-files-success-<?php echo $timestamp; ?>").style.display = "";
	dom.byId("ibm-sbt-community-files-error-<?php echo $timestamp; ?>").style.display = "none";
}

function handleError<?php echo $timestamp; ?>(dom, error) {
	dom.setText("ibm-sbt-community-files-error-<?php echo $timestamp; ?>", "Error: " + error.message);

	dom.byId("ibm-sbt-community-files-success-<?php echo $timestamp; ?>").style.display = "none";
	dom.byId("ibm-sbt-community-files-error-<?php echo $timestamp; ?>").style.display = "";
}

function clearError<?php echo $timestamp; ?>(dom) {
	dom.setText("ibm-sbt-community-files-error-<?php echo $timestamp; ?>", "");
	dom.byId("ibm-sbt-community-files-error-<?php echo $timestamp; ?>").style.display = "none";
}

function loadCommunity<?php echo $timestamp; ?>(communityService, dom) {
	communityService.getMyCommunities({ps: 10000}).then(function(communities) {
		var communityList = dom.byId("ibm-sbt-communities-<?php echo $timestamp; ?>");
		for (var i = 0; i < communities.length; i++) {
			var opt = document.createElement('option');
		    opt.value = communities[i].getCommunityUuid();
		    var title = communities[i].getTitle();
		    if (title.length > 20) {
			    title = title.substr(0, 16) + '...';
		    }
		    opt.innerHTML = title;
		    communityList.appendChild(opt);
		}
		onCommunityChange<?php echo $timestamp; ?>();
	}, function(error) {
		handleError<?php echo $timestamp; ?>(dom, error);
	});

}

require([ "sbt/connections/CommunityService", "sbt/dom", "sbt/config", ], function(CommunityService, dom, config) {
	var communityService = new CommunityService({endpoint: "<?php echo $this->config->endpoint; ?>"});
	// To make sure authentication happens before upload
	communityService.endpoint.authenticate().then(function() {
		handleLoggedIn<?php echo $timestamp; ?>(communityService, dom);
	});
});
</script>