require([ "sbt/config", "sbt/dom", "sbt/Endpoint" ], function(config, dom, Endpoint) {

	var endpoint = Endpoint.find("smartcloud");
	var url = "/lotuslive-shindig-server/social/rest/people/@me/@self";
	endpoint.request(url).then(
		function(response) {
			dom.setText("first", response);
			
			// enable button
			dom.byId("getProfileBtn").onclick = function(evt) {
				getSecond();
		    };
		    dom.byId("clearProfileBtn").onclick = function(evt) {
		    	dom.setText("second", "");
		    };
		    dom.byId("postProfileBtn").onclick = function(evt) {
		    	createSubscriber();
		    };
		}, 
		function(error) {
			dom.setText("error", error.message);
		}
	);

});

function getSecond() {
	require([ "sbt/dom", "sbt/Endpoint" ], function(dom, Endpoint) {

		var endpoint = Endpoint.find("smartcloud");
		var url = "/lotuslive-shindig-server/social/rest/people/@me/@self";
		endpoint.request(url).then(
			function(response) {
				dom.setText("second", response);
			}, 
			function(error) {
				dom.setText("error", error.message);
			}
		);

	});
}

function createSubscriber() {
	require([ "sbt/dom", "sbt/Endpoint" ], function(dom, Endpoint) {

		var endpoint = Endpoint.find("smartcloud");
		var url = "/api/bss/resource/subscriber";
		var options =  { 
			method : "POST", 
			handleAs : "json",
			postData : JSON.stringify({ email : "mark_wallace@ie.ibm.com" }),
			headers : { "Content-Type" : "application/json" } 
		};
		endpoint.request(url, options).then(
			function(response) {
				dom.setText("post", response);
			}, 
			function(error) {
				dom.setText("error", error.message);
			}
		);

	});
}