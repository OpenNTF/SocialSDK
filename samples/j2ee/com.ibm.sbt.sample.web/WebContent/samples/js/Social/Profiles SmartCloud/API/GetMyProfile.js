require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/config", "sbt/json" ], function(ProfileService, dom, config, json) {

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
		var profileService = new ProfileService();
		try {
		profileService.getProfile(profileId).then( 

	                function(profile){
	                	results = getResults(profile);
	                    dom.setText("json", json.jsonBeanStringify(results));
	                },
	                function(error){            	
	                    dom.setText("json", json.jsonBeanStringify(error));
	                }
	            );
        } catch(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    };

});


function getResults(profile) {
    return {
    	"getUserId" : profile.getId(),
        "getDisplayName" : profile.getDisplayName(),
        "getThumbnailUrl" : profile.getThumbnailUrl(),
        "getEmail" : profile.getEmail(),
        "getAddress" : profile.getAddress(),
        "getDepartment" : profile.getDepartment(),
        "getJobTitle" : profile.getJobTitle(),
        "getProfileUrl" : profile.getProfileUrl(),
        "getTelephoneNumber" : profile.getTelephoneNumber(),
        "getCountry" : profile.getCountry(),
        "getOrgId" : profile.getOrgId(),
        "getOrg" : profile.getOrg(),
        "getAbout" : profile.getAbout()
    };
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