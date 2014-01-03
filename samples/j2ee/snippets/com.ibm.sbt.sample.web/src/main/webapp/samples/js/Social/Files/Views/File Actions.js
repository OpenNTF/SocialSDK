require([ "sbt/declare", "sbt/dom", "sbt/lang", "sbt/controls/view/ActionBar", 
          "sbt/connections/controls/files/UploadFileAction", 
          "sbt/connections/controls/files/NewFolderAction" ], 
	function(declare, dom, lang, ActionBar, UploadFileAction, NewFolderAction) {

		var overrides = {
			displayMessage : function(template, isError) {
				displayMessage(dom, template, false);
			}
		};
	
		var actionBar = new ActionBar();
	    actionBar.addAction(lang.mixin(new UploadFileAction(), overrides));
	    actionBar.addAction(lang.mixin(new NewFolderAction(), overrides));
	    
	    dom.byId("actionBarDiv").appendChild(actionBar.domNode);
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

