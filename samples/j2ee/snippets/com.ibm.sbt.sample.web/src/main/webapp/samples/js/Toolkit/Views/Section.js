require(["sbt/dom", "sbt/controls/view/Section"], 
	function(dom, Section) {
	    var section = new Section({ title : "Sample Section" });
	    section.setContent("<h2>Sample Section Content<h2>");
	   
	    dom.byId("sectionDiv").appendChild(section.domNode);
	}
);
