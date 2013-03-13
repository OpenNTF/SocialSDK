var currentCommunity = null;
var currentMembers = null;

require(["sbt/Endpoint", "sbt/connections/CommunityService", "sbt/dom"], function(Endpoint, CommunityService, dom) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl: "/connections/opensocial/basic/rest/people/@me/",
        handleAs: "json",
        loginUi: "dialog",
        load: function(response) {
            handleLoggedIn(response.entry, CommunityService, dom);
        },
        error: function(error) {
            handleError(dom, error);
        }
    });
    displayMessage(dom, "Please wait... Loading your profile entry");
});

function loadCommunity(communityService, dom) {
    currentCommunity = null;
    communityService.getMyCommunities({
        parameters: { ps: 1 },
        load: function(communities) {
            handleCommunitiesLoaded(communities, dom);
        },
        error: function(error) {
            handleError(dom, error);
        }       
    });
}

function loadMembers(community, dom) {
    currentMembers = null;
    community.getMembers({
        load: function(members) {
            handleMembersLoaded(members, dom);
        },
        error: function(error) {
            handleError(dom, error);
        }
    });
    
    displayMessage(dom, "Please wait... Loading members for : " + community.getCommunityUuid());
}

function removeMembers(community, members, dom) {
    for (var i=0; i<members.length; i++) {
        community.removeMember(members[i]);
    }
    
    loadMembers(community, dom);
}

function addMember(community, email, dom) {
    var input = dom.byId("memberEmail");
    community.addMember({
        email: email,
        load : function(member){
            loadMembers(community, dom);
        },
        error : function(error){
            handleError(dom, error);
        }
    });
    
    displayMessage(dom, "Please wait... Adding member: " + member.getEmail());
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
    dom.byId("communityId").value = communities[0].getCommunityUuid();
    
    currentCommunity = communities[0];
    
    loadMembers(communities[0], dom);
   
    displayMessage(dom, "Please wait... Loading community members for: " + communities[0].getCommunityUuid());
}

function handleMembersLoaded(members, dom) {
    var select = dom.byId("membersList");
    while (select.childNodes[0]) {
        dom.destroy(select.childNodes[0]);
    }
    for(var i = 0; i < members.length; i++) {
        var node = dom.create("option", { value: members[i].getId(), innerHTML: members[i].getEmail() }, select);
    }
    
    currentMembers = members;
    
    displayMessage(dom, "Sucessfully loaded community members for: " + members[0].getCommunity().getCommunityUuid());
}

function getMember(id) {
    if (currentMembers) {
        for(var i = 0; i < currentMembers.length; i++) {
            if (id == currentMembers[i].getId()) {
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
            currentCommunity.addMember({
                email: input.value,
                load : function(member){
                    getMembers(dom, community);
                },
                error : function(error){
                    handleError(dom, error);
                }
            });

            loadMembers(currentCommunity, dom);
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
