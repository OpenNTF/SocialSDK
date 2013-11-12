function addOnClickHandlers(profileService, profileId, dom) {

	dom.byId("updateBtn").onclick = function(evt) {
		updateProfilePhoto(profileService, profileId, dom);
	};
}

function handleLoggedIn(profileService, entry, dom) {
	dom.setText("success", "Profile Loaded");
	var profileId = parseUserid(entry);
	addOnClickHandlers(profileService, profileId, dom);
}

function parseUserid(entry) {
	return entry.id.substring("urn:lsid:lconn.ibm.com:profiles.person:".length);
}

function updateProfilePhoto(profileService, profileId, dom) {

	dom.byId("loading").style.visibility = "visible";
	dom.setText('status', '');

	// "your-files" is the ID of the HTML5 File Control. Refer to Update Profile Photo.html
	profileService.updateProfilePhoto("your-files", profileId).then(function() {		
		profileService.getProfile(profileId).then(function(profile) {
			var url =  profile.getThumbnailUrl();			
			dom.byId("image").src = url + "&rand=" + Math.random();
			displayMessage(dom, "Profile Photo updated successfuly");
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

require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/config" ], function(ProfileService, dom, config) {
	var profileService = new ProfileService();
	// To make sure authentication happens before upload 
	profileService.endpoint.authenticate().then(function() {
		//handleLoggedIn(profileService, dom);
		var url = "/connections/opensocial/basic/rest/people/@me/";
		profileService.endpoint.request(url, {
			handleAs : "json"
		}).then(function(response) {
			handleLoggedIn(profileService, response.entry, dom);
		}, function(error) {
			handleError(error);
		});
		dom.setText("success", "Please wait... Loading your profile entry");
	});
});


