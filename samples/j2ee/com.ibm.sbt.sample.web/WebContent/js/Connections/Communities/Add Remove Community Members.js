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
            displayError(dom, error);
        }
    });
    displayMessage(dom, "Please wait... Loading your profile entry");
});

function loadCommunity(communityService, dom) {
    communityService.getMyCommunities({
        parameters: { ps: 1 },
        load: function(communities) {
            handleCommunitiesLoaded(communities, dom);
        },
        error: function(error) {
            displayError(dom, error);
        }       
    });
}

function loadMembers(community, dom) {
    community.getMembers({
        load: function(members) {
            handleMembersLoaded(members, dom);
        },
        error: function(error) {
            displayError(dom, error);
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
            displayError(dom, error);
        }
    });
    
    displayMessage(dom, "Please wait... Adding member: " + member.getEmail());
}

function handleLoggedIn(entry, CommunityService, dom) {
    dom.byId("entry").setUserData("entry", entry, function() {});
    
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
    dom.byId("communityId").setUserData("community", communities[0], function() {});
    
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
        node.setUserData("member", members[i], function() {});
    }
    
    displayMessage(dom, "Sucessfully loaded community members for: " + members[0].getCommunity().getCommunityUuid());
}

function addOnClickHandlers(communityService, dom) {
    dom.byId("removeBtn").onclick = function(evt) {
        var community = dom.byId("communityId").getUserData("community");
        if (community) {
            var select = dom.byId("membersList");
            var members = [];
            for(var i = 0; i < select.childNodes.length; i++) {
                var option = select.childNodes[i];
                if (option.selected) {
                    var member = option.getUserData("member");
                    members.push(member);
                }
            }
            removeMembers(community, members, dom);
        }
    };
    dom.byId("refreshBtn").onclick = function(evt) {
        var community = dom.byId("communityId").getUserData("community");
        if (community) {
            loadMembers(community, dom);
        }
    };
    dom.byId("addBtn").onclick = function(evt) {
        var community = dom.byId("communityId").getUserData("community");
        if (community) {
            var input = dom.byId("memberEmail");
            community.addMember({
                email: input.value,
                load : function(member){
                    getMembers(dom, community);
                },
                error : function(error){
                    displayError(dom, error);
                }
            });

            loadMembers(community, dom);
        }
    };
}

function displayMessage(dom, msg) {
    dom.setText("success", msg); 
    
    dom.byId("success").style.display = "";
    dom.byId("error").style.display = "none";
}

function displayError(dom, error) {
    dom.setText("error", "Error: " + error.message);
    
    dom.byId("success").style.display = "none";
    dom.byId("error").style.display = "";
}

function clearError(dom) {
    dom.setText("error", "");
    
    dom.byId("error").style.display = "none";
}
