require(["sbt/connections/core","sbt/xml","sbt/xpath"],function(conn,xml,xpath) {

	var ep = sbt.Endpoints['connections'];
	ep.xhrGet({
		serviceUrl:	"/activities/service/atom2/activities",
		handleAs:	"text",
		content: {
			email:	"%{sample.email1}"
		},
		load: function(response) {
      		var doc = xml.parse(response);
			var name = xpath.selectText(doc,"/a:feed/a:entry/a:contributor/a:name",conn.namespaces);
			var email = xpath.selectText(doc,"/a:feed/a:entry/a:contributor/a:email",conn.namespaces);

      		dojo.byId("content").appendChild(dojo.doc.createTextNode("Name:"+name+", email:"+email));
		},
		error: function(error){
			dojo.byId("content").appendChild(dojo.doc.createTextNode("Error:"+error));
		}
	});
});