require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService, dom, config) {

	var endpoint = config.findEndpoint("smartcloud");
	var url = "/manage/oauth/getUserIdentity";
	endpoint.request(url, {
		handleAs : "json"
	}).then(function(response) {
		handleLoggedIn(response);
	}, function(error) {
		handleError(error);
	});
	dom.setText("success", "Please wait... Loading your profile entry");

	function handleLoggedIn(entry) {
		var profileId = parseUserid(entry);
		var profileDisplayName = entry.name;
		var profileService = new ProfileService();
		profileService.getProfileByGUID(profileId).then(function(profile) {
			dom.setText("userId", getAttributeValue(profile.getId()));
			dom.setText("name", getAttributeValue(profile.getDisplayName()));
			dom.setText("email", getAttributeValue(profile.getEmail()));
			dom.setText("thumbnailUrl", getAttributeValue(profile.getThumbnailUrl()));
			dom.setText("jobTitle", getAttributeValue(profile.getTitle()));
			dom.setText("telephoneNumber", getAttributeValue(profile.getPhoneNumber()));
			dom.setText("profileUrl", getAttributeValue(profile.getProfileUrl()));
			dom.setText("address", getAttributeValue(profile.getAddress()));
			dom.setText("department", getAttributeValue(profile.getDepartment()));
			displayMessage("Successfully loaded profile entry for " + profileDisplayName);
		}, function(error) {
			handleError(error);
		});
	}

	var getAttributeValue = function(value) {
		if (value) {
			return value;
		} else {
			return "No Result";
		}
	};

	function parseUserid(entry) {
		return entry.subscriberid;
	}

	function handleError(error) {
		dom.setText("error", "Error: " + error.message);

		dom.byId("success").style.display = "none";
		dom.byId("error").style.display = "";
	}

	function displayMessage(msg) {
		dom.setText("success", msg);

		dom.byId("success").style.display = "";
		dom.byId("error").style.display = "none";
	}

});