require(["sbt/dom","sbt/config"], function(dom,config) {
	var ep = config.findEndpoint("connectionsOA2");
	if(ep.isAuthenticated){
		dom.setText("ConnOAuth2LoginStatus","You are authenticated");
		dom.byId("ConnOAuth2Login").style.display = "none";
		dom.byId("ConnOAuth2Logout").style.display = "inline";
	}else{
		dom.setText("ConnOAuth2LoginStatus","You are not authenticated");
		dom.byId("ConnOAuth2Login").style.display = "inline";
		dom.byId("ConnOAuth2Logout").style.display = "none";
	}
});

function login(loginUi) {
	require(["sbt/dom","sbt/config"], function(dom,config) {
		config.Properties["loginUi"] = loginUi;
		config.findEndpoint("connectionsOA2").authenticate().then(
			function(response){
				dom.setText("ConnOAuth2LoginStatus","You are authenticated");
				dom.byId("ConnOAuth2Login").style.display = "none";
				dom.byId("ConnOAuth2Logout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/config',"sbt/dom"], function(config,dom) {
		config.findEndpoint("connectionsOA2").logout().then(
			function(response){
				dom.setText("ConnOAuth2LoginStatus","You are not authenticated");
				dom.byId("ConnOAuth2Login").style.display = "inline";
				dom.byId("ConnOAuth2Logout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}
		);
	});
}