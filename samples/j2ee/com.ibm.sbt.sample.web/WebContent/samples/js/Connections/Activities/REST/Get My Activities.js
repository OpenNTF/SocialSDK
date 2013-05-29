require(["sbt/connections/ConnectionsConstants","sbt/Endpoint","sbt/xml","sbt/xpath","sbt/dom"],function(conn,Endpoint,xml,xpath,dom) {
    var endpoint = Endpoint.find("connections");
    
    var url = "/activities/service/atom2/activities";
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
            email : "%{sample.email1}",
            ps : 5
        }
    };
    
    endpoint.request(url, options).then(
    	function(response) {
			var displayStr = "";
      		var activities = xpath.selectNodes(xml.parse(response), "/a:feed/a:entry", conn.Namespaces);      		
      		for(var count = 0; count < activities.length; count ++){	
      			var activityName = xpath.selectText(activities[count], "a:title", conn.Namespaces);
      			displayStr += activityName + ((count == activities.length -1) ?"  ":" , ");
      		}
      		if (displayStr.length == 0) {
                displayStr = "You are not a member of any activity.";
            }
      		dom.setText("content", displayStr);
        },
        function(error){
            dom.setText("content", error);
        }
    );
});