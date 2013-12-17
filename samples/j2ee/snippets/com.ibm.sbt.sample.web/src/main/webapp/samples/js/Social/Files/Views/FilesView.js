require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView"], 
    function(declare, dom, FilesView) {
	    var filesView = new FilesView({type : "myFiles"});
	    dom.byId("filesViewDiv").appendChild(filesView.domNode);
	}
);
