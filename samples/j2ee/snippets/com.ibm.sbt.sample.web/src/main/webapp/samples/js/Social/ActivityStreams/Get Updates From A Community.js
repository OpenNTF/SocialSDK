require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
        var createRow = function(i) {
            var table = dom.byId("activityStreamTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.setAttribute("id", "actor"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "type"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "text"+i);
            tr.appendChild(td);
        };
        var communityID = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
    	var activityStreamService = new ActivityStreamService();
    	var promise = activityStreamService.getUpdatesFromCommunity(
    		communityID,
			{
				count: 5
			}
    	);
    	promise.then(
            function(as){
                if (as.length == 0) {
                    dom.setText("content", "No Results for this Stream.");
                } else {
                    for(var i=0; i<as.length; i++){
                        var ase = as[i];
                        createRow(i);
                        dom.setText("actor"+i, ase.getActorDisplayName());
                        dom.setText("type"+i, ase.getVerb());
                        dom.setText("text"+i, ase.getPlainTitle());
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);