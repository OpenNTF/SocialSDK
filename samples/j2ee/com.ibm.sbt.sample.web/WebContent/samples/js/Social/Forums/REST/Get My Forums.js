require(["sbt/connections/ConnectionsConstants","sbt/config","sbt/xml","sbt/xpath","sbt/dom"],function(conn,config,xml,xpath,dom) {
    var endpoint = config.findEndpoint("connections");
    
    var url = "/forums/atom/forums/my";
    
    var options = { 
        method : "GET", 
        handleAs : "text"
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
                displayStr = "You are not an owner of any forums.";
            }
      		dom.setText("content", displayStr);
        },
        function(error){
            dom.setText("content", error);
        }
    );
});