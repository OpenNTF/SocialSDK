// Tags associated with this profile
var tagsList = [];

require(["sbt/dom",
         "sbt/connections/ProfileService",
         "sbt/connections/SearchService",
         "sbt/connections/ActivityStreamService"],
function(dom, ProfileService, SearchService, ActivityStreamService) {
		// TODO: Don't hardcode! EdBlanks@renovations.com
		var profileId = "04F26086-1A63-D244-4825-7A70002586FA";
		
		var profileService = new ProfileService();
		profileService.getProfile(profileId).then(function(profile) {
		dom.byId("profilePic").src = profile.getThumbnailUrl();
		dom.byId("name1").innerHTML = profile.getName();
		dom.byId("name2").innerHTML = profile.getName();
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
					dom.byId("searching").innerHTML = "";
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
            	
        a.innerHTML = result.getTitle();      
        
        tags.appendChild(span);      	
        span.appendChild(a);
    };

});

