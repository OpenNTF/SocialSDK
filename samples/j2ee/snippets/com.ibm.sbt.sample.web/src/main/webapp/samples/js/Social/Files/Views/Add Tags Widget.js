require(["sbt/declare", "sbt/config", "sbt/dom", 
         "sbt/connections/FileService", 
         "sbt/connections/controls/files/AddTagsWidget"], 
	function(declare, config, dom, FileService, AddTagsWidget) {
    	var endpoint = config.findEndpoint("connections");
    	if (!endpoint.isAuthenticated) {
    		endpoint.authenticate();
    		return;
    	}
    	
    	var ps = "%{name=fileCount|value=1}";
    	var fileService = new FileService();
    	fileService.getMyFiles({ ps:ps }).then(
    		function(files) {
    		    var addTagsWidget = new AddTagsWidget({ 
    		    	hideButtons: false,
    		    	files: files,
    		    	displayMessage : function(template, isError) {
    					displayMessage(dom, template, isError);
    				}
    		    });
    		    
    		    dom.byId("widgetDiv").appendChild(addTagsWidget.domNode);
    		}
    	);
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
