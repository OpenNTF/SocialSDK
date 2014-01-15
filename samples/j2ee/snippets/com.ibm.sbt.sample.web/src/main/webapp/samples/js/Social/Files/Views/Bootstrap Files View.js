require(["sbt/declare", "sbt/dom", "sbt/connections/controls/files/FilesView"], 
    function(declare, dom, FilesView) {
	    
		var actionTemplate = dom.byId("actionTemplate").textContent;
		var viewTemplate = dom.byId("viewTemplate").textContent;
		var moveToTrashTemplate = dom.byId("moveToTrashTemplate").textContent;
		var uploadFileTemplate = dom.byId("uploadFileTemplate").textContent;
		var addTagsTemplate = dom.byId("addTagsTemplate").textContent;
		var shareFilesTemplate = dom.byId("shareFilesTemplate").textContent;
		var dialogTemplate = dom.byId("dialogTemplate").textContent;
		
		var filesView = new FilesView({
			actionButtonTemplate: actionTemplate,
			filesViewTemplate: viewTemplate,
			
			moveToTrashArgs: {templateString : moveToTrashTemplate},
			shareFileArgs: {templateString: shareFilesTemplate},
			uploadFileArgs: {templateString: uploadFileTemplate},
			addTagsArgs: {templateString: addTagsTemplate},
			dialogArgs:{templateString: dialogTemplate},
			gridArgs: {type : "myFiles",hideSorter:true,hidePager:true},
			
			disabledActionClass: "btn-disabled"
		});
	
	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("gridTemplate").textContent;
	    filesView.grid.renderer.template = gridTemplate;
	    
	    dom.byId("filesViewDiv").appendChild(filesView.domNode);

	}
);
