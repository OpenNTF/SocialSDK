require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
	var ep = Endpoint.find("connections");
	ep.logout({
		success: function(response){
			dom.setText("content","You successfully logged out from Connections with response: "+response);	
		},
		failure: function(response){
			dom.setText("content","Logout unsuccessful. response: "+response);
		}	
	});
});