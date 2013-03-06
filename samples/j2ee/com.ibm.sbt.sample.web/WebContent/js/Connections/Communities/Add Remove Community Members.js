require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
    var communityService = new CommunityService();
    communityService.getMyCommunities({
        parameters: { ps: 1 },
        load: function(communities) {
            if (!communities || communities.length == 0) {
                dom.setText("content", "You are not a member of any communities."); 
                return;
            }
            
            dom.setText("communityTitle", communities[0].getTitle());
            
            getMembers(dom, communities[0]);

            dom.byId("removeBtn").onclick = function(evt) {
                removeMembers(dom, communities[0]);
            };
            dom.byId("refreshBtn").onclick = function(evt) {
                getMembers(dom, communities[0]);
            };
            dom.byId("addBtn").onclick = function(evt) {
                addMember(dom, communities[0]);
            };
        },
        error: function(error) {
            displayError(dom, error);
        }       
    });
    
});

function getMembers(dom, community) {
    community.getMembers({
        load: function(members) {
            displayMembers(dom, members);
        },
        error: function(error) {
            displayError(dom, error);
        }
    });
}

function displayMembers(dom, members) {
    var select = dom.byId("membersList");
    while (select.childNodes[0]) {
        dom.destroy(select.childNodes[0]);
    }
    for(var i = 0; i < members.length; i++) {
        var node = dom.create("option", { value: members[i].getId(), innerHTML: members[i].getEmail() }, select);
        node.setUserData("member", members[i], function() {});
    }
    
}

function removeMembers(dom, community) {
    var select = dom.byId("membersList");
    for(var i = 0; i < select.childNodes.length; i++) {
        var option = select.childNodes[i];
        if (option.selected) {
            var member = option.getUserData("member");
            community.removeMember(member);
        }
    }
    
    getMembers(dom, community);
}

function addMember(dom, community) {
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
}

function displayError(dom, error) {
    dom.setText("content", "Error: " + error.message);
}
