require(["sbt/connections/core","sbt/xml","sbt/xpath","sbt/dom"],function(conn,xml,xpath,dom) {

	var ep = sbt.Endpoints['connections'];
	ep.xhrGet({
		serviceUrl:	"/activities/service/atom2/activities",
		handleAs:	"text",
		content: {
			email:	"%{sample.email1}",
			ps: 5
		},
		load: function(response) { 
			var displayStr = "";
      		var activities = xpath.selectNodes(xml.parse(response), "/a:feed/a:entry", conn.namespaces);      		
      		for(var count = 0; count < activities.length; count ++){	
      			var activityName = xpath.selectText(activities[count], "a:title", conn.namespaces);
      			displayStr += activityName + ((count == activities.length -1) ?"  ":" , ");
      		}
      		if (displayStr.length == 0) {
                displayStr = "You are not a member of any activity.";
            }
      		dom.setText("content",displayStr);
		},
		error: function(error){
			dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		}
	});
});