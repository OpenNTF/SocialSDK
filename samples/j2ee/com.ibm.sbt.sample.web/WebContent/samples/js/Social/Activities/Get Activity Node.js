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
        activitiesService.getActivityNode("4b014d80-f0f2-4ce6-b339-3fcfbeb6765b").then(
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
);