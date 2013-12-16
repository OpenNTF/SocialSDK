require(["sbt/declare", "sbt/config", "sbt/dom", "sbt/connections/controls/files/UploadFileWidget"], 
	function(declare, config, dom, UploadFileWidget) {
    	var endpoint = config.findEndpoint("connections");
    	if (!endpoint.isAuthenticated) {
    		endpoint.authenticate();
    		return;
    	}
    	
	    var uploadFileWidget = new UploadFileWidget({ 
	    	hideButtons: false,
	    	displayMessage : function(template, isError) {
				displayMessage(dom, template, isError);
			}
	    });
	    
	    dom.byId("widgetDiv").appendChild(uploadFileWidget.domNode);
	}
);

function displayMessage(dom, template, isError) {
	var alertDiv = dom.byId("alertDiv");
    if (template) {
    	while (alertDiv.hasChildNodes()) {
    		alertDiv.removeChild(alertDiv.lastChild);
		}
        alertDiv.appendChild(dom.toDom(template, alertDiv.ownerDocument)); 
        dom.removeClass(alertDiv, isError ? "lotusSuccess" : "lotusError");
    	dom.addClass(alertDiv, isError ? "lotusError" : "lotusSuccess");
        alertDiv.style.display = "";
    } else {
    	alertDiv.style.display = "none";
    }
}
