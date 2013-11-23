var currentCommunity = null;
var currentInvite = null;

require(["sbt/config", "sbt/connections/CommunityService", "sbt/dom"], function(config, CommunityService, dom) {
    var communityService = new CommunityService();
    loadCommunity(communityService, dom);
    addOnClickHandlers(communityService, dom);    
});

function loadCommunity(communityService, dom) {
    displayMessage(dom, "Please wait... Loading your latest community");

    communityService.getMyCommunities({ ps: 1 }).then(
        function(communities) {
            var community = (!communities || communities.length == 0) ? null : communities[0];
            handleCommunityLoaded(community, dom);
            loadInvites(community, dom);
        },
        function(error) {
            handleError(dom, error);
        }       
    );
}

function loadInvites(community, dom) {
    community.getAllInvites().then(
        function(invites) {
        	showAllInvites(invites, dom);
        },
        function(error) {
            handleError(dom, error);
        }       
    );
}

function showAllInvites(invites, dom) {
    var tableBody = dom.byId("invitesTableBody");
    while (tableBody.childNodes[0]) {
        dom.destroy(tableBody.childNodes[0]);
    }
    
    for(var i=0; i<invites.length; i++){
        var invite = invites[i];
        createRow(invite, dom);
    }
}

function createRow(invite, dom) {
    var table = dom.byId("invitesTableBody");
    var tr = document.createElement("tr");
    table.appendChild(tr);
    var td = document.createElement("td");
    td.appendChild(document.createTextNode(invite.getTitle()));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(document.createTextNode(invite.getContributor().name));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(document.createTextNode(invite.getContributor().userid));
    tr.appendChild(td);
    td = document.createElement("td");
    td.appendChild(document.createTextNode(invite.getInviteeUuid()));
    tr.appendChild(td);
    td = document.createElement("td");
    var revokeBtn = document.createElement("button");
    revokeBtn.appendChild(document.createTextNode("Revoke Invite"));
    revokeBtn.setAttribute("class", "btn");
    td.appendChild(revokeBtn);
    tr.appendChild(td);
    
    revokeBtn.onclick = function(evt) {
    	displayMessage(dom, "Please wait... Revoking invite: " + invite.getTitle());
    	invite.remove().then(
	        function(inviteUuid) {
	        	displayMessage(dom, "Successfully revoked invite: " + invite.getTitle());
	        	
	        	currentInvite = null;
	        	handleInviteLoaded(null, dom);
	        	
	            loadInvites(currentCommunity, dom);
	        },
	        function(error) {
	            handleError(dom, error);
	        }       
    	);
    }
}

function createInvite(communityService, email, userid, dom) {
    displayMessage(dom, "Please wait... Creating invite: " + (userid || email));
    
    currentInvite = null;
    var invite = communityService.newInvite(); 
    invite.setCommunityUuid(currentCommunity.getCommunityUuid());
    // you need to provide a valid email or userid 
    invite.setEmail(email);
    invite.setUserid(userid);
    communityService.createInvite(invite).then(  
        function(invite) { 
        	invite.load().then(
                function(invite) { 
                    handleInviteCreated(invite, dom);
                },
                function(error) {
                    handleError(dom, error);
                }
            );
        	loadInvites(currentCommunity, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
}

function handleCommunityLoaded(community, dom) {
    if (!community) {
        dom.byId("communityTitle").value = "";
        return;
    }

    currentCommunity = community;
    dom.byId("communityTitle").value = community.getTitle();
   
    displayMessage(dom, "Successfully loaded community: " + community.getCommunityUuid());
}

function handleInviteLoaded(invite, dom) {
    if (!invite) {
        dom.byId("inviteId").value = "";
        dom.byId("inviteTitle").value = "";
        dom.byId("inviteEmail").value = "";
        dom.byId("inviteUserid").value = "";
        dom.byId("inviteeId").value = "";
        displayMessage(dom, "Unable to load invite."); 
        return;
    }
    
    dom.byId("inviteId").value = invite.getInviteUuid();
    dom.byId("inviteTitle").value = invite.getTitle();
    dom.byId("inviteEmail").value = invite.getEmail();
    dom.byId("inviteUserid").value = invite.getUserid();
    dom.byId("inviteeId").value = invite.getInviteeUuid();
    
    currentInvite = invite;

    displayMessage(dom, "Successfully loaded invite: " + invite.getInviteUuid());
}

function handleInviteCreated(invite, dom) {
    handleInviteLoaded(invite, dom);
    
    displayMessage(dom, "Successfully created invite: " + invite.getInviteUuid());
}

function addOnClickHandlers(communityService, dom) {
    dom.byId("refreshBtn").onclick = function(evt) {
        loadCommunity(communityService, dom);
        
        currentInvite = null;
        handleInviteLoaded(null, dom);
    };
    dom.byId("inviteBtn").onclick = function(evt) {
        dom.byId("inviteId").value = "";      
        var email = dom.byId("inviteEmail");        
        var userid = dom.byId("inviteUserid");        
        createInvite(communityService, email.value, userid.value, dom);
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
