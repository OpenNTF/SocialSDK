require(["sbt/dom","sbt/config"], function(dom,config) {
	var ep = config.findEndpoint("smartcloud");
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
	require(["sbt/dom","sbt/config"], function(dom,config) {
		config.Properties["loginUi"] = loginUi;
		config.findEndpoint("smartcloud").authenticate().then(
			function(response){
				dom.setText("OAuth1LoginStatus","You are authenticated");
				dom.byId("OAuth1Login").style.display = "none";
				dom.byId("OAuth1Logout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/config',"sbt/dom"], function(config,dom) {
		config.findEndpoint("smartcloud").logout().then(
			function(response){
				dom.setText("OAuth1LoginStatus","You are not authenticated");
				dom.byId("OAuth1Login").style.display = "inline";
				dom.byId("OAuth1Logout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}	
		);
	});
}