var currentProfile = null;
var profileService = null;
var profileId = null;
var profileDisplayName = null;

require([ "sbt/config", "sbt/connections/ProfileService", "sbt/dom" ], function(config, ProfileService, dom) {

	var endpoint = config.findEndpoint("connections");
	var url = "/connections/opensocial/basic/rest/people/@me/";
	endpoint.request(url, {
		handleAs : "json"
	}).then(function(response) {
		handleLoggedIn(response.entry);
	}, function(error) {
		handleError(error);
	});
	dom.setText("success", "Please wait... Loading your profile entry");

	function handleLoggedIn(entry) {
		profileId = parseUserid(entry);
		profileDisplayName = entry.displayName;
		dom.setText("success", "Please wait... Loading the profile entry for " + profileDisplayName);
		profileService = new ProfileService();
		loadProfile(false);
	}

	function parseUserid(entry) {		
		return entry.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
	}
	function loadProfile(afterUpdate) {
		var promise = profileService.getProfile(profileId);
		try {
			promise.then(function(profile) {
				currentProfile = profile;
				loadProfileFields(afterUpdate);
			}, function(error) {
				handleError(error);
			});
		} catch (error) {
			handleError(error);
		}
	}

	function updateProfile(building, floor, jobTitle) {
		currentProfile.setBuilding(building);
		currentProfile.setFloor(floor);
		currentProfile.setJobTitle(jobTitle);

		currentProfile.update().then(function(profile) {
			loadProfile(true);			
		}, function(error) {
			handleError(error);
		});
		displayMessage("Please wait... Updating the profile entry for " + profileDisplayName);
	}

	function loadProfileFields(afterUpdate) {
		if (!currentProfile) {
			dom.byId("userId").value = "";
			dom.byId("building").value = "";
			dom.byId("floor").value = "";
			dom.byId("jobTitle").value = "";
			displayMessage("Unable to load profile.");
			return;
		}

		dom.byId("userId").value = currentProfile.getUserid();
		dom.byId("building").value = currentProfile.getBuilding();
		dom.byId("floor").value = currentProfile.getFloor();
		dom.byId("jobTitle").value = currentProfile.getJobTitle();
		if(afterUpdate){
			displayMessage("Successfully updated profile entry for " + profileDisplayName);
		}else{
			displayMessage("Successfully loaded profile entry for " + profileDisplayName);
		}
		addOnClickHandlers();
	}	

	function addOnClickHandlers() {
		dom.byId("loadBtn").onclick = function(evt) {
			loadProfile(false);
		};

		dom.byId("updateBtn").onclick = function(evt) {
			var building = dom.byId("building");
			var floor = dom.byId("floor");
			var jobTitle = dom.byId("jobTitle");
			updateProfile(building.value, floor.value, jobTitle.value);
		};
	}

	function displayMessage(msg) {
		dom.setText("success", msg);

		dom.byId("success").style.display = "";
		dom.byId("error").style.display = "none";
	}

	function handleError(error) {
		dom.setText("error", "Error: " + error.message);

		dom.byId("success").style.display = "none";
		dom.byId("error").style.display = "";
	}

	function clearError() {
		dom.setText("error", "");

		dom.byId("error").style.display = "none";
	}
});