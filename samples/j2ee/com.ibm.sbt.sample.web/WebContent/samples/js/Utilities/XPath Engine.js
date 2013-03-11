require(["sbt/dom","sbt/xml","sbt/xpath"],function(dom,xml,xpath) {

	var result = "";
	var doc = xml.parse("<a><b>I'm B</b><c>I'm C</c></a>");
	
    result += xpath.selectText(doc,"/a/c");

	dom.byId("content").appendChild(dom.createTextNode(result));
});