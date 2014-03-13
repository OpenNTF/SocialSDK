require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService, dom, config) {

	var endpoint = config.findEndpoint("connections");
	var url = "/connections/opensocial/basic/rest/people/@me/";
	endpoint.request(url, { handleAs : "json" }).then(
		function(response) {
			var userid = parseUserid(entry);
			handleLoggedIn(userid, entry.displayName);
		}, 
		function(error) {
			url = "/manage/oauth/getUserIdentity";

			endpoint.request(url, { handleAs : "json" }).then(
				function(response) {
					handleLoggedIn(response.subscriberid, response.name);
				}, 
				function(error) {
					handleError(error);
				}
			);
		}
	);
	dom.setText("success", "Please wait... Loading your profile entry");

	function handleLoggedIn(userid, displayName) {
		var profileService = new ProfileService();
		profileService.getProfile(userid).then(
			function(profile) {
				showValue("userId", profile.getUserid());
				showValue("name", profile.getName());
				showValue("email", profile.getEmail());
				showValue("groupwareMail", profile.getGroupwareMail());
				showValue("thumbnailUrl", profile.getThumbnailUrl());
				showValue("jobTitle", profile.getJobTitle());
				showValue("telephoneNumber", profile.getTelephoneNumber());
				showValue("profileUrl", profile.getProfileUrl());
				showValue("building", profile.getBuilding());
				showValue("floor", profile.getFloor());
				showValue("pronunciationUrl", profile.getPronunciationUrl());
				showValue("summary", profile.getSummary());
				showValue("department", profile.getDepartment());
				displayMessage("Successfully loaded profile entry for " + displayName);
			}, 
			function(error) {
				handleError(error);
			}
		);
	}
	
	function showValue(id, value) {
		if (value) {
			dom.byId(id).parentNode.style.display = "";
			dom.setText(id, getAttributeValue(value));
		} 
	}

	function getAttributeValue(value) {
		if (value) {
			return value;
		} else {
			return "";
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