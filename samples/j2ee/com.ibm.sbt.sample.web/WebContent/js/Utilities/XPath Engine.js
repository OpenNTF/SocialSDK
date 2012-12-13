require(["sbt/xml","sbt/xpath"],function(xml,xpath) {

	var result = "";
	var doc = xml.parse("<a><b>I'm B</b><c>I'm C</c></a>");
	
    result += xpath.selectText(doc,"/a/c");

	dojo.byId("content").appendChild(dojo.doc.createTextNode(result));
});