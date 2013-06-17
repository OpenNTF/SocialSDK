require(["sbt/dom","sbt/config"], function(dom,config) {
	var ep = config.findEndpoint("connections");
	if(ep.isAuthenticated){
		dom.setText("connectionsLoginStatus","You are authenticated");
		dom.byId("connectionsLogin").style.display = "none";
		dom.byId("connectionsLogout").style.display = "inline";
	}else{
		dom.setText("connectionsLoginStatus","You are not authenticated");
		dom.byId("connectionsLogin").style.display = "inline";
		dom.byId("connectionsLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(["sbt/dom","sbt/config"], function(dom,config) {
		config.Properties["loginUi"] = loginUi;
		config.findEndpoint("connections").authenticate().then(
			function(response){
				dom.setText("connectionsLoginStatus","You are authenticated");
				dom.byId("connectionsLogin").style.display = "none";
				dom.byId("connectionsLogout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/config',"sbt/dom"], function(config,dom) {
		config.findEndpoint("connections").logout().then(
			function(response){
				dom.setText("connectionsLoginStatus","You are not authenticated");
				dom.byId("connectionsLogin").style.display = "inline";
				dom.byId("connectionsLogout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}	
		);
	});
}