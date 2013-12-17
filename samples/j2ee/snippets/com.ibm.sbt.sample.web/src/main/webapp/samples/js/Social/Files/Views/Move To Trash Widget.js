require(["sbt/declare", "sbt/config", "sbt/dom", "sbt/connections/controls/files/MoveToTrashWidget"], 
	function(declare, config, dom, MoveToTrashWidget) {
    	var endpoint = config.findEndpoint("connections");
    	if (!endpoint.isAuthenticated) {
    		endpoint.authenticate();
    		return;
    	}
    	
	    var moveToTrashWidget = new MoveToTrashWidget({ 
	    	hideButtons: false,
			onError : function(error) {
				displayMessage(dom, "Error moving files to trash: "+error, true);
			},
			onSuccess : function(files) {
				var msg = "Successfully moved files to trash: "+files; 
				displayMessage(dom, msg, false);
			},
			onCancel : function(forum) {
				displayMessage(dom, "Operation has been cancelled", false);
			}
	    });
	    
	    dom.byId("widgetDiv").appendChild(moveToTrashWidget.domNode);
	}
);

function displayMessage(dom, msg, isError) {
	var alertDiv = dom.byId("alertDiv");
    if (msg) {
        dom.setText(alertDiv, msg); 
        dom.removeClass(alertDiv, isError ? "lotusSuccess" : "lotusError");
    	dom.addClass(alertDiv, isError ? "lotusError" : "lotusSuccess");
        alertDiv.style.display = "";
    } else {
    	alertDiv.style.display = "none";
    }
}
