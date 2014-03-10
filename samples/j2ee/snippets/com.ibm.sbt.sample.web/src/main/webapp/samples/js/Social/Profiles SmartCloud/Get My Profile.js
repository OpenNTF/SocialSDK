require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/json" ], function(ProfileService, dom, json) {

	var profileService = new ProfileService();
	profileService.getMyProfile().then(
		function(profile) {
			dom.setText("userId", getAttributeValue(profile.getId()));
			dom.setText("name", getAttributeValue(profile.getDisplayName()));
			dom.setText("email", getAttributeValue(profile.getEmail()));
			dom.setText("thumbnailUrl", getAttributeValue(profile.getThumbnailUrl()));
			dom.setText("jobTitle", getAttributeValue(profile.getJobTitle()));
			dom.setText("telephoneNumber", getAttributeValue(profile.getTelephoneNumber()));
			dom.setText("profileUrl", getAttributeValue(profile.getProfileUrl()));
			dom.setText("address", getAttributeValue(profile.getAddress()));
			dom.setText("department", getAttributeValue(profile.getDepartment()));
			displayMessage("Successfully loaded profile entry for " + profileDisplayName);
		}, 
		function(error) {
			handleError(error);
		}
	);

	var getAttributeValue = function(value) {
		if (value) {
			return value;
		} else {
			return "No Result";
		}
	};

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