require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("smartcloudOA2");
	if(ep.isAuthenticated){
		dom.setText("OAuth2LoginStatus","You are authenticated");
		dom.byId("OAuth2Login").style.display = "none";
		dom.byId("OAuth2Logout").style.display = "inline";
	}else{
		dom.setText("OAuth2LoginStatus","You are not authenticated");
		dom.byId("OAuth2Login").style.display = "inline";
		dom.byId("OAuth2Logout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("smartcloudOA2").authenticate().then(
			function(response){
				dom.setText("OAuth2LoginStatus","You are authenticated");
				dom.byId("OAuth2Login").style.display = "none";
				dom.byId("OAuth2Logout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
		Endpoint.find("smartcloudOA2").logout().then(
			function(response){
				dom.setText("OAuth2LoginStatus","You are not authenticated");
				dom.byId("OAuth2Login").style.display = "inline";
				dom.byId("OAuth2Logout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}
		);
	});
}