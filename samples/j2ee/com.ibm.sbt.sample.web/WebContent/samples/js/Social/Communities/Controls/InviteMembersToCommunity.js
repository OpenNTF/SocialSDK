var currentCommunity = null;
var currentMembers = null;

// Community ID
var communityId = null;

// List of potential members. Populated by using the search box
var potentialMembers = [];

require(["sbt/config", 
         "sbt/connections/CommunityService", 
         "sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/controls/search/SearchBox"], 
         
function(config, CommunityService, dom, stringUtil, SearchBox) {
    
	 // Setup search
	var searchBox = new SearchBox({
	type:"full",
	        searchSuggest: "on",
	        memberList: true
	});

	dom.byId("searchBox").appendChild(searchBox.domNode);
	       
	searchBox.domNode.addEventListener("searchResultEvent",function(event) {
		if(!event) {
			event = window.event;
		}
		// Component for displaying search results
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
			row.innerHTML = "Your search returned no results";
			row.appendChild(data);
			table.appendChild(row);
		}
	       
		resultDiv.appendChild(table);
	       
	},false);
	
	var endpoint = config.findEndpoint("connections");
    var url = "/connections/opensocial/basic/rest/people/@me/";
    if (stringUtil.startsWith(endpoint.proxyPath, "smartcloud")) {
        url = "/manage/oauth/getUserIdentity";
    }
    endpoint.request(url, {handleAs: "json"}).then(
        function(response) {
            handleLoggedIn(response.entry, CommunityService, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
    dom.setText("success", "Please wait... Loading your profile entry");
    
    // Invitation action listener
    dom.byId("btnInvite").onclick = function(evt) {
    	// Community service logic
        var communityService = new CommunityService(); 
        
        var invitationString = "";
        
        // Invite potential members to the community
        for (var i = 0; i < searchBox._members.length; i++) {
        	if (searchBox._members[i].id == null) {
        		continue;
        	}
        	communityService.createInvite(communityId, searchBox._members[i].id);
            invitationString += searchBox._members[i].name + ", ";
        }
        
        invitationString = invitationString.substring(0, invitationString.length - 3);
        
        // Display success message and hide input form
        dom.byId("success").innerHTML = "Successfully invited " + invitationString + " to your community";
        dom.byId("invitationForm").style.display = "none";
    }
});


function loadCommunity(communityService, dom) {
    currentCommunity = null;
    communityService.getMyCommunities({ ps: 1 }).then(
        function(communities) {
            handleCommunitiesLoaded(communities, dom);
        },
        function(error) {
            handleError(dom, error);
        }       
    );
}

function loadMembers(community, dom) {
    currentMembers = null;
    community.getMembers().then(
        function(members) {
            handleMembersLoaded(members, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
    
    displayMessage(dom, "Please wait... Loading members for : " + community.getCommunityUuid());
}

function removeMembers(community, members, dom) {
    for (var i=0; i<members.length-1; i++) {
        community.removeMember(members[i].getUserid());
    }

    // wait for the last remove before reloading
    community.removeMember(members[members.length-1].getUserid()).then(
        function(memberId) {
            loadMembers(community, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function addMember(community, email, dom) {
    var input = dom.byId("memberEmail");
    community.addMember(email).then(       
        function(member){
            loadMembers(community, dom);
        },
        function(error){
            handleError(dom, error);
        }
    );
    
    displayMessage(dom, "Please wait... Adding member: " + email);
}

function handleLoggedIn(entry, CommunityService, dom) {
    var communityService = new CommunityService();
    loadCommunity(communityService, dom);

    addOnClickHandlers(communityService, dom);
    
    displayMessage(dom, "Please wait... Loading your latest community");
}

function handleCommunitiesLoaded(communities, dom) {
    if (!communities || communities.length == 0) {
        dom.byId("communityId").value = "";
        dom.byId("communityTitle").value = "";
        displayMessage(dom, "You are not a member of any communities."); 
        return;
    }
    
    dom.byId("communityTitle").value = communities[0].getTitle();
    communityId = communities[0].getCommunityUuid();
    dom.byId("communityId").value = communityId;
    
    currentCommunity = communities[0];
    
    loadMembers(communities[0], dom);
   
    displayMessage(dom, "Please wait... Loading community members for: " + communities[0].getCommunityUuid());
}

function handleMembersLoaded(members, dom) {
    currentMembers = members;
    
    displayMessage(dom, "Sucessfully loaded community members for: " + members[0].getCommunityUuid());
}

function getMember(id) {
    if (currentMembers) {
        for(var i = 0; i < currentMembers.length; i++) {
            if (id == currentMembers[i].getUserid()) {
                return currentMembers[i];
            }
        }
    }
}

function addOnClickHandlers(communityService, dom) {
    dom.byId("removeBtn").onclick = function(evt) {
        if (currentCommunity) {
            var select = dom.byId("membersList");
            var members = [];
            for(var i = 0; i < select.childNodes.length; i++) {
                var option = select.childNodes[i];
                if (option.selected) {
                    var member = getMember(option.value);
                    if (member) {
                        members.push(member);
                    }
                }
            }
            removeMembers(currentCommunity, members, dom);
        }
    };
    dom.byId("refreshBtn").onclick = function(evt) {
        if (currentCommunity) {
            loadMembers(currentCommunity, dom);
        }
    };
    dom.byId("addBtn").onclick = function(evt) {
        if (currentCommunity) {
            var input = dom.byId("memberEmail");
            addMember(currentCommunity, input.value, dom);
        }
    };
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