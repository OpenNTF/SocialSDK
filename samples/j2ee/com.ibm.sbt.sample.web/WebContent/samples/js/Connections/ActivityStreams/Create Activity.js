require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants","sbt/dom"], function(ActivityStreamService, ASConstants, dom) {
	dom.byId("loading").style.visibility = "visible"; 
	var actObj = {}; //creating postData
	var actor = actObj.actor = {};
	actor.id = ASConstants.ASUser.ME;
	var object = actObj.object = {};
	object.id = "objectid";
	object.displayName = "IBM software universe event";
	actObj.verb = ASConstants.Verb.INVITE;
	
	var activityStreamService = new ActivityStreamService();
	activityStreamService.postEntry({
		userType : ASConstants.ASUser.PUBLIC,
		groupType : ASConstants.ASGroup.ALL,
		applicationType : ASConstants.ASApplication.ALL,
		postData : actObj,
		load : function(as) {
		    dom.setText("content","Activity Stream Entry created successfully. ID: "+as.data.entry.id);
		    dom.byId("loading").style.visibility = "hidden"; 
		},
		error :  function(error){
		    dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
		    dom.byId("loading").style.visibility = "hidden"; 
		}
	});
});