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
			template: viewTemplate,
			moveToTrashTemplate: moveToTrashTemplate,
			shareFilesTemplate:shareFilesTemplate,
			uploadFileTemplate: uploadFileTemplate,
			addTagsTemplate: addTagsTemplate,
			dialogTemplate:dialogTemplate,
			disabledActionClass: "btn-disabled",
			gridArgs: {type : "myFiles",hideSorter:true,hidePager:true}
		});
	
	    filesView.grid.renderer.tableClass = "table";
	    var gridTemplate = dom.byId("gridTemplate").textContent;
	    filesView.grid.renderer.template = gridTemplate;
	    
	    dom.byId("filesViewDiv").appendChild(filesView.domNode);

	}
);
