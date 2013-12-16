require(["sbt/declare", "sbt/config", "sbt/dom", "sbt/connections/controls/files/NewFolderWidget"], 
	function(declare, config, dom, NewFolderWidget) {
    	var endpoint = config.findEndpoint("connections");
    	if (!endpoint.isAuthenticated) {
    		endpoint.authenticate();
    		return;
    	}
    	
	    var uploadFilesWidget = new NewFolderWidget({ 
	    	hideButtons: false,
	    	displayMessage : function(message, isError) {
				displayMessage(dom, message, isError);
			}
	    });
	    
	    dom.byId("widgetDiv").appendChild(uploadFilesWidget.domNode);
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
