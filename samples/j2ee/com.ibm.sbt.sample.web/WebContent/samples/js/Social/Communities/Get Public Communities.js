require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService, dom) {
	    var createRow = function(title, communityUuid) {
	        var table = dom.byId("communitiesTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.appendChild(dom.createTextNode(title));
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.appendChild(dom.createTextNode(communityUuid));
	        tr.appendChild(td);
	    };
    
    	var communityService = new CommunityService();
    	communityService.getPublicCommunities({ ps: 5 }).then(
            function(communities){
                if (communities.length == 0) {
                    text = "There are no public communities.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<communities.length; i++){
                        var community = communities[i];
                        var title = community.getTitle(); 
                        var communityUuid = community.getCommunityUuid(); 
                        createRow(title, communityUuid);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);