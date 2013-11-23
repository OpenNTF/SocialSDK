require(["sbt/dom","sbt/config"], function(dom,config) {
	var ep = config.findEndpoint("domino");
	if(ep.isAuthenticated){
		dom.setText("dominoLoginStatus","You are authenticated");
		dom.byId("dominoLogin").style.display = "none";
		dom.byId("dominoLogout").style.display = "inline";
	}else{
		dom.setText("dominoLoginStatus","You are not authenticated");
		dom.byId("dominoLogin").style.display = "inline";
		dom.byId("dominoLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(["sbt/dom","sbt/config"], function(dom,config) {
		config.Properties["loginUi"] = loginUi;
		config.findEndpoint("domino").authenticate().then(
			function(response){
				dom.setText("dominoLoginStatus","You are authenticated");
				dom.byId("dominoLogin").style.display = "none";
				dom.byId("dominoLogout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/config',"sbt/dom"], function(config,dom) {
		config.findEndpoint("domino").logout().then(
			function(response){
				dom.setText("dominoLoginStatus","You are not authenticated");
				dom.byId("dominoLogin").style.display = "inline";
				dom.byId("dominoLogout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}	
		);
	});
}