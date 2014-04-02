<div id="***REMOVED*** echo $this->config->elementID;?>"></div>

***REMOVED*** 
global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-community-files.php';
?>

<script type="text/javascript">
var currentCommunity = null;

function addOnClickHandlers(communityService, dom) {

	dom.byId("uploadBtn").onclick = function(evt) {
		if (currentCommunity) {

			uploadCommunityFile(communityService, currentCommunity.getCommunityUuid(), dom);
		}
	};
}

function handleLoggedIn(communityService, dom) {
	loadCommunity(communityService, dom);

	addOnClickHandlers(communityService, dom);
}

function uploadCommunityFile(communityService, communityId, dom) {
	displayMessage(dom, "Uploading file to community: "+communityId);

	communityService.uploadCommunityFile("ibm-sbt-community-files", communityId).then(
		function(community) {
			displayMessage(dom, "Community file uploaded successfuly for community ID: " + communityId);
		}, 
		function(error) {
			handleError(dom, error);
		}
	);
}

function displayMessage(dom, msg) {
	dom.setText("ibm-sbt-community-files-success", msg);

	dom.byId("ibm-sbt-community-files-success").style.display = "";
	dom.byId("ibm-sbt-community-files-error").style.display = "none";
}

function handleError(dom, error) {
	dom.setText("ibm-sbt-community-files-error", "Error: " + error.message);

	dom.byId("ibm-sbt-community-files-success").style.display = "none";
	dom.byId("ibm-sbt-community-files-error").style.display = "";
}

function clearError(dom) {
	dom.setText("ibm-sbt-community-files-error", "");
	dom.byId("ibm-sbt-community-files-error").style.display = "none";
}

function loadCommunity(communityService, dom) {
	currentCommunity = null;

	communityService.getMyCommunities({
		ps : 1
	}).then(function(communities) {
		var community = (!communities || communities.length == 0) ? null : communities[0];
		if (community) {
			currentCommunity = community;
			dom.byId("communityUrl").href = currentCommunity.getCommunityUrl();
			displayMessage(dom, "Successfully loaded community ID: " + community.getCommunityUuid() + ", Title: " + community.getTitle());
		}
		else {
			displayMessage(dom, "No Communites found for user");
			dom.byId("uploadBtn").disabled = "disabled";
		}
	}, function(error) {
		handleError(dom, error);
	});

}

require([ "sbt/connections/CommunityService", "sbt/dom", "sbt/config" ], function(CommunityService, dom, config) {
	var communityService = new CommunityService({endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"});
	// To make sure authentication happens before upload
	communityService.endpoint.authenticate().then(function() {
		handleLoggedIn(communityService, dom);
	});
});
</script>