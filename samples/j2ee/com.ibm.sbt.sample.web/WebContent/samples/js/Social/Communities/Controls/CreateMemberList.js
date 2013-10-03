require(["sbt/dom", 
         "sbt/connections/controls/profiles/ProfileGrid",
         "sbt/connections/controls/bootstrap/ProfileRendererMixin",
         "sbt/connections/CommunityService",
         "sbt/lang",
         "sbt/connections/controls/search/SearchBox",
         "sbt/connections/ProfileService"], 

function(dom, ProfileGrid, ProfileRendererMixin, CommunityService, lang, SearchBox, ProfileService) {
    // Search logic
	var searchBox = new SearchBox({
		type:"full",
       	searchSuggest: "on",
       	memberList: true
	});
	
	dom.byId("searchBox").appendChild(searchBox.domNode);
       
	searchBox.domNode.addEventListener("searchResultEvent",function(event) {
		if(!event){
			event = window.event;
		}
		var resultDiv = dom.byId("results");
		resultDiv.innerHTML = "";
		//Create a table to display results
		var table = document.createElement("table");
		if(event.results.length >0){
			for(var i=0;i<event.results.length;i++){
				var title = event.results[i].getTitle();
				var row = document.createElement("tr");
				var data = document.createElement("td");
				row.innerHTML = title;
				row.appendChild(data);
				table.appendChild(row);
			}
		} else {
			var row = document.createElement("tr");
			var data = document.createElement("td");
			row.innerHTML = "Your Search Returned No Results";
			row.appendChild(data);
			table.appendChild(row);
		}
       	
		resultDiv.appendChild(table);
       	
	},false);
	
	// Community service logic
	var communityService = new CommunityService(); 
    
    dom.byId("btnCreateCommunity").onclick = function(evt) {
        
        var title = document.getElementById("titleTextField").value;
    
        if(!title || !title.length > 0){
        	document.getElementById("titleError").innerHTML = "You must enter a title for your community";
        	return;
        } else {
        	document.getElementById("titleError").innerHTML = "";
        }
        
        var content = document.getElementById("contentTextField").value;
        if(!content || !content.length > 0){
        	document.getElementById("contentError").innerHTML = "You must specify your community content";
        	return;
        } else {
        	document.getElementById("contentError").innerHTML = "";
        }
        
        var tags = document.getElementById("tagsTextField").value;
        if(!tags || !tags.length > 0){
        	document.getElementById("tagsError").innerHTML = "You must specify some tags for your community";
        	return;
        } else {
        	document.getElementById("tagsError").innerHTML = "";
        }
        
        // Create a new community and configure it
        var community = communityService.newCommunity();
        community.setTitle(title);
        community.setContent(content);	
        community.setTags(tags);
        
        // Add members to the community
        for (var i = 0; i < searchBox._members.length; i++) {
        	var member = searchBox._members[i];
        	
        	// Get the member's email address
            var profileService = new ProfileService();
            var query = { userid : member.id };
            var promise = profileService.search(query);

            promise.then(
            		function(profiles) {
            			for(var i = 0; i < profiles.length; i++) {
            				var profile = profiles[i];	           
            				var email = profile.getEmail();	 
            				alert("about to add " + email);
                    	    community.addMember({ id: email }, {});
       	             	}
            		},
            		
                    function(error) {
                    	alert("ERROR: " + error);
                    }
            );
        }
        
        communityService.createCommunity(community, {});
    };
   
});