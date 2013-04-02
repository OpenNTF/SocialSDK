require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("smartcloud");
	if(ep.isAuthenticated){
		dom.setText("OAuth1LoginStatus","You are authenticated");
		dom.byId("OAuth1Login").style.display = "none";
		dom.byId("OAuth1Logout").style.display = "inline";
	}else{
		dom.setText("OAuth1LoginStatus","You are not authenticated");
		dom.byId("OAuth1Login").style.display = "inline";
		dom.byId("OAuth1Logout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("smartcloud").authenticate({
			success: function(response){
				dom.setText("OAuth1LoginStatus","You are authenticated");
				dom.byId("OAuth1Login").style.display = "none";
				dom.byId("OAuth1Logout").style.display = "inline";	
			},
			cancel: function(){
				dom.setText("content","You need to Login to Proceed");
			}
		});
	});
}

function logout() {
	require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
		Endpoint.find("smartcloud").logout({
			success: function(response){
				dom.setText("OAuth1LoginStatus","You are not authenticated");
				dom.byId("OAuth1Login").style.display = "inline";
				dom.byId("OAuth1Logout").style.display = "none";
			},
			failure: function(){
				dom.setText("content","Failed to Logout");
			}	
		});
	});
}