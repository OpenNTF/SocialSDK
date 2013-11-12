require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService, dom, config) {

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
		var profileId = parseUserid(entry);
		var profileDisplayName = entry.displayName;
		var profileService = new ProfileService();
		profileService.getProfile(profileId).then(function(profile) {
			dom.setText("userId", getAttributeValue(profile.getUserid()));
			dom.setText("name", getAttributeValue(profile.getName()));
			dom.setText("email", getAttributeValue(profile.getEmail()));
			dom.setText("groupwareMail", getAttributeValue(profile.getGroupwareMail()));
			dom.setText("thumbnailUrl", getAttributeValue(profile.getThumbnailUrl()));
			dom.setText("jobTitle", getAttributeValue(profile.getJobTitle()));
			dom.setText("telephoneNumber", getAttributeValue(profile.getTelephoneNumber()));
			dom.setText("profileUrl", getAttributeValue(profile.getProfileUrl()));
			dom.setText("building", getAttributeValue(profile.getBuilding()));
			dom.setText("floor", getAttributeValue(profile.getFloor()));
			dom.setText("pronunciationUrl", getAttributeValue(profile.getPronunciationUrl()));
			dom.setText("summary", getAttributeValue(profile.getSummary()));
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
		return entry.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
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