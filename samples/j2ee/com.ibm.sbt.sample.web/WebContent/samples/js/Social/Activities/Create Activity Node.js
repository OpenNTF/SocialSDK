require(["sbt/connections/ActivityService", "sbt/dom"], 
    function(ActivityService,dom) {
        var createRow = function(title, activityId) {
            var table = dom.byId("activitiesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = title;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = activityId;
            tr.appendChild(td);
        };

        var activitiesService = new ActivityService();
        var activityNode = activitiesService.newActivityNode({
        	"activityId" : "190f7f24-9ca6-44fc-8c6c-77afb6ff2fa3",
        	"title" : "Vineet Entry created from JS Sample", 
        	"type" : "Entry", 
        	"content" : "Some Content text"
        });
        activityNode.addTextField("TextFieldName","TextFieldSummary");
        activitiesService.createActivityNode("190f7f24-9ca6-44fc-8c6c-77afb6ff2fa3", activityNode).then(
            function(activityNode) {               
                  var title = activityNode.getTitle(); 
                  var activityNodeId = activityNode.getActivityNodeId(); 
                  createRow(title, activityNodeId);                  
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
)