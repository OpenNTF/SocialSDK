require(["sbt/dom", "sbt/lang", "sbt/connections/controls/profiles/ProfileGrid","sbt/connections/controls/bootstrap/ProfileRendererMixin"], function(dom, lang, ProfileGrid, ProfileRendererMixin) {
    var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{sample.email1}",
    });
    
    lang.mixin(grid.renderer, ProfileRendererMixin);
    var domNode = dom.byId("profileRow");
    var CustomProfileRow = domNode.text || domNode.textContent;
    grid.renderer.template = CustomProfileRow;
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
   
});