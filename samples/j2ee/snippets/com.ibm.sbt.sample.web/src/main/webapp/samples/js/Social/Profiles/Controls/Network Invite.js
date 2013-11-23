// Tags associated with this profile
var tagsList = [];

require(["sbt/dom",
         "sbt/connections/ProfileService",
         "sbt/connections/SearchService",
         "sbt/connections/ActivityStreamService"],
function(dom, ProfileService, SearchService, ActivityStreamService) {
		var profileId = "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}";
		
		var profileService = new ProfileService();
		profileService.getProfile(profileId).then(function(profile) {
		dom.byId("profilePic").src = profile.getThumbnailUrl();
		dom.setText("name1", profile.getName());
		dom.setText("name2", profile.getName());
	}, function(error) {
		alert(error);
	});
	
	// Search service for tags
	var searchService = new SearchService();
	
	var requestArgs = {
			scope : "communities"
	};
	
	dom.byId("btnSearch").onclick = function(evt) {
		var tag = dom.byId("tagInput").value;

		searchService.getResultsByTag(tag, requestArgs).then(
				function(results) {
					
					for(var i=0; i<results.length; i++){
						var result = results[i];
	                    createTag(result);
					}
	                dom.byId("communityTable").style.display = "";
	                
				},
				function(error) {
					dom.setText("searching", "");
					showError(error.message);
				}
			);
	}
	
	// Add action listener to invite button
	dom.byId("btnSendInvitation").onclick = function(evt) {
		var invitationText = dom.byId("message").value;
		
		// Create the invite
		var profileService = new ProfileService();
		profileService.createInvite(profileId, invitationText, {});
		
		// Check if the user wants to follow the contact
//		 getNotificationsForMe : function(args) {
		var follow = dom.byId("follow");
		if (follow.checked) {
			var activityStream = new ActivityStreamService();
		}
		
		// Hide invitation form
		var form = document.getElementById("invitationForm");
		form.style.display = "none";
		        
		// Display success message
		var msg = document.getElementById("confirmationMessage");
		msg.style.display = "block";
	};
	
	var createTag = function(result) {
        var tags = dom.byId("tags");

        var span = document.createElement("span");
        span.setAttribute("class", "lotusFilters");   
        
        span.onclick = function() {
        	var parent = span.parentNode;
        	parent.removeChild(span);
        };
        
        var a = document.createElement("a");      
        a.setAttribute("class", "lotusFilter");      
        a.href = "javascript:;";      	
            	
        dom.setText(a, result.getTitle());
        tags.appendChild(span);      	
        span.appendChild(a);
    };

});

