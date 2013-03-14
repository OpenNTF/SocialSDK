require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("connections");
	config.Properties["loginUi"] = "popup";
	if(ep.isAuthenticated){
		dom.setText("content","You are already logged in");
	}else{
		ep.authenticate({
			success: function(response){
				dom.setText("content","You successfully logged in");	
			},
			cancel: function(){
				dom.setText("content","You need to Login to Proceed");
			}
		});
	}
});