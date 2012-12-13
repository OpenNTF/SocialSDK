require(["sbt/xml"],function(xml) {

	var result = "";

	var doc = xml.parse("<a><b>I'm B</b><c>I'm C</c></a>");
	result = xml.asString(doc);

	dojo.byId("content").appendChild(dojo.doc.createTextNode(result));
});
