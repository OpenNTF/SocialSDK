require(["sbt/dom","sbt/connections/core","sbt/xml","sbt/xpath"], function(dom,conn,xml,xpath) {
    var ep = sbt.Endpoints['connections'];
    ep.xhrGet({
        serviceUrl : "/dogear/atom",
        handleAs : "text",
        load: function(response) {
      		var doc = xml.parse(response);
			var link = xpath.selectText(doc,"/a:feed//a:link/@href",conn.namespaces);
			console.log(link);
			var a = document.createElement("a");
			a.setAttribute("href", link);
			a.appendChild(dom.createTextNode(link));
      		dom.byId("content").appendChild(dom.createTextNode("Link: "));
      		dom.byId("content").appendChild(a);
		},
		error: function(error){
			dom.byId("content").appendChild(dom.createTextNode("Error:"+error));
		}
    });
});