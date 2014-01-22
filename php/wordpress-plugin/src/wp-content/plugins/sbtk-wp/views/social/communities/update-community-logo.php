<div id="error" style="display: none;" class="alert alert-error"></div>
<div id="success" style="" class="alert alert-success"></div>

<br>
<input id="your-files" accept="image/*" type="file">
<button class="btn btn-primary" id="updateBtn">Update</button>
<p>
</p><div class="form-horizontal">
	<legend>Community Logo</legend>
	<img alt="communityLogo" src="" id="logo">
	<div style="height: 8px"></div>
	<div id="note" class="alert alert-error">Note: This Sample will
		not work on browsers which do not support HTML 5 File API.</div>
</div>
<p></p>

<script type="text/javascript">
var currentCommunity = null;

function addOnClickHandlers(communityService, dom) {

	dom.byId("updateBtn").onclick = function(evt) {
		if (currentCommunity) {
			updateCommunityLogo(communityService, currentCommunity.getCommunityUuid(), dom);
		}
	};
}

function handleLoggedIn(communityService, dom) {
	loadCommunity(communityService, dom);

	addOnClickHandlers(communityService, dom);
}

function updateCommunityLogo(communityService, communityId, dom) {

	dom.byId("loading").style.visibility = "visible";
	dom.setText('status', '');

	// "your-files" is the ID of the HTML5 File Control. Refer to Update Community Logo.html
	communityService.updateCommunityLogo("your-files", communityId).then(function() {
		communityService.getCommunity(communityId).then(function(community) {
			var url = community.getLogoUrl();
			dom.byId("logo").src = url + "&amp;rand=" + Math.random();
			displayMessage(dom, "Community Logo updated successfuly for community ID: " + community.getCommunityUuid() + ", Title: " + community.getTitle());
			dom.byId("loading").style.visibility = "hidden";
		}, function(error) {
			handleError(dom, error);
			dom.byId("loading").style.visibility = "hidden";
		});
	}, function(error) {
		handleError(dom, error);
		dom.byId("loading").style.visibility = "hidden";
	});
}

function displayMessage(dom, msg) {
	dom.setText("success", msg);

	dom.byId("success").style.display = "";
	dom.byId("error").style.display = "none";
}

function handleError(dom, error) {
	dom.setText("error", "Error: " + error.message);

	dom.byId("success").style.display = "none";
	dom.byId("error").style.display = "";
}

function clearError(dom) {
	dom.setText("error", "");
	dom.byId("error").style.display = "none";
}

function loadCommunity(communityService, dom) {
	currentCommunity = null;

	communityService.getMyCommunities({
		ps : 1
	}).then(function(communities) {
		var community = (!communities || communities.length == 0) ? null : communities[0];
		if (community) {
			currentCommunity = community;
			displayMessage(dom, "Successfully loaded community ID: " + community.getCommunityUuid() + ", Title: " + community.getTitle());
		}
		else {
			displayMessage(dom, "No Communites found for user");
			dom.byId("updateBtn").disabled = "disabled";
		}
	}, function(error) {
		handleError(dom, error);
	});

}

require([ "sbt/connections/CommunityService", "sbt/dom", "sbt/config" ], function(CommunityService, dom, config) {
	var communityService = new CommunityService();
	// To make sure authentication happens before upload
	communityService.endpoint.authenticate().then(function() {
		handleLoggedIn(communityService, dom);
	});
});
</script>