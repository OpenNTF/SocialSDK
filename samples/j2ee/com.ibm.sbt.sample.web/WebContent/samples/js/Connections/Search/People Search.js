require([ "sbt/Endpoint", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/connections/ConnectionsConstants" ], 
	function(Endpoint, dom, xml, xpath, conn) {
	
		var endpoint = Endpoint.find("connections");
    
		var options = { 
			method : "GET", 
			handleAs : "text",
			query : {
				query : ""
			}
		};
	
		dom.byId("searchBtn").onclick = function(ev) {
			dom.byId("error").style.display = "none";
			dom.byId("peopleTable").style.display = "none";
			
			var topic = dom.byId("topicInput").value;
			options.query.query = topic;
			
			endpoint.request("/search/atom/search/facets/people", options).then(
				function(response) {
					var doc = xml.parse(response);
		            var entries = xpath.selectNodes(doc, "/a:feed/a:entry", conn.Namespaces);
		            
		            if (entries.length == 0) {
		            	showError("No people associated with topic: " + topic);
	                } else {
	                    for(var i=0; i<entries.length; i++){
	                        var entry = entries[i];
	                        var name = xpath.selectText(entry, "a:author/a:name", conn.Namespaces);
	                        var email = xpath.selectText(entry, "a:author/a:email", conn.Namespaces);
	                        createRow(name, email);
	                    }
	                    dom.byId("peopleTable").style.display = "";
	                }
				},
				function(error){
					showError(error.message);
				}
			);
		};
		
		var showError = function(message) {
			var errorDiv = dom.byId("error");
			errorDiv.style.display = "";
			errorDiv.innerHTML = message;
		};
		
		var createRow = function(name, email) {
            var table = dom.byId("peopleTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = name;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = email;
            tr.appendChild(td);
        };
	}
);
