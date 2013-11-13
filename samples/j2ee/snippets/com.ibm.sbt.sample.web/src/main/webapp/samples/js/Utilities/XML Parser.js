require(["sbt/dom", "sbt/xml"],function(dom, xml) {

	var result = "";

	var doc = xml.parse("<a><b>I'm B</b><c>I'm C</c></a>");
	result = xml.asString(doc);

	dom.byId("content").appendChild(dom.createTextNode(result));
});
