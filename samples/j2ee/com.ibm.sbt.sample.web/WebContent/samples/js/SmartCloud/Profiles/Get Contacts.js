require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/Endpoint" ], function(ProfileService, dom, Endpoint) {

	var endpoint = Endpoint.find("smartcloud");
	var url = "/manage/oauth/getUserIdentity";
	endpoint.request(url, {
		handleAs : "json"
	}).then(function(response) {
		handleLoggedIn(response);
	}, function(error) {
		handleError(error);
	});
	dom.setText("success", "Please wait... Loading your Contact List");

	function handleLoggedIn(entry) {
		var profileDisplayName = entry.name;
		var profileService = new ProfileService();
		profileService.getMyContacts().then(function(profiles) {
			for(var i=0; i<profiles.length; i++){
                var profile = profiles[i];
                createRow(i);
                console.log("Name"+i+" = "+profile.getDisplayName());
                dom.setText("Name"+i, profile.getDisplayName()); 
                dom.setText("Id"+i, profile.getId()); 
            }
			displayMessage("Successfully loaded Contact List for " + profileDisplayName);
		}, function(error) {
			handleError(error);
		});
	}

	 var createRow = function(i) {
         var table = dom.byId("ProfileContactsTable");
         var tr = document.createElement("tr");
         table.appendChild(tr);
         var td = document.createElement("td");
         td.setAttribute("Id", "Name"+i);
         tr.appendChild(td);
         td = document.createElement("td");
         td.setAttribute("Id", "Id"+i);
         tr.appendChild(td);
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