require(["sbt/dom", "sbt/lang", "sbt/connections/controls/files/FileGrid", "sbt/connections/controls/bootstrap/FileRendererMixin"], function(dom,lang, FileGrid,FileRendererMixin ) {
        var grid = new FileGrid({
	         type : "myFiles",
	         pinFile: true
	    });
       
        lang.mixin(grid.renderer, FileRendererMixin);
        var domNode = dom.byId("fileRow");
        var CustomFileRow = domNode.text || domNode.textContent;
        grid.renderer.template = CustomFileRow;
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});