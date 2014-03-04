require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
			var domNode = dom.byId("fileRow");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingHeader");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingFooter");
		    var PagingFooter = domNode.text || domNode.textContent;
		
		    var grid = new FileGrid({
		    	 type : "myFiles",
		         hidePager: false,
		         hideSorter: true,
		         hideFooter: false,
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}       	 
		    });

		    grid.renderer.tableClass = "table";
		    grid.renderer.template = FileRow;
		    
		    dom.byId("gridDiv").appendChild(grid.domNode);    
		    grid.update();
	});