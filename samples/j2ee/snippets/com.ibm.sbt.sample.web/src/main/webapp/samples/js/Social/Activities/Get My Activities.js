require(["sbt/connections/ActivityService", "sbt/dom"], 
    function(ActivityService,dom) {
        var createRow = function(title, activityUuid) {
            var table = dom.byId("activitiesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(document.createTextNode(title));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(activityUuid));
            tr.appendChild(td);
        };

        var activityService = new ActivityService();
        activityService.getMyActivities().then(
            function(activities) {
                if (activities.length == 0) {
                	dom.setText("content", "You are not a member of any activities.");
                } else {
                    for(var i=0; i<activities.length; i++){
                        var activity = activities[i];
                        var title = activity.getTitle(); 
                        var activityUuid = activity.getActivityUuid(); 
                        createRow(title, activityUuid);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);