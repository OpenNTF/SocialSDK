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