require(["sbt/connections/ActivityStreamService","sbt/dom"], function(ActivityStreamService, dom) {
	dom.byId("loading").style.visibility = "visible"; 
	var activityStreamService = new ActivityStreamService();
	activityStreamService.getResponsesToMyContent({
		load : function(as) {
			var aEntries = as.getEntries();//get array of all activityEntry Objects
			if(aEntries.length == 0){
				dom.setText("content","No Results");
			}
			for(var i=0; i<aEntries.length; i++){
				var thisEntry = aEntries[i];
				var liElement = document.createElement("li");
				liElement.setAttribute("id", "li"+i);
				document.getElementById("content").appendChild(liElement);
				dom.setText("li"+i,thisEntry.connections.plainTitle);
			}
			dom.byId("loading").style.visibility = "hidden"; 
		},
		error : function(error){
	        dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
	        dom.byId("loading").style.visibility = "hidden"; 
		}
		}
	);
});
