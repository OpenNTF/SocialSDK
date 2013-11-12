require(["sbt/dom", "sbt/lang", "sbt/connections/controls/profiles/ProfileGrid","sbt/connections/controls/bootstrap/ProfileRendererMixin"], function(dom, lang, ProfileGrid, ProfileRendererMixin) {
    var grid = new ProfileGrid({
        type : "colleagues",
        userid : "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}"
    });
    
    lang.mixin(grid.renderer, ProfileRendererMixin);
    var domNode = dom.byId("profileRow");
    var CustomProfileRow = domNode.text || domNode.textContent;
    grid.renderer.template = CustomProfileRow;
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
   
});