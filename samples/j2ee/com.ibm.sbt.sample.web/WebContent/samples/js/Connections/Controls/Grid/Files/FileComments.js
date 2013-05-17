require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
             type : "fileComments",
             userId : "%{sample.userId1}",
             fileId : "%{sample.userId1.fileId1}"
        });
                 
        dom.byId("gridDiv").appendChild(grid.domNode);
                 
        grid.update();
});