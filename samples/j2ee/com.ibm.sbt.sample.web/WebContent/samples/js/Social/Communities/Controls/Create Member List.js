require(["sbt/dom", 
         "sbt/connections/controls/profiles/ProfileGrid",
         "sbt/connections/controls/bootstrap/ProfileRendererMixin",
         "sbt/connections/CommunityService",
         "sbt/lang",
         "sbt/connections/controls/search/SearchBox"], 

function(dom, ProfileGrid, ProfileRendererMixin, CommunityService, lang, SearchBox) {
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
		dom.setText(resultDiv, "");
		//Create a table to display results
		var table = document.createElement("table");
		if(event.results.length >0){
			for(var i=0;i<event.results.length;i++){
				var title = event.results[i].getTitle();
				var row = document.createElement("tr");
				var data = document.createElement("td");
				dom.setText(row, title);
				row.appendChild(data);
				table.appendChild(row);
			}
		} else {
			var row = document.createElement("tr");
			var data = document.createElement("td");
			dom.setText(row, "Your Search Returned No Results");
			row.appendChild(data);
			table.appendChild(row);
		}
       	
		resultDiv.appendChild(table);
       	
	},false);
	
	// Community service logic
	var communityService = new CommunityService(); 
    
    dom.byId("btnCreateCommunity").onclick = function(evt) {
        
    	// Get the community title
        var title = document.getElementById("titleTextField").value;
    
        // The title is required. Make sure that the user entered one. If not, display an
        // error message and return
        if(!title || !title.length > 0){
        	dom.setText("titleError", "You must enter a title for your community");
        	return;
        } else {
        	dom.setText("titleError", "");
        }
        
        // Get community content
        var content = document.getElementById("contentTextField").value;
        
        // The content is required. Make sure that the user entered one. If not, display an
        // error message and return
        if(!content || !content.length > 0){
        	dom.setText("contentError", "You must specify your community content");
        	return;
        } else {
        	dom.setText("contentError", "");
        }
        
        // Get community tags
        var tags = document.getElementById("tagsTextField").value;
        
        // At least one tag is required. Make sure that the user entered one. If not, display an
        // error message and return
        if(!tags || !tags.length > 0){
        	dom.setText("tagsError", "You must specify some tags for your community");
        	return;
        } else {
        	dom.setText("tagsError", "");
        }
        
        // Create a new community and configure it
        var community = communityService.newCommunity();
        community.setTitle(title);
        community.setContent(content);	
        community.setTags(tags);
        
        // Hide creation form
        var msg = document.getElementById("communityCreationForm");
        msg.style.display = "none";
        
        // Load waiting dialog
        var msg = document.getElementById("communityWaitMessage");
        msg.style.display = "block";

 		 // Create the community
        communityService.createCommunity(community).then(  
        		function(community) { 
                    community.load().then(
                        function(community) { 
                        	// Get community ID
                            var communityUuid = community.getCommunityUuid();
                            
                            // Add community members
                            for (var i = 0; i < searchBox.members.length; i++) {
                            	var member = searchBox.members[i];
                            	communityService.addMember( communityUuid, member.id, {});
                            }
                            // Hide wait message
                            var msg = document.getElementById("communityWaitMessage");
                            msg.style.display = "none";
                            
                            // Display success message
                            var msg = document.getElementById("confirmationMessage");
                            msg.style.display = "block";
                        },
                        function(error) {
                            alert(error);
                        }
                    );
                },
                function(error) {
                    handleError(dom, error);
                }
            );
    };
   
});