require(["sbt/connections/FollowService", "sbt/connections/FollowConstants", "sbt/dom"], 
    function(FollowService, consts, dom) {
	    var createRow = function(followedResource) {
	        var table = dom.byId("followedResourcesTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, followedResource.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, followedResource.getFollowedResourceUuid());
	        tr.appendChild(td);
	    };
    
    	var followService = new FollowService();
    	followService.getFollowedResources(consts.CommunitiesSource, consts.CommunitiesResourceType, { ps: 5 }).then(
            function(followedResources){
                if (followedResources.length == 0) {
                    text = "Followed Communities returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<followedResources.length; i++){
                        var followedResource = followedResources[i];
                        createRow(followedResource);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);