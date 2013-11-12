require([ "sbt/smartcloud/ProfileService", "sbt/dom", "sbt/Endpoint" ], function(ProfileService, dom, Endpoint) {

	dom.setText("success", "Please wait... Loading your Contact List");

	var profileService = new ProfileService();
	profileService.getMyContacts().then(function(profiles) {
		for(var i=0; i<profiles.length; i++){
            var profile = profiles[i];
            createRow(i);
            dom.setText("name"+i, profile.getDisplayName()); 
            dom.setText("id"+i, profile.getId()); 
        }
		displayMessage("Successfully loaded Contact List ");
	}, function(error) {
		handleError(error);
	});

	var createRow = function(i) {
         var table = dom.byId("ProfileContactsTable");
         var tr = document.createElement("tr");
         table.appendChild(tr);
         var td = document.createElement("td");
         td.setAttribute("id", "name"+i);
         tr.appendChild(td);
         td = document.createElement("td");
         td.setAttribute("id", "id"+i);
         tr.appendChild(td);
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